package com.dearfloral.module.customorders.dto;

import com.dearfloral.common.enums.DemoResponseStatus;
import java.time.LocalDateTime;
import java.util.List;

public record CustomDemoResponse(
        Long demoId,
        Long orderId,
        Integer versionNo,
        String demoImageUrl,
        List<String> demoImages,
        String demoDescription,
        DemoResponseStatus customerResponseStatus,
        String customerFeedback,
        LocalDateTime uploadedAt,
        LocalDateTime respondedAt
) {
}
