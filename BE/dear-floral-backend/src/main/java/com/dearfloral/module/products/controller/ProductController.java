package com.dearfloral.module.products.controller;

import com.dearfloral.common.api.ApiResponse;
import com.dearfloral.common.api.PageMeta;
import com.dearfloral.common.enums.ProductKind;
import com.dearfloral.module.products.dto.ProductResponse;
import com.dearfloral.module.products.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<ProductResponse>>> getPublicProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) ProductKind productKind,
            @RequestParam(required = false) Boolean isSellableDirectly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Page<ProductResponse> data = productService.getPublicProducts(
                keyword, categoryId, productKind, isSellableDirectly, page, limit
        );
        PageMeta meta = productService.toPageMeta(data);
        return ResponseEntity.ok(ApiResponse.success("PRODUCT_LIST_FETCHED", "Product list fetched successfully.", data.getContent(), meta));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductDetail(@PathVariable Long productId) {
        ProductResponse data = productService.getPublicProductDetail(productId);
        return ResponseEntity.ok(ApiResponse.success("PRODUCT_DETAIL_FETCHED", "Product detail fetched successfully.", data));
    }

    @GetMapping("/custom-selectable")
    public ResponseEntity<ApiResponse<Iterable<ProductResponse>>> getCustomSelectableProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Page<ProductResponse> data = productService.getPublicProducts(
                keyword, categoryId, ProductKind.FRAME_OPTION, false, page, limit
        );
        PageMeta meta = productService.toPageMeta(data);
        return ResponseEntity.ok(ApiResponse.success(
                "CUSTOM_SELECTABLE_PRODUCT_LIST_FETCHED",
                "Custom selectable products fetched successfully.",
                data.getContent(),
                meta
        ));
    }
}
