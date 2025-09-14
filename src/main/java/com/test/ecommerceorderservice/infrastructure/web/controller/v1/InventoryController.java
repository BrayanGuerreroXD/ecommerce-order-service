package com.test.ecommerceorderservice.infrastructure.web.controller.v1;

import com.test.ecommerceorderservice.application.dto.request.CreateInventoryRequest;
import com.test.ecommerceorderservice.application.dto.request.UpdateInventoryQuantityRequest;
import com.test.ecommerceorderservice.application.dto.response.InventoryResponse;
import com.test.ecommerceorderservice.application.service.InventoryApplicationService;
import com.test.ecommerceorderservice.infrastructure.annotation.RoleVerify;
import com.test.ecommerceorderservice.infrastructure.enums.Role;
import com.test.ecommerceorderservice.infrastructure.web.dto.DefaultResponse;
import com.test.ecommerceorderservice.infrastructure.web.dto.SuccessResponse;
import com.test.ecommerceorderservice.shared.dto.request.FilterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RoleVerify(Role.ADMIN)
@RestController
@RequestMapping("inventories")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryApplicationService inventoryApplicationService;

    @GetMapping("find-by-product/{productId}")
    public DefaultResponse<InventoryResponse> getInventoryByProductId(@PathVariable Long productId) {
        return new DefaultResponse<>(inventoryApplicationService.getInventoryByProductId(productId));
    }

    @GetMapping("find-all-paginated")
    public DefaultResponse<Page<InventoryResponse>> findAllInventories(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "column", required = false) String column,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "search", required = false) String search
    ) {
        FilterRequest filterRequest = new FilterRequest(page, size, column, order);
        Page<InventoryResponse> inventoryPage = inventoryApplicationService.findAllInventories(search, filterRequest);
        return new DefaultResponse<>(inventoryPage);
    }

    @PostMapping
    public DefaultResponse<InventoryResponse> createInventory(@RequestBody @Valid CreateInventoryRequest req) {
        return new DefaultResponse<>(inventoryApplicationService.createInventory(req));
    }

    @PatchMapping("/{inventoryId}")
    public DefaultResponse<SuccessResponse> updateInventoryQuantity(
            @PathVariable Long inventoryId,
            @RequestBody @Valid UpdateInventoryQuantityRequest req
    ) {
        inventoryApplicationService.updateInventoryQuantity(inventoryId, req.getQuantity());
        return new DefaultResponse<>(new SuccessResponse(true));
    }

    @DeleteMapping("/{inventoryId}")
    public DefaultResponse<SuccessResponse> deleteInventory(@PathVariable Long inventoryId) {
        inventoryApplicationService.deleteInventoryById(inventoryId);
        return new DefaultResponse<>(new SuccessResponse(true));
    }

}