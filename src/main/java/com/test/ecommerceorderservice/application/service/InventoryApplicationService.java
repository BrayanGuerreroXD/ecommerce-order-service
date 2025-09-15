package com.test.ecommerceorderservice.application.service;

import com.test.ecommerceorderservice.application.dto.request.CreateInventoryRequest;
import com.test.ecommerceorderservice.application.dto.response.InventoryResponse;
import com.test.ecommerceorderservice.application.mapper.InventoryMapper;
import com.test.ecommerceorderservice.domain.model.Inventory;
import com.test.ecommerceorderservice.domain.model.Product;
import com.test.ecommerceorderservice.domain.repository.InventoryRepository;
import com.test.ecommerceorderservice.infrastructure.enums.exceptions.ExceptionCodeEnum;
import com.test.ecommerceorderservice.infrastructure.web.exception.BadRequestException;
import com.test.ecommerceorderservice.infrastructure.web.exception.ConflictException;
import com.test.ecommerceorderservice.shared.dto.request.FilterRequest;
import com.test.ecommerceorderservice.shared.util.PageableFilterConvert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryApplicationService {

    private final InventoryRepository inventoryRepository;
    private final ProductApplicationService productApplicationService;
    private final PageableFilterConvert pageableFilterConvert;

    // retry parameters for optimistic updates
    private static final int MAX_RETRIES = 3;

    @Transactional
    public InventoryResponse createInventory(CreateInventoryRequest createInventoryRequest) {
        log.info("Creating inventory for productId: {} with quantity: {}", createInventoryRequest.getProductId(),
                createInventoryRequest.getQuantity());

        Product product = productApplicationService.getProductModelById(createInventoryRequest.getProductId());

        if (inventoryRepository.existsByProductId(product.getId())) {
            throw new BadRequestException(ExceptionCodeEnum.C01INV02);
        }

        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setQuantity(createInventoryRequest.getQuantity());

        Inventory savedInventory = inventoryRepository.save(inventory);

        return InventoryMapper.toResponse(savedInventory);
    }

    public InventoryResponse getInventoryByProductId(Long productId) {
        log.info("Fetching inventory for productId: {}", productId);
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new BadRequestException(ExceptionCodeEnum.C01INV01));
        return InventoryMapper.toResponse(inventory);
    }

    public Page<InventoryResponse> findAllInventories(String search, FilterRequest filterRequest) {
        Pageable pageable = pageableFilterConvert.createPageAndSort(filterRequest);
        return inventoryRepository.findAll(
                search,
                pageable
        ).map(InventoryMapper::toResponse);
    }

    /**
     * UPDATE from CRUD / admin.
     * Here the absolute quantity (newQuantity) is SET, not added or subtracted.
     * Uses optimistic locking with retries: read-modify-save to maintain version control.
     */
    @Transactional
    public void updateInventoryQuantity(Long inventoryId, Integer newQuantity) {
        log.info("Updating inventory for inventoryId: {} setting absolute quantity: {}", inventoryId, newQuantity);
        int attempt = 0;
        while (attempt <= MAX_RETRIES) {
            attempt++;
            try {
                Inventory inv = inventoryRepository.findById(inventoryId)
                        .orElseThrow(() -> new BadRequestException(ExceptionCodeEnum.C01INV01));

                inv.setQuantity(newQuantity);
                inventoryRepository.save(inv);
                return;
            } catch (ConcurrencyFailureException ex) {
                log.warn("Optimistic lock conflict on inventoryId {} attempt {}", inventoryId, attempt);
                if (attempt >= MAX_RETRIES) {
                    throw new ConflictException(ExceptionCodeEnum.C01INV03); // persistent conflict
                }
                sleepBackoff(attempt);
            }
        }
    }

    /**
     * Fast path for Orders: atomic reservation in DB. Returns true if reserved (updated==1), false if out of stock.
     * Orders should call this method when high concurrency is needed.
     */
    @Transactional
    public boolean decreaseForOrder(Long productId, int qty) {
        log.debug("Attempting atomic decrease for productId {} qty {}", productId, qty);
        int updated = inventoryRepository.decreaseIfEnough(productId, qty);
        return updated == 1;
    }

    /**
     * Fast path for Orders: atomic increase in DB. Returns true if increased (updated==1), false if not found.
     * Orders should call this method when high concurrency is needed.
     */
    @Transactional
    public boolean increaseByProduct(Long productId, int newQty) {
        log.debug("Attempting atomic increase for productId {} setting absolute qty {}", productId, newQty);
        int updated = inventoryRepository.increaseByProduct(productId, newQty);
        return updated == 1;
    }

    private void sleepBackoff(int attempt) {
        try {
            long BASE_BACKOFF_MS = 100L;
            Thread.sleep(BASE_BACKOFF_MS * attempt);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}