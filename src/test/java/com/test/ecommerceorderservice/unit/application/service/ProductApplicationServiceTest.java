package com.test.ecommerceorderservice.unit.application.service;

import com.test.ecommerceorderservice.application.dto.request.CreateProductRequest;
import com.test.ecommerceorderservice.application.dto.request.UpdateProductRequest;
import com.test.ecommerceorderservice.application.dto.response.ProductResponse;
import com.test.ecommerceorderservice.application.service.ProductApplicationService;
import com.test.ecommerceorderservice.domain.model.Product;
import com.test.ecommerceorderservice.domain.repository.ProductRepository;
import com.test.ecommerceorderservice.infrastructure.enums.exceptions.ExceptionCodeEnum;
import com.test.ecommerceorderservice.infrastructure.web.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductApplicationServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductApplicationService productService;

    @Captor
    private ArgumentCaptor<Product> productCaptor;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createProduct_success_shouldSaveAndReturnResponse() {
        CreateProductRequest req = new CreateProductRequest();
        req.setName("My Product");
        req.setSku("SKU-123");
        req.setPrice(9.99);

        Product saved = new Product();
        saved.setId(10L);
        saved.setName("My Product");
        saved.setSku("sku-123");
        saved.setPrice(9.99);

        when(productRepository.findBySku(anyString())).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        ProductResponse resp = productService.createProduct(req);

        assertThat(resp).isNotNull();
        assertThat(resp.getId()).isEqualTo(10L);
        assertThat(resp.getSku()).isEqualTo("sku-123");
        assertThat(resp.getName()).isEqualTo("My Product");
        verify(productRepository).findBySku(eq("SKU-123"));
        verify(productRepository).save(productCaptor.capture());

        Product captured = productCaptor.getValue();
        assertThat(captured.getSku()).isEqualTo("sku-123"); // lowercased before save
        assertThat(captured.getPrice()).isEqualTo(9.99);
    }

    @Test
    void createProduct_whenSkuExists_thenThrowBadRequestException() {
        CreateProductRequest req = new CreateProductRequest();
        req.setName("P");
        req.setSku("DUP-SKU");
        req.setPrice(9.99);

        Product existing = new Product();
        existing.setId(1L);
        existing.setSku("dup-sku");

        when(productRepository.findBySku(anyString())).thenReturn(Optional.of(existing));

        BadRequestException ex = catchThrowableOfType(() -> productService.createProduct(req), BadRequestException.class);

        assertThat(ex).isNotNull();
        assertThat(ex.getErrorResponse()).isNotNull();
        assertThat(ex.getErrorResponse().getErrors().getCode().toString())
                .isEqualTo(ExceptionCodeEnum.C01PRD02.name());
        verify(productRepository, never()).save(any());
    }

    @Test
    void updateProduct_success_whenSkuUnchanged_shouldSaveWithoutCheckingSkuExistence() {
        // Arrange
        Long id = 100L;
        Product existing = new Product();
        existing.setId(id);
        existing.setName("Old");
        existing.setSku("sku-100");
        existing.setPrice(5.0);

        UpdateProductRequest req = new UpdateProductRequest();
        req.setName("Updated");
        req.setSku("SKU-100"); // same ignoring case
        req.setPrice(6.0);

        when(productRepository.findById(eq(id))).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        ProductResponse resp = productService.updateProduct(id, req);

        // Assert
        assertThat(resp).isNotNull();
        assertThat(resp.getName()).isEqualTo("Updated");
        assertThat(resp.getSku()).isEqualTo("sku-100"); // stored lowercased
        verify(productRepository, times(1)).findById(eq(id));
        verify(productRepository, never()).findBySku(anyString()); // no check because sku unchanged (case-insensitive)
        verify(productRepository).save(productCaptor.capture());

        Product saved = productCaptor.getValue();
        assertThat(saved.getSku()).isEqualTo("sku-100");
        assertThat(saved.getPrice()).isEqualTo(6.0);
    }

    @Test
    void updateProduct_success_whenSkuChanged_shouldValidateSkuThenSave() {
        // Arrange
        Long id = 200L;
        Product existing = new Product();
        existing.setId(id);
        existing.setName("Old");
        existing.setSku("old-sku");
        existing.setPrice(5.0);

        UpdateProductRequest req = new UpdateProductRequest();
        req.setName("NewName");
        req.setSku("NEW-SKU");
        req.setPrice(7.0);

        when(productRepository.findById(eq(id))).thenReturn(Optional.of(existing));
        when(productRepository.findBySku(eq("NEW-SKU"))).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        ProductResponse resp = productService.updateProduct(id, req);

        // Assert
        assertThat(resp).isNotNull();
        assertThat(resp.getName()).isEqualTo("NewName");
        assertThat(resp.getSku()).isEqualTo("new-sku");
        verify(productRepository).findBySku(eq("NEW-SKU"));
        verify(productRepository).save(productCaptor.capture());
        assertThat(productCaptor.getValue().getSku()).isEqualTo("new-sku");
    }

    @Test
    void updateProduct_whenNotFound_thenThrowBadRequestException() {
        // Arrange
        Long id = 999L;
        UpdateProductRequest req = new UpdateProductRequest();
        req.setName("X");
        req.setSku("XSKU");
        req.setPrice(1.0);

        when(productRepository.findById(eq(id))).thenReturn(Optional.empty());

        // Act & Assert
        BadRequestException ex = catchThrowableOfType(() -> productService.updateProduct(id, req), BadRequestException.class);
        assertThat(ex).isNotNull();
        assertThat(ex.getErrorResponse().getErrors().getCode().toString())
                .isEqualTo(ExceptionCodeEnum.C01PRD01.name());
        verify(productRepository, never()).save(any());
    }

    @Test
    void getProductById_success_and_notFound() {
        // success
        Product p = new Product();
        p.setId(11L);
        p.setName("P11");
        p.setSku("p11");
        p.setPrice(3.33);

        when(productRepository.findById(11L)).thenReturn(Optional.of(p));

        ProductResponse r = productService.getProductById(11L);
        assertThat(r).isNotNull();
        assertThat(r.getId()).isEqualTo(11L);
        assertThat(r.getSku()).isEqualTo("p11");

        // not found
        when(productRepository.findById(22L)).thenReturn(Optional.empty());
        BadRequestException ex = catchThrowableOfType(() -> productService.getProductById(22L), BadRequestException.class);
        assertThat(ex.getErrorResponse().getErrors().getCode().toString()).isEqualTo(ExceptionCodeEnum.C01PRD01.name());
    }

    @Test
    void getAllProducts_returnsMappedList() {
        // Arrange
        Product p1 = new Product();
        p1.setId(1L);
        p1.setName("A");
        p1.setSku("a");
        p1.setPrice(1.0);

        Product p2 = new Product();
        p2.setId(2L);
        p2.setName("B");
        p2.setSku("b");
        p2.setPrice(2.0);

        when(productRepository.findAll()).thenReturn(List.of(p1, p2));

        // Act
        List<ProductResponse> all = productService.getAllProducts();

        // Assert
        assertThat(all).hasSize(2);
        assertThat(all).extracting(ProductResponse::getId).containsExactly(1L, 2L);
    }

    @Test
    void getProductModelById_whenNotFound_throw() {
        when(productRepository.findById(500L)).thenReturn(Optional.empty());
        BadRequestException ex = catchThrowableOfType(() -> productService.getProductModelById(500L), BadRequestException.class);
        assertThat(ex.getErrorResponse().getErrors().getCode().toString()).isEqualTo(ExceptionCodeEnum.C01PRD01.name());
    }
}
