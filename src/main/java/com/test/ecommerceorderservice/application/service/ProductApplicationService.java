package com.test.ecommerceorderservice.application.service;

import com.test.ecommerceorderservice.application.dto.request.CreateProductRequest;
import com.test.ecommerceorderservice.application.dto.request.UpdateProductRequest;
import com.test.ecommerceorderservice.application.dto.response.ProductResponse;
import com.test.ecommerceorderservice.domain.model.Product;
import com.test.ecommerceorderservice.domain.repository.ProductRepository;
import com.test.ecommerceorderservice.infrastructure.enums.exceptions.ExceptionCodeEnum;
import com.test.ecommerceorderservice.infrastructure.web.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductApplicationService {

    private final ProductRepository productRepository;
    public static final String CACHE = "product";

    @Transactional
    @CachePut(value = CACHE, key = "#result.id")
    public ProductResponse createProduct(CreateProductRequest createProductRequest) {
        log.info("Creating product with name: {} and SKU: {}", createProductRequest.getName(),
                createProductRequest.getSku());

        this.validateIfSkuAlreadyExists(createProductRequest.getSku());

        Product product = new Product();
        product.setName(createProductRequest.getName());
        product.setPrice(createProductRequest.getPrice());
        product.setSku(createProductRequest.getSku().toLowerCase());
        product.setPrice(createProductRequest.getPrice());
        return this.toResponse(productRepository.save(product));
    }

    @Transactional
    @CachePut(value = CACHE, key = "#result.id")
    public ProductResponse updateProduct(Long id, UpdateProductRequest updateProductRequest) {
        log.info("Updating product with id: {}", id);

        Product existingProduct = productRepository.findById(id).orElseThrow(
                () -> new BadRequestException(ExceptionCodeEnum.C01PRD01)
        );

        if (!existingProduct.getSku().equalsIgnoreCase(updateProductRequest.getSku())) {
            this.validateIfSkuAlreadyExists(updateProductRequest.getSku());
        }

        existingProduct.setName(updateProductRequest.getName());
        existingProduct.setPrice(updateProductRequest.getPrice());
        existingProduct.setSku(updateProductRequest.getSku().toLowerCase());
        existingProduct.setPrice(updateProductRequest.getPrice());

        return this.toResponse(productRepository.save(existingProduct));
    }

    @Cacheable(value = CACHE, key = "#id")
    public ProductResponse getProductById(Long id) {
        log.info("Fetching product with id: {}", id);
        Product product = productRepository.findById(id).orElseThrow(
                () -> new BadRequestException(ExceptionCodeEnum.C01PRD01)
        );
        return this.toResponse(product);
    }

    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll().stream().map(this::toResponse).toList();
    }

    public Product getProductModelById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new BadRequestException(ExceptionCodeEnum.C01PRD01)
        );
    }

    private ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setSku(product.getSku());
        response.setPrice(product.getPrice());
        return response;
    }

    private void validateIfSkuAlreadyExists(String sku) {
        productRepository.findBySku(sku).ifPresent(
                p -> {
                    throw new BadRequestException(ExceptionCodeEnum.C01PRD02);
                }
        );
    }
}

