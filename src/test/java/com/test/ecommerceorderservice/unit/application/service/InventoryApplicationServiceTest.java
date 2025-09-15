package com.test.ecommerceorderservice.unit.application.service;

import com.test.ecommerceorderservice.application.dto.request.CreateInventoryRequest;
import com.test.ecommerceorderservice.application.dto.response.InventoryResponse;
import com.test.ecommerceorderservice.application.service.InventoryApplicationService;
import com.test.ecommerceorderservice.application.service.ProductApplicationService;
import com.test.ecommerceorderservice.domain.model.Inventory;
import com.test.ecommerceorderservice.domain.model.Product;
import com.test.ecommerceorderservice.domain.repository.InventoryRepository;
import com.test.ecommerceorderservice.infrastructure.enums.exceptions.ExceptionCodeEnum;
import com.test.ecommerceorderservice.infrastructure.web.exception.BadRequestException;
import com.test.ecommerceorderservice.infrastructure.web.exception.ConflictException;
import com.test.ecommerceorderservice.shared.dto.request.FilterRequest;
import com.test.ecommerceorderservice.shared.util.PageableFilterConvert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class InventoryApplicationServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private ProductApplicationService productApplicationService;

    @Mock
    private PageableFilterConvert pageableFilterConvert;

    @InjectMocks
    private InventoryApplicationService inventoryService;

    @Test
    void createInventory_success() {
        // Arrange
        CreateInventoryRequest req = new CreateInventoryRequest();
        req.setProductId(1L);
        req.setQuantity(10);

        Product product = new Product();
        product.setId(1L);

        Inventory inv = new Inventory();
        inv.setId(100L);
        inv.setProduct(product);
        inv.setQuantity(10);

        when(productApplicationService.getProductModelById(1L)).thenReturn(product);
        when(inventoryRepository.existsByProductId(1L)).thenReturn(false);
        when(inventoryRepository.save(any())).thenReturn(inv);

        // Act
        InventoryResponse resp = inventoryService.createInventory(req);

        // Assert
        assertThat(resp).isNotNull();
        assertThat(resp.getId()).isEqualTo(100L);
        assertThat(resp.getQuantity()).isEqualTo(10);
        verify(inventoryRepository).save(any(Inventory.class));
    }

    @Test
    void createInventory_whenAlreadyExists_thenThrowBadRequest() {
        CreateInventoryRequest req = new CreateInventoryRequest();
        req.setProductId(1L);
        req.setQuantity(5);

        Product product = new Product();
        product.setId(1L);

        when(productApplicationService.getProductModelById(1L)).thenReturn(product);
        when(inventoryRepository.existsByProductId(1L)).thenReturn(true);

        BadRequestException ex = catchThrowableOfType(
                () -> inventoryService.createInventory(req),
                BadRequestException.class
        );

        assertThat(ex.getErrorResponse().getErrors().getCode().toString())
                .isEqualTo(ExceptionCodeEnum.C01INV02.name());
        verify(inventoryRepository, never()).save(any());
    }

    @Test
    void getInventoryByProductId_success() {
        Product p = new Product();
        p.setId(1L);

        Inventory inv = new Inventory();
        inv.setId(200L);
        inv.setProduct(p);
        inv.setQuantity(7);

        when(inventoryRepository.findByProductId(1L)).thenReturn(Optional.of(inv));

        InventoryResponse resp = inventoryService.getInventoryByProductId(1L);

        assertThat(resp.getId()).isEqualTo(200L);
        assertThat(resp.getQuantity()).isEqualTo(7);
    }

    @Test
    void getInventoryByProductId_notFound_throwsBadRequest() {
        when(inventoryRepository.findByProductId(2L)).thenReturn(Optional.empty());

        BadRequestException ex = catchThrowableOfType(
                () -> inventoryService.getInventoryByProductId(2L),
                BadRequestException.class
        );

        assertThat(ex.getErrorResponse().getErrors().getCode().toString())
                .isEqualTo(ExceptionCodeEnum.C01INV01.name());
    }

    @Test
    void findAllInventories_success() {
        FilterRequest filter = new FilterRequest();
        Pageable pageable = PageRequest.of(0, 10);

        Inventory inv1 = new Inventory(); inv1.setId(1L);
        Inventory inv2 = new Inventory(); inv2.setId(2L);

        Page<Inventory> page = new PageImpl<>(List.of(inv1, inv2));

        when(pageableFilterConvert.createPageAndSort(filter)).thenReturn(pageable);
        when(inventoryRepository.findAll("search", pageable)).thenReturn(page);

        Page<InventoryResponse> resp = inventoryService.findAllInventories("search", filter);

        assertThat(resp).hasSize(2);
        assertThat(resp.getContent().get(0).getId()).isEqualTo(1L);
    }

    @Test
    void updateInventoryQuantity_success() {
        Long invId = 300L;
        Inventory inv = new Inventory();
        inv.setId(invId);
        inv.setQuantity(5);

        when(inventoryRepository.findById(invId)).thenReturn(Optional.of(inv));
        when(inventoryRepository.save(any())).thenReturn(inv);

        inventoryService.updateInventoryQuantity(invId, 20);

        verify(inventoryRepository).save(inv);
        assertThat(inv.getQuantity()).isEqualTo(20);
    }

    @Test
    void updateInventoryQuantity_withConflicts_thenThrowConflict() {
        Long invId = 400L;
        Inventory inv = new Inventory();
        inv.setId(invId);
        inv.setQuantity(5);

        when(inventoryRepository.findById(invId)).thenReturn(Optional.of(inv));
        // always throw concurrency exception
        when(inventoryRepository.save(any())).thenThrow(new ConcurrencyFailureException("conflict"));

        ConflictException ex = catchThrowableOfType(
                () -> inventoryService.updateInventoryQuantity(invId, 30),
                ConflictException.class
        );

        assertThat(ex.getErrorResponse().getErrors().getCode().toString())
                .isEqualTo(ExceptionCodeEnum.C01INV03.name());
    }

    @Test
    void decreaseForOrder_successAndFail() {
        when(inventoryRepository.decreaseIfEnough(1L, 2)).thenReturn(1);
        when(inventoryRepository.decreaseIfEnough(2L, 5)).thenReturn(0);

        assertThat(inventoryService.decreaseForOrder(1L, 2)).isTrue();
        assertThat(inventoryService.decreaseForOrder(2L, 5)).isFalse();
    }

    @Test
    void increaseByProduct_successAndFail() {
        when(inventoryRepository.increaseByProduct(1L, 10)).thenReturn(1);
        when(inventoryRepository.increaseByProduct(2L, 15)).thenReturn(0);

        assertThat(inventoryService.increaseByProduct(1L, 10)).isTrue();
        assertThat(inventoryService.increaseByProduct(2L, 15)).isFalse();
    }
}
