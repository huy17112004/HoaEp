package com.dearfloral.module.customorders.dto;

import com.dearfloral.common.enums.DemoResponseStatus;
import java.time.LocalDateTime;

public record CustomDemoResponse(
        Long demoId,
        Long orderId,
        Integer versionNo,
        String demoImageUrl,
        String demoDescription,
        DemoResponseStatus customerResponseStatus,
        String customerFeedback,
        LocalDateTime uploadedAt,
        LocalDateTime respondedAt
) {
}
