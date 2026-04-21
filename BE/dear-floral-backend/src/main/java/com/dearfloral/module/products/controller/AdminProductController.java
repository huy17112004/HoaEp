package com.dearfloral.module.products.controller;

import com.dearfloral.common.api.ApiResponse;
import com.dearfloral.common.api.PageMeta;
import com.dearfloral.common.enums.ProductKind;
import com.dearfloral.config.security.UserPrincipal;
import com.dearfloral.module.products.dto.ProductResponse;
import com.dearfloral.module.products.dto.ProductUpsertRequest;
import com.dearfloral.module.products.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final ProductService productService;

    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<ProductResponse>>> getProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) ProductKind productKind,
            @RequestParam(required = false) Boolean isSellableDirectly,
            @RequestParam(required = false) Boolean isCustomSelectable,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Page<ProductResponse> data = productService.getAdminProducts(
                keyword, categoryId, productKind, isSellableDirectly, isCustomSelectable, page, limit
        );
        PageMeta meta = productService.toPageMeta(data);
        return ResponseEntity.ok(ApiResponse.success("PRODUCT_LIST_FETCHED", "Product list fetched successfully.", data.getContent(), meta));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @ModelAttribute ProductUpsertRequest request
    ) {
        ProductResponse data = productService.createProduct(request, principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success("PRODUCT_CREATED", "Product created successfully.", data));
    }

    @PutMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable Long productId,
            @Valid @ModelAttribute ProductUpsertRequest request
    ) {
        ProductResponse data = productService.updateProduct(productId, request, principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success("PRODUCT_UPDATED", "Product updated successfully.", data));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(ApiResponse.success("PRODUCT_DELETED", "Product deleted successfully.", null));
    }
}
