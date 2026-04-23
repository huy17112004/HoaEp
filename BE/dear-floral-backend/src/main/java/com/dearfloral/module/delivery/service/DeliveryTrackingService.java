package com.dearfloral.module.delivery.service;

import com.dearfloral.common.api.PageMeta;
import com.dearfloral.module.delivery.dto.DeliveryTrackingResponse;
import com.dearfloral.module.delivery.entity.AvailableDeliveryRecordEntity;
import com.dearfloral.module.delivery.entity.CustomDeliveryRecordEntity;
import com.dearfloral.module.delivery.repository.AvailableDeliveryRecordRepository;
import com.dearfloral.module.delivery.repository.CustomDeliveryRecordRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeliveryTrackingService {

    private final AvailableDeliveryRecordRepository availableDeliveryRecordRepository;
    private final CustomDeliveryRecordRepository customDeliveryRecordRepository;

    public DeliveryTrackingService(
            AvailableDeliveryRecordRepository availableDeliveryRecordRepository,
            CustomDeliveryRecordRepository customDeliveryRecordRepository
    ) {
        this.availableDeliveryRecordRepository = availableDeliveryRecordRepository;
        this.customDeliveryRecordRepository = customDeliveryRecordRepository;
    }

    @Transactional(readOnly = true)
    public List<DeliveryTrackingResponse> getTrackingRecords(String deliveryStatus, int page, int limit) {
        String normalizedStatus = deliveryStatus == null ? null : deliveryStatus.trim().toUpperCase(Locale.ROOT);
        List<DeliveryTrackingResponse> rows = new ArrayList<>();

        for (AvailableDeliveryRecordEntity record : availableDeliveryRecordRepository.findAll()) {
            if (normalizedStatus != null && !normalizedStatus.equals(record.getDeliveryStatus())) {
                continue;
            }
            rows.add(new DeliveryTrackingResponse(
                    "AVAILABLE_ORDER",
                    record.getId(),
                    record.getAvailableOrder().getId(),
                    record.getAvailableOrder().getOrderCode(),
                    "SHIP_OUTPUT",
                    record.getDeliveryStatus(),
                    formatAddress(
                            record.getAvailableOrder().getShippingAddress().getAddressLine(),
                            record.getAvailableOrder().getShippingAddress().getDistrict(),
                            record.getAvailableOrder().getShippingAddress().getProvince()
                    ),
                    record.getAvailableOrder().getOrderedAt(),
                    resolveEventTime(record.getShippedTime(), record.getDeliveredTime(), record.getCreatedAt()),
                    record.getReceiverNote()
            ));
        }

        for (CustomDeliveryRecordEntity record : customDeliveryRecordRepository.findAll()) {
            if (normalizedStatus != null && !normalizedStatus.equals(record.getDeliveryStatus())) {
                continue;
            }
            rows.add(new DeliveryTrackingResponse(
                    "CUSTOM_ORDER",
                    record.getId(),
                    record.getCustomOrder().getId(),
                    record.getCustomOrder().getOrderCode(),
                    record.getDeliveryType().name(),
                    record.getDeliveryStatus(),
                    formatAddress(
                            record.getCustomOrder().getShippingAddress().getAddressLine(),
                            record.getCustomOrder().getShippingAddress().getDistrict(),
                            record.getCustomOrder().getShippingAddress().getProvince()
                    ),
                    record.getCustomOrder().getOrderedAt(),
                    resolveEventTime(record.getPickupTime(), record.getShippedTime(), record.getDeliveredTime(), record.getCreatedAt()),
                    record.getReceiverNote()
            ));
        }

        rows.sort(Comparator.comparing(DeliveryTrackingResponse::eventTime, Comparator.nullsLast(Comparator.naturalOrder())).reversed());
        int fromIndex = Math.min(page * limit, rows.size());
        int toIndex = Math.min(fromIndex + limit, rows.size());
        return rows.subList(fromIndex, toIndex);
    }

    @Transactional(readOnly = true)
    public PageMeta toPageMeta(String deliveryStatus, int page, int limit) {
        String normalizedStatus = deliveryStatus == null ? null : deliveryStatus.trim().toUpperCase(Locale.ROOT);
        long totalItems = availableDeliveryRecordRepository.findAll().stream()
                .filter(record -> normalizedStatus == null || normalizedStatus.equals(record.getDeliveryStatus()))
                .count()
                + customDeliveryRecordRepository.findAll().stream()
                .filter(record -> normalizedStatus == null || normalizedStatus.equals(record.getDeliveryStatus()))
                .count();
        int totalPages = (int) Math.ceil((double) totalItems / limit);

        return PageMeta.builder()
                .page(page)
                .limit(limit)
                .totalItems(totalItems)
                .totalPages(totalPages)
                .build();
    }

    private LocalDateTime resolveEventTime(LocalDateTime... candidates) {
        for (LocalDateTime candidate : candidates) {
            if (candidate != null) {
                return candidate;
            }
        }
        return null;
    }

    private String formatAddress(String addressLine, String district, String province) {
        return addressLine + ", " + district + ", " + province;
    }
}
