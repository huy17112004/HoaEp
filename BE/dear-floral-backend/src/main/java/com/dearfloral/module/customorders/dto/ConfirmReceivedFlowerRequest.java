package com.dearfloral.module.customorders.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record ConfirmReceivedFlowerRequest(
        @NotNull(message = "receivedFlowerImageFile is required.")
        MultipartFile receivedFlowerImageFile,
        String note
) {
}
