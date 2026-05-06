package com.dearfloral.module.purchasereceipts.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public record UpdatePurchaseReceiptRequest(
        @NotNull(message = "receiptDate is required.")
        LocalDate receiptDate,

        @Size(max = 500, message = "note must be at most 500 characters.")
        String note,

        @NotEmpty(message = "items must not be empty.")
        List<@Valid PurchaseReceiptItemRequest> items
) {
}

