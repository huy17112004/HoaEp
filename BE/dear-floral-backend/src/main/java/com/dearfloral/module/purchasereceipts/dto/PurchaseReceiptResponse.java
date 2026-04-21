package com.dearfloral.module.purchasereceipts.dto;

import java.time.LocalDate;
import java.util.List;

public record PurchaseReceiptResponse(
        Long purchaseReceiptId,
        String receiptCode,
        LocalDate receiptDate,
        String note,
        List<PurchaseReceiptItemResponse> items
) {
}
