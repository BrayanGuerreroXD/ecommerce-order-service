package com.test.ecommerceorderservice.infrastructure.web.controller.v1;

import com.test.ecommerceorderservice.application.dto.request.CreateProductRequest;
import com.test.ecommerceorderservice.application.dto.request.UpdateProductRequest;
import com.test.ecommerceorderservice.application.dto.response.ProductResponse;
import com.test.ecommerceorderservice.application.service.ProductApplicationService;
import com.test.ecommerceorderservice.infrastructure.annotation.RoleVerify;
import com.test.ecommerceorderservice.infrastructure.enums.Role;
import com.test.ecommerceorderservice.infrastructure.web.dto.DefaultResponse;
import com.test.ecommerceorderservice.infrastructure.web.dto.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RoleVerify(Role.ADMIN)
@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductApplicationService productApplicationService;

    @PostMapping
    public DefaultResponse<ProductResponse> create(@RequestBody @Valid CreateProductRequest req) {
        ProductResponse res = productApplicationService.createProduct(req);
        return new DefaultResponse<>(res);
    }

    @GetMapping("/{id}")
    public DefaultResponse<ProductResponse> get(@PathVariable Long id) {
        return new DefaultResponse<>(productApplicationService.getProductById(id));
    }

    @GetMapping
    public DefaultResponse<List<ProductResponse>> list() {
        return new DefaultResponse<>(productApplicationService.getAllProducts());
    }

    @PutMapping("/{id}")
    public DefaultResponse<ProductResponse> update(@PathVariable Long id, @RequestBody @Valid UpdateProductRequest req) {
        return new DefaultResponse<>(productApplicationService.updateProduct(id, req));
    }

    @DeleteMapping("/{id}")
    public DefaultResponse<SuccessResponse> delete(@PathVariable Long id) {
        return new DefaultResponse<>(productApplicationService.deleteProduct(id));
    }
}