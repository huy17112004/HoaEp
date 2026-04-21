package com.dearfloral.module.delivery.service;

import com.dearfloral.common.exception.BusinessException;
import com.dearfloral.common.exception.NotFoundException;
import com.dearfloral.common.enums.AvailableOrderStatus;
import com.dearfloral.module.availableorders.entity.AvailableOrderEntity;
import com.dearfloral.module.availableorders.repository.AvailableOrderRepository;
import com.dearfloral.module.delivery.dto.AvailableDeliveryResponse;
import com.dearfloral.module.delivery.dto.UpdateAvailableDeliveryRequest;
import com.dearfloral.module.delivery.entity.AvailableDeliveryRecordEntity;
import com.dearfloral.module.delivery.repository.AvailableDeliveryRecordRepository;
import com.dearfloral.module.reports.service.AuditLogService;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AvailableOrderDeliveryService {

    private final AvailableOrderRepository availableOrderRepository;
    private final AvailableDeliveryRecordRepository availableDeliveryRecordRepository;
    private final AuditLogService auditLogService;

    public AvailableOrderDeliveryService(
            AvailableOrderRepository availableOrderRepository,
            AvailableDeliveryRecordRepository availableDeliveryRecordRepository,
            AuditLogService auditLogService
    ) {
        this.availableOrderRepository = availableOrderRepository;
        this.availableDeliveryRecordRepository = availableDeliveryRecordRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public AvailableDeliveryResponse updateDelivery(Long orderId, UpdateAvailableDeliveryRequest request, Long actorUserId) {
        AvailableOrderEntity order = availableOrderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("ORDER_NOT_FOUND", "Order not found."));

        if (order.getOrderStatus() == AvailableOrderStatus.CANCELED) {
            throw new BusinessException("ORDER_CANCELED", "Cannot update delivery for canceled order.");
        }

        AvailableDeliveryRecordEntity deliveryRecord = new AvailableDeliveryRecordEntity();
        deliveryRecord.setAvailableOrder(order);
        String normalizedStatus = request.deliveryStatus().trim().toUpperCase();
        deliveryRecord.setDeliveryStatus(normalizedStatus);
        deliveryRecord.setReceiverNote(request.deliveryNote() == null ? null : request.deliveryNote().trim());

        LocalDateTime effectiveTime = request.deliveryTime() == null ? LocalDateTime.now() : request.deliveryTime();
        if ("SHIPPED".equals(normalizedStatus)) {
            deliveryRecord.setShippedTime(effectiveTime);
        }
        if ("DELIVERED".equals(normalizedStatus)) {
            deliveryRecord.setDeliveredTime(effectiveTime);
        }

        AvailableDeliveryRecordEntity saved = availableDeliveryRecordRepository.save(deliveryRecord);
        auditLogService.logAction(
                actorUserId,
                "AVAILABLE_ORDER_DELIVERY_UPDATED",
                "AVAILABLE_ORDER",
                orderId,
                "deliveryStatus=" + normalizedStatus
        );
        return new AvailableDeliveryResponse(
                order.getId(),
                order.getOrderCode(),
                saved.getDeliveryStatus(),
                saved.getReceiverNote(),
                saved.getShippedTime(),
                saved.getDeliveredTime()
        );
    }

    public AvailableDeliveryResponse getLatestDelivery(Long orderId) {
        AvailableOrderEntity order = availableOrderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("ORDER_NOT_FOUND", "Order not found."));
        AvailableDeliveryRecordEntity record = availableDeliveryRecordRepository
                .findTopByAvailableOrderIdOrderByCreatedAtDesc(orderId)
                .orElseThrow(() -> new NotFoundException("DELIVERY_RECORD_NOT_FOUND", "Delivery record not found."));

        return new AvailableDeliveryResponse(
                order.getId(),
                order.getOrderCode(),
                record.getDeliveryStatus(),
                record.getReceiverNote(),
                record.getShippedTime(),
                record.getDeliveredTime()
        );
    }
}
