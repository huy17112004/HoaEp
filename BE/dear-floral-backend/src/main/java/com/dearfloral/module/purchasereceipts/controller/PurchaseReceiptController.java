package com.dearfloral.module.purchasereceipts.controller;

import com.dearfloral.common.api.ApiResponse;
import com.dearfloral.common.api.PageMeta;
import com.dearfloral.config.security.UserPrincipal;
import com.dearfloral.module.purchasereceipts.dto.CreatePurchaseReceiptRequest;
import com.dearfloral.module.purchasereceipts.dto.PurchaseReceiptResponse;
import com.dearfloral.module.purchasereceipts.service.PurchaseReceiptService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/purchase-receipts")
public class PurchaseReceiptController {

    private final PurchaseReceiptService purchaseReceiptService;

    public PurchaseReceiptController(PurchaseReceiptService purchaseReceiptService) {
        this.purchaseReceiptService = purchaseReceiptService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PurchaseReceiptResponse>> createReceipt(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody CreatePurchaseReceiptRequest request
    ) {
        PurchaseReceiptResponse data = purchaseReceiptService.createReceipt(request, principal.getUserId());
        return ResponseEntity.ok(ApiResponse.success("PURCHASE_RECEIPT_CREATED", "Purchase receipt created successfully.", data));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Iterable<PurchaseReceiptResponse>>> getReceipts(
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Page<PurchaseReceiptResponse> data = purchaseReceiptService.getReceipts(fromDate, toDate, keyword, page, limit);
        PageMeta meta = purchaseReceiptService.toPageMeta(data);
        return ResponseEntity.ok(ApiResponse.success("PURCHASE_RECEIPT_LIST_FETCHED", "Purchase receipt list fetched successfully.", data.getContent(), meta));
    }
}
