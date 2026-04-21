package com.dearfloral.module.inventory.controller;

import com.dearfloral.common.api.ApiResponse;
import com.dearfloral.common.api.PageMeta;
import com.dearfloral.common.enums.ProductKind;
import com.dearfloral.module.inventory.dto.InventoryItemResponse;
import com.dearfloral.module.inventory.service.InventoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<InventoryItemResponse>>> getInventory(
            @RequestParam(required = false) ProductKind productKind,
            @RequestParam(required = false) Boolean isSellableDirectly,
            @RequestParam(required = false) Boolean isCustomSelectable,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Page<InventoryItemResponse> data = inventoryService.getInventoryItems(
                productKind, isSellableDirectly, isCustomSelectable, keyword, page, limit
        );
        PageMeta meta = inventoryService.toPageMeta(data);
        return ResponseEntity.ok(ApiResponse.success("INVENTORY_FETCHED", "Inventory fetched successfully.", data.getContent(), meta));
    }
}
