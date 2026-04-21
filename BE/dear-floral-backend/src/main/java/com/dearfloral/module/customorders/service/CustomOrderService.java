package com.dearfloral.module.customorders.service;

import com.dearfloral.common.api.PageMeta;
import com.dearfloral.common.enums.CustomOrderStatus;
import com.dearfloral.common.enums.CustomPaymentStage;
import com.dearfloral.common.enums.DemoResponseStatus;
import com.dearfloral.common.enums.DeliveryType;
import com.dearfloral.common.enums.FlowerEvaluationStatus;
import com.dearfloral.common.enums.InventoryTransactionType;
import com.dearfloral.common.enums.ProductKind;
import com.dearfloral.common.enums.RoleCode;
import com.dearfloral.common.exception.BusinessException;
import com.dearfloral.common.exception.NotFoundException;
import com.dearfloral.module.auth.entity.UserEntity;
import com.dearfloral.module.auth.repository.UserRepository;
import com.dearfloral.module.customorders.dto.CreateCustomDemoRequest;
import com.dearfloral.module.customorders.dto.CreateCustomOrderRequest;
import com.dearfloral.module.customorders.dto.CreateCustomOrderResponse;
import com.dearfloral.module.customorders.dto.CreateRemainingPaymentRequest;
import com.dearfloral.module.customorders.dto.CustomDeliveryResponse;
import com.dearfloral.module.customorders.dto.CustomDemoResponse;
import com.dearfloral.module.customorders.dto.CustomOrderResponse;
import com.dearfloral.module.customorders.dto.CustomOrderStatusResponse;
import com.dearfloral.module.customorders.dto.DemoFeedbackAction;
import com.dearfloral.module.customorders.dto.DemoFeedbackRequest;
import com.dearfloral.module.customorders.dto.DemoFeedbackResponse;
import com.dearfloral.module.customorders.dto.EvaluateFlowerInputRequest;
import com.dearfloral.module.customorders.dto.EvaluateFlowerInputResponse;
import com.dearfloral.module.customorders.dto.RemainingPaymentResponse;
import com.dearfloral.module.customorders.dto.UpdateCustomDeliveryRequest;
import com.dearfloral.module.customorders.dto.UpdateCustomOrderStatusRequest;
import com.dearfloral.module.customorders.entity.CustomDemoEntity;
import com.dearfloral.module.customorders.entity.CustomOrderEntity;
import com.dearfloral.module.customorders.entity.CustomOrderPaymentEntity;
import com.dearfloral.module.customorders.entity.CustomOrderStatusHistoryEntity;
import com.dearfloral.module.customorders.repository.CustomDemoRepository;
import com.dearfloral.module.customorders.repository.CustomOrderPaymentRepository;
import com.dearfloral.module.customorders.repository.CustomOrderRepository;
import com.dearfloral.module.customorders.repository.CustomOrderStatusHistoryRepository;
import com.dearfloral.module.delivery.entity.CustomDeliveryRecordEntity;
import com.dearfloral.module.delivery.repository.CustomDeliveryRecordRepository;
import com.dearfloral.module.inventory.entity.InventoryItemEntity;
import com.dearfloral.module.inventory.entity.InventoryTransactionEntity;
import com.dearfloral.module.inventory.repository.InventoryItemRepository;
import com.dearfloral.module.inventory.repository.InventoryTransactionRepository;
import com.dearfloral.module.products.entity.ProductEntity;
import com.dearfloral.module.products.repository.ProductRepository;
import com.dearfloral.module.reports.service.AuditLogService;
import com.dearfloral.module.users.entity.CustomerAddressEntity;
import com.dearfloral.module.users.repository.CustomerAddressRepository;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class CustomOrderService {

    private static final String PAYMENT_STATUS_PAID = "PAID";
    private static final String PAYMENT_STATUS_PARTIALLY_PAID = "PARTIALLY_PAID";
    private static final BigDecimal DEFAULT_DEPOSIT_RATE = new BigDecimal("0.50");
    private static final BigDecimal DEFAULT_EXTRA_REVISION_FEE_RATE = new BigDecimal("0.1000");
    private static final int FREE_REVISION_LIMIT = 3;
    private static final Map<CustomOrderStatus, EnumSet<CustomOrderStatus>> ALLOWED_TRANSITIONS = Map.of(
            CustomOrderStatus.DEPOSITED, EnumSet.of(CustomOrderStatus.WAITING_FLOWER_REVIEW, CustomOrderStatus.CANCELED),
            CustomOrderStatus.WAITING_FLOWER_REVIEW, EnumSet.of(CustomOrderStatus.IN_PROGRESS, CustomOrderStatus.CANCELED),
            CustomOrderStatus.IN_PROGRESS, EnumSet.of(
                    CustomOrderStatus.WAITING_DEMO_FEEDBACK,
                    CustomOrderStatus.WAITING_REMAINING_PAYMENT,
                    CustomOrderStatus.COMPLETED,
                    CustomOrderStatus.CANCELED
            ),
            CustomOrderStatus.WAITING_DEMO_FEEDBACK, EnumSet.of(
                    CustomOrderStatus.IN_PROGRESS,
                    CustomOrderStatus.WAITING_REMAINING_PAYMENT,
                    CustomOrderStatus.CANCELED
            ),
            CustomOrderStatus.WAITING_REMAINING_PAYMENT, EnumSet.of(CustomOrderStatus.IN_PROGRESS, CustomOrderStatus.CANCELED),
            CustomOrderStatus.COMPLETED, EnumSet.noneOf(CustomOrderStatus.class),
            CustomOrderStatus.CANCELED, EnumSet.noneOf(CustomOrderStatus.class)
    );

    private final CustomOrderRepository customOrderRepository;
    private final CustomDemoRepository customDemoRepository;
    private final CustomOrderPaymentRepository customOrderPaymentRepository;
    private final CustomOrderStatusHistoryRepository customOrderStatusHistoryRepository;
    private final ProductRepository productRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final UserRepository userRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final CustomDeliveryRecordRepository customDeliveryRecordRepository;
    private final AuditLogService auditLogService;

    public CustomOrderService(
            CustomOrderRepository customOrderRepository,
            CustomDemoRepository customDemoRepository,
            CustomOrderPaymentRepository customOrderPaymentRepository,
            CustomOrderStatusHistoryRepository customOrderStatusHistoryRepository,
            ProductRepository productRepository,
            InventoryItemRepository inventoryItemRepository,
            InventoryTransactionRepository inventoryTransactionRepository,
            UserRepository userRepository,
            CustomerAddressRepository customerAddressRepository,
            CustomDeliveryRecordRepository customDeliveryRecordRepository,
            AuditLogService auditLogService
    ) {
        this.customOrderRepository = customOrderRepository;
        this.customDemoRepository = customDemoRepository;
        this.customOrderPaymentRepository = customOrderPaymentRepository;
        this.customOrderStatusHistoryRepository = customOrderStatusHistoryRepository;
        this.productRepository = productRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.inventoryTransactionRepository = inventoryTransactionRepository;
        this.userRepository = userRepository;
        this.customerAddressRepository = customerAddressRepository;
        this.customDeliveryRecordRepository = customDeliveryRecordRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public CreateCustomOrderResponse createOrder(Long customerUserId, CreateCustomOrderRequest request) {
        UserEntity customer = getUserOrThrow(customerUserId);
        CustomerAddressEntity shippingAddress = resolveShippingAddress(customer, request);

        ProductEntity frame = productRepository.findById(request.selectedFrameProductId())
                .orElseThrow(() -> new NotFoundException("PRODUCT_NOT_FOUND", "Selected frame product not found."));
        validateFrameProduct(frame);

        InventoryItemEntity inventory = inventoryItemRepository.findWithLockByProductId(frame.getId())
                .orElseThrow(() -> new BusinessException("INSUFFICIENT_INVENTORY", "Selected frame is out of stock."));
        if (inventory.getQuantityOnHand() < 1) {
            throw new BusinessException("INSUFFICIENT_INVENTORY", "Selected frame is out of stock.");
        }

        BigDecimal totalAmount = frame.getPrice().setScale(2, RoundingMode.HALF_UP);
        BigDecimal depositAmount = totalAmount.multiply(DEFAULT_DEPOSIT_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal remainingAmount = totalAmount.subtract(depositAmount);
        String aggregatePaymentStatus = remainingAmount.compareTo(BigDecimal.ZERO) > 0
                ? PAYMENT_STATUS_PARTIALLY_PAID
                : PAYMENT_STATUS_PAID;

        CustomOrderEntity order = new CustomOrderEntity();
        order.setOrderCode(generateOrderCode());
        order.setCustomerUser(customer);
        order.setShippingAddress(shippingAddress);
        order.setSelectedFrameProduct(frame);
        order.setOrderStatus(CustomOrderStatus.WAITING_FLOWER_REVIEW);
        order.setPaymentStatus(aggregatePaymentStatus);
        order.setDepositAmount(depositAmount);
        order.setRemainingAmount(remainingAmount);
        order.setTotalAmount(totalAmount);
        order.setFlowerType(request.flowerType().trim());
        order.setPersonalizationContent(request.personalizationContent() == null ? null : request.personalizationContent().trim());
        order.setRequestedDeliveryDate(request.requestedDeliveryDate());
        order.setFlowerInputImageUrl(request.flowerInputImage().trim());
        order.setFlowerEvaluationStatus(FlowerEvaluationStatus.PENDING);
        order.setDemoRevisionCount(0);
        order.setExtraRevisionFeeRate(DEFAULT_EXTRA_REVISION_FEE_RATE);
        order.setOrderedAt(LocalDateTime.now());
        order.setNote(request.note() == null ? null : request.note().trim());
        CustomOrderEntity savedOrder = customOrderRepository.save(order);

        CustomOrderPaymentEntity depositPayment = new CustomOrderPaymentEntity();
        depositPayment.setCustomOrder(savedOrder);
        depositPayment.setPaymentStage(CustomPaymentStage.DEPOSIT);
        depositPayment.setPaymentMethod(request.depositPaymentMethod().trim().toUpperCase());
        depositPayment.setAmount(depositAmount);
        depositPayment.setPaymentStatus(PAYMENT_STATUS_PAID);
        depositPayment.setTransactionRef(trimToNull(request.depositTransactionRef()));
        depositPayment.setPaymentProofUrl(trimToNull(request.depositPaymentProof()));
        depositPayment.setPaidAt(LocalDateTime.now());
        depositPayment.setNote("Deposit paid on custom order creation.");
        customOrderPaymentRepository.save(depositPayment);

        inventory.setQuantityOnHand(inventory.getQuantityOnHand() - 1);
        inventoryItemRepository.save(inventory);
        saveInventoryTransaction(
                frame,
                InventoryTransactionType.RESERVE,
                -1,
                savedOrder.getId(),
                customer,
                "Reserve frame for custom order " + savedOrder.getOrderCode()
        );

        saveStatusHistory(
                savedOrder,
                CustomOrderStatus.WAITING_FLOWER_REVIEW,
                CustomOrderStatus.WAITING_FLOWER_REVIEW,
                customer,
                "Order created and deposit paid."
        );

        log.info(
                "Custom order created: actorUserId={}, orderId={}, orderCode={}, frameProductId={}",
                customerUserId,
                savedOrder.getId(),
                savedOrder.getOrderCode(),
                frame.getId()
        );

        return new CreateCustomOrderResponse(
                savedOrder.getId(),
                savedOrder.getOrderCode(),
                PAYMENT_STATUS_PAID,
                savedOrder.getPaymentStatus()
        );
    }

    @Transactional(readOnly = true)
    public Page<CustomOrderResponse> getMyOrders(Long customerUserId, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Specification<CustomOrderEntity> spec = (root, query, cb) -> cb.equal(root.get("customerUser").get("id"), customerUserId);
        return customOrderRepository.findAll(spec, pageable).map(this::toOrderResponse);
    }

    @Transactional(readOnly = true)
    public Page<CustomOrderResponse> getAdminOrders(
            String keyword,
            CustomOrderStatus orderStatus,
            String paymentStatus,
            int page,
            int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        Specification<CustomOrderEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword.trim().toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("orderCode")), like),
                        cb.like(cb.lower(root.get("customerUser").get("fullName")), like)
                ));
            }
            if (orderStatus != null) {
                predicates.add(cb.equal(root.get("orderStatus"), orderStatus));
            }
            if (paymentStatus != null && !paymentStatus.isBlank()) {
                predicates.add(cb.equal(cb.upper(root.get("paymentStatus")), paymentStatus.trim().toUpperCase()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return customOrderRepository.findAll(spec, pageable).map(this::toOrderResponse);
    }

    @Transactional(readOnly = true)
    public CustomOrderResponse getOrderDetail(Long orderId, Long actorUserId, RoleCode actorRole) {
        CustomOrderEntity order = getOrderForActor(orderId, actorUserId, actorRole);
        return toOrderResponse(order);
    }

    @Transactional
    public CustomOrderStatusResponse updateOrderStatus(
            Long orderId,
            UpdateCustomOrderStatusRequest request,
            Long actorUserId
    ) {
        UserEntity actor = getUserOrThrow(actorUserId);
        CustomOrderEntity order = customOrderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("ORDER_NOT_FOUND", "Custom order not found."));

        CustomOrderStatus currentStatus = order.getOrderStatus();
        CustomOrderStatus nextStatus = request.status();
        validateStatusTransition(currentStatus, nextStatus);

        applyTerminalTimestamp(order, nextStatus);
        order.setOrderStatus(nextStatus);
        CustomOrderEntity savedOrder = customOrderRepository.save(order);
        saveStatusHistory(savedOrder, currentStatus, nextStatus, actor, trimToNull(request.reason()));
        auditLogService.logAction(
                actorUserId,
                "CUSTOM_ORDER_STATUS_UPDATED",
                "CUSTOM_ORDER",
                savedOrder.getId(),
                "from=" + currentStatus + ",to=" + nextStatus
        );

        log.info(
                "Custom order status updated: actorUserId={}, orderId={}, fromStatus={}, toStatus={}",
                actorUserId,
                orderId,
                currentStatus,
                nextStatus
        );

        return new CustomOrderStatusResponse(savedOrder.getId(), savedOrder.getOrderCode(), savedOrder.getOrderStatus());
    }

    @Transactional
    public EvaluateFlowerInputResponse evaluateFlowerInput(
            Long orderId,
            EvaluateFlowerInputRequest request,
            Long actorUserId
    ) {
        if (request.evaluationStatus() == FlowerEvaluationStatus.PENDING) {
            throw new BusinessException("INVALID_EVALUATION_STATUS", "evaluationStatus must be PASS or FAIL.");
        }

        UserEntity actor = getUserOrThrow(actorUserId);
        CustomOrderEntity order = customOrderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("ORDER_NOT_FOUND", "Custom order not found."));

        if (order.getOrderStatus() != CustomOrderStatus.WAITING_FLOWER_REVIEW
                && order.getOrderStatus() != CustomOrderStatus.DEPOSITED) {
            throw new BusinessException("INVALID_ORDER_STATUS", "Order is not ready for flower evaluation.");
        }

        CustomOrderStatus fromStatus = order.getOrderStatus();
        CustomOrderStatus nextStatus;
        order.setFlowerEvaluationStatus(request.evaluationStatus());
        order.setFlowerEvaluationNote(trimToNull(request.evaluationNote()));

        if (request.evaluationStatus() == FlowerEvaluationStatus.PASS) {
            nextStatus = CustomOrderStatus.IN_PROGRESS;
        } else {
            nextStatus = CustomOrderStatus.CANCELED;
            order.setCanceledAt(LocalDateTime.now());
            restoreReservedFrame(order, actor);
        }

        order.setOrderStatus(nextStatus);
        customOrderRepository.save(order);
        saveStatusHistory(order, fromStatus, nextStatus, actor, "Flower input evaluated.");
        auditLogService.logAction(
                actorUserId,
                "CUSTOM_ORDER_FLOWER_EVALUATED",
                "CUSTOM_ORDER",
                order.getId(),
                "evaluationStatus=" + request.evaluationStatus() + ",nextStatus=" + nextStatus
        );

        log.info(
                "Flower input evaluated: actorUserId={}, orderId={}, evaluationStatus={}, nextStatus={}",
                actorUserId,
                orderId,
                request.evaluationStatus(),
                nextStatus
        );

        return new EvaluateFlowerInputResponse(order.getId(), order.getFlowerEvaluationStatus(), order.getOrderStatus());
    }

    @Transactional
    public CustomDemoResponse uploadDemo(Long orderId, CreateCustomDemoRequest request, Long actorUserId) {
        UserEntity actor = getUserOrThrow(actorUserId);
        CustomOrderEntity order = customOrderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("ORDER_NOT_FOUND", "Custom order not found."));

        if (order.getOrderStatus() == CustomOrderStatus.CANCELED || order.getOrderStatus() == CustomOrderStatus.COMPLETED) {
            throw new BusinessException("INVALID_ORDER_STATUS", "Cannot upload demo for this order status.");
        }
        if (order.getFlowerEvaluationStatus() != FlowerEvaluationStatus.PASS) {
            throw new BusinessException("FLOWER_NOT_PASSED", "Flower input must pass evaluation before demo upload.");
        }

        int nextVersion = customDemoRepository.findTopByCustomOrderIdOrderByVersionNoDesc(orderId)
                .map(latest -> latest.getVersionNo() + 1)
                .orElse(1);

        CustomDemoEntity demo = new CustomDemoEntity();
        demo.setCustomOrder(order);
        demo.setVersionNo(nextVersion);
        demo.setDemoImageUrl(request.demoImage().trim());
        demo.setDemoDescription(trimToNull(request.demoDescription()));
        demo.setCustomerResponseStatus(DemoResponseStatus.PENDING);
        demo.setUploadedBy(actor);
        demo.setUploadedAt(LocalDateTime.now());
        CustomDemoEntity savedDemo = customDemoRepository.save(demo);

        CustomOrderStatus fromStatus = order.getOrderStatus();
        order.setOrderStatus(CustomOrderStatus.WAITING_DEMO_FEEDBACK);
        customOrderRepository.save(order);
        saveStatusHistory(order, fromStatus, CustomOrderStatus.WAITING_DEMO_FEEDBACK, actor, "Demo uploaded.");
        auditLogService.logAction(
                actorUserId,
                "CUSTOM_DEMO_UPLOADED",
                "CUSTOM_ORDER",
                order.getId(),
                "demoId=" + savedDemo.getId() + ",version=" + savedDemo.getVersionNo()
        );

        log.info(
                "Custom demo uploaded: actorUserId={}, orderId={}, demoId={}, versionNo={}",
                actorUserId,
                orderId,
                savedDemo.getId(),
                savedDemo.getVersionNo()
        );

        return toDemoResponse(savedDemo);
    }

    @Transactional(readOnly = true)
    public List<CustomDemoResponse> getOrderDemos(Long orderId, Long actorUserId, RoleCode actorRole) {
        getOrderForActor(orderId, actorUserId, actorRole);
        return customDemoRepository.findByCustomOrderIdOrderByVersionNoAsc(orderId)
                .stream()
                .map(this::toDemoResponse)
                .toList();
    }

    @Transactional
    public DemoFeedbackResponse feedbackDemo(
            Long orderId,
            Long demoId,
            DemoFeedbackRequest request,
            Long customerUserId
    ) {
        CustomOrderEntity order = customOrderRepository.findByIdAndCustomerUserId(orderId, customerUserId)
                .orElseThrow(() -> new NotFoundException("ORDER_NOT_FOUND", "Custom order not found."));
        UserEntity customer = getUserOrThrow(customerUserId);

        if (order.getOrderStatus() != CustomOrderStatus.WAITING_DEMO_FEEDBACK) {
            throw new BusinessException("INVALID_ORDER_STATUS", "Order is not waiting for demo feedback.");
        }

        CustomDemoEntity demo = customDemoRepository.findByIdAndCustomOrderId(demoId, orderId)
                .orElseThrow(() -> new NotFoundException("DEMO_NOT_FOUND", "Demo not found."));
        if (demo.getCustomerResponseStatus() != DemoResponseStatus.PENDING) {
            throw new BusinessException("DEMO_ALREADY_RESPONDED", "Demo feedback was already submitted.");
        }

        demo.setCustomerFeedback(trimToNull(request.feedback()));
        demo.setRespondedAt(LocalDateTime.now());

        CustomOrderStatus fromStatus = order.getOrderStatus();
        if (request.action() == DemoFeedbackAction.APPROVE) {
            demo.setCustomerResponseStatus(DemoResponseStatus.APPROVE);
            order.setOrderStatus(CustomOrderStatus.WAITING_REMAINING_PAYMENT);
        } else {
            demo.setCustomerResponseStatus(DemoResponseStatus.REQUEST_REVISION);
            int newRevisionCount = order.getDemoRevisionCount() + 1;
            order.setDemoRevisionCount(newRevisionCount);
            if (newRevisionCount > FREE_REVISION_LIMIT) {
                applyExtraRevisionFee(order);
            }
            order.setOrderStatus(CustomOrderStatus.IN_PROGRESS);
        }

        customDemoRepository.save(demo);
        customOrderRepository.save(order);
        saveStatusHistory(order, fromStatus, order.getOrderStatus(), customer, "Customer feedback submitted.");
        auditLogService.logAction(
                customerUserId,
                "CUSTOM_DEMO_FEEDBACK_SUBMITTED",
                "CUSTOM_ORDER",
                order.getId(),
                "demoId=" + demo.getId() + ",action=" + request.action()
        );

        log.info(
                "Custom demo feedback submitted: actorUserId={}, orderId={}, demoId={}, action={}, revisionCount={}",
                customerUserId,
                orderId,
                demoId,
                request.action(),
                order.getDemoRevisionCount()
        );

        return new DemoFeedbackResponse(order.getOrderStatus(), order.getDemoRevisionCount());
    }

    @Transactional
    public RemainingPaymentResponse payRemaining(Long orderId, CreateRemainingPaymentRequest request, Long customerUserId) {
        CustomOrderEntity order = customOrderRepository.findByIdAndCustomerUserId(orderId, customerUserId)
                .orElseThrow(() -> new NotFoundException("ORDER_NOT_FOUND", "Custom order not found."));
        UserEntity customer = getUserOrThrow(customerUserId);

        if (order.getOrderStatus() != CustomOrderStatus.WAITING_REMAINING_PAYMENT) {
            throw new BusinessException("INVALID_ORDER_STATUS", "Order is not waiting remaining payment.");
        }
        if (order.getRemainingAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("REMAINING_AMOUNT_INVALID", "Order has no remaining amount.");
        }
        if (customOrderPaymentRepository.existsByCustomOrderIdAndPaymentStage(orderId, CustomPaymentStage.REMAINING)) {
            throw new BusinessException("REMAINING_ALREADY_PAID", "Remaining payment already exists.");
        }

        BigDecimal paidAmount = order.getRemainingAmount().setScale(2, RoundingMode.HALF_UP);

        CustomOrderPaymentEntity payment = new CustomOrderPaymentEntity();
        payment.setCustomOrder(order);
        payment.setPaymentStage(CustomPaymentStage.REMAINING);
        payment.setPaymentMethod(request.paymentMethod().trim().toUpperCase());
        payment.setAmount(paidAmount);
        payment.setPaymentStatus(PAYMENT_STATUS_PAID);
        payment.setTransactionRef(trimToNull(request.transactionRef()));
        payment.setPaymentProofUrl(trimToNull(request.paymentProof()));
        payment.setPaidAt(LocalDateTime.now());
        payment.setNote("Remaining payment completed.");
        customOrderPaymentRepository.save(payment);

        CustomOrderStatus fromStatus = order.getOrderStatus();
        order.setPaymentStatus(PAYMENT_STATUS_PAID);
        order.setRemainingAmount(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        order.setOrderStatus(CustomOrderStatus.IN_PROGRESS);
        customOrderRepository.save(order);
        saveStatusHistory(order, fromStatus, order.getOrderStatus(), customer, "Remaining payment completed.");
        auditLogService.logAction(
                customerUserId,
                "CUSTOM_ORDER_REMAINING_PAYMENT_COMPLETED",
                "CUSTOM_ORDER",
                order.getId(),
                "amount=" + paidAmount
        );

        log.info(
                "Custom remaining payment created: actorUserId={}, orderId={}, amount={}",
                customerUserId,
                orderId,
                paidAmount
        );

        return new RemainingPaymentResponse(order.getPaymentStatus(), order.getRemainingAmount());
    }

    @Transactional
    public CustomDeliveryResponse updateDelivery(Long orderId, UpdateCustomDeliveryRequest request, Long actorUserId) {
        CustomOrderEntity order = customOrderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("ORDER_NOT_FOUND", "Custom order not found."));
        if (order.getOrderStatus() == CustomOrderStatus.CANCELED) {
            throw new BusinessException("ORDER_CANCELED", "Cannot update delivery for canceled order.");
        }

        LocalDateTime effectiveTime = request.deliveryTime() == null ? LocalDateTime.now() : request.deliveryTime();
        CustomDeliveryRecordEntity delivery = new CustomDeliveryRecordEntity();
        delivery.setCustomOrder(order);
        delivery.setDeliveryType(request.deliveryType());
        String normalizedStatus = request.deliveryStatus().trim().toUpperCase();
        delivery.setDeliveryStatus(normalizedStatus);
        delivery.setReceiverNote(trimToNull(request.deliveryNote()));

        if (request.deliveryType() == DeliveryType.PICKUP_INPUT) {
            delivery.setPickupTime(effectiveTime);
        } else {
            if ("SHIPPED".equals(normalizedStatus)) {
                delivery.setShippedTime(effectiveTime);
            }
            if ("DELIVERED".equals(normalizedStatus)) {
                delivery.setDeliveredTime(effectiveTime);
            }
        }

        CustomDeliveryRecordEntity saved = customDeliveryRecordRepository.save(delivery);
        auditLogService.logAction(
                actorUserId,
                "CUSTOM_ORDER_DELIVERY_UPDATED",
                "CUSTOM_ORDER",
                order.getId(),
                "deliveryType=" + saved.getDeliveryType() + ",deliveryStatus=" + saved.getDeliveryStatus()
        );
        log.info(
                "Custom delivery updated: actorUserId={}, orderId={}, deliveryType={}, deliveryStatus={}",
                actorUserId,
                orderId,
                request.deliveryType(),
                normalizedStatus
        );

        return new CustomDeliveryResponse(
                order.getId(),
                order.getOrderCode(),
                saved.getDeliveryType(),
                saved.getDeliveryStatus(),
                saved.getReceiverNote(),
                saved.getPickupTime(),
                saved.getShippedTime(),
                saved.getDeliveredTime()
        );
    }

    public PageMeta toPageMeta(Page<?> pageData) {
        return PageMeta.builder()
                .page(pageData.getNumber())
                .limit(pageData.getSize())
                .totalItems(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .build();
    }

    private CustomerAddressEntity resolveShippingAddress(UserEntity customer, CreateCustomOrderRequest request) {
        if (request.shippingAddressId() != null && request.shippingAddressObject() != null) {
            throw new BusinessException(
                    "SHIPPING_ADDRESS_AMBIGUOUS",
                    "Provide shippingAddressId or shippingAddressObject, not both."
            );
        }

        if (request.shippingAddressId() != null) {
            return customerAddressRepository.findByIdAndCustomerUserId(request.shippingAddressId(), customer.getId())
                    .orElseThrow(() -> new NotFoundException("ADDRESS_NOT_FOUND", "Shipping address not found."));
        }

        if (request.shippingAddressObject() == null) {
            throw new BusinessException(
                    "SHIPPING_ADDRESS_REQUIRED",
                    "shippingAddressId or shippingAddressObject is required."
            );
        }

        CustomerAddressEntity address = new CustomerAddressEntity();
        address.setCustomerUser(customer);
        address.setReceiverName(request.shippingAddressObject().receiverName().trim());
        address.setReceiverPhone(request.shippingAddressObject().receiverPhone().trim());
        address.setAddressLine(request.shippingAddressObject().addressLine().trim());
        address.setWard(request.shippingAddressObject().ward().trim());
        address.setDistrict(request.shippingAddressObject().district().trim());
        address.setProvince(request.shippingAddressObject().province().trim());
        address.setIsDefault(request.shippingAddressObject().isDefault());
        address.setNote(trimToNull(request.shippingAddressObject().note()));

        if (Boolean.TRUE.equals(address.getIsDefault())) {
            customerAddressRepository.resetDefaultByCustomerUserId(customer.getId());
        }
        return customerAddressRepository.save(address);
    }

    private CustomOrderEntity getOrderForActor(Long orderId, Long actorUserId, RoleCode actorRole) {
        if (actorRole == RoleCode.CUSTOMER) {
            return customOrderRepository.findByIdAndCustomerUserId(orderId, actorUserId)
                    .orElseThrow(() -> new NotFoundException("ORDER_NOT_FOUND", "Custom order not found."));
        }
        return customOrderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("ORDER_NOT_FOUND", "Custom order not found."));
    }

    private CustomOrderResponse toOrderResponse(CustomOrderEntity order) {
        return new CustomOrderResponse(
                order.getId(),
                order.getOrderCode(),
                order.getSelectedFrameProduct().getId(),
                order.getSelectedFrameProduct().getName(),
                order.getOrderStatus(),
                order.getPaymentStatus(),
                order.getDepositAmount(),
                order.getRemainingAmount(),
                order.getTotalAmount(),
                order.getFlowerType(),
                order.getPersonalizationContent(),
                order.getRequestedDeliveryDate(),
                order.getFlowerInputImageUrl(),
                order.getFlowerEvaluationStatus(),
                order.getFlowerEvaluationNote(),
                order.getDemoRevisionCount(),
                order.getExtraRevisionFeeRate(),
                order.getOrderedAt()
        );
    }

    private CustomDemoResponse toDemoResponse(CustomDemoEntity demo) {
        return new CustomDemoResponse(
                demo.getId(),
                demo.getCustomOrder().getId(),
                demo.getVersionNo(),
                demo.getDemoImageUrl(),
                demo.getDemoDescription(),
                demo.getCustomerResponseStatus(),
                demo.getCustomerFeedback(),
                demo.getUploadedAt(),
                demo.getRespondedAt()
        );
    }

    private void validateFrameProduct(ProductEntity product) {
        if (product.getProductKind() != ProductKind.FRAME_OPTION) {
            throw new BusinessException("INVALID_PRODUCT_KIND", "Custom order accepts frame_option product only.");
        }
        if (!Boolean.TRUE.equals(product.getIsCustomSelectable())) {
            throw new BusinessException("PRODUCT_NOT_CUSTOM_SELECTABLE", "Product is not custom selectable.");
        }
        if (!"ACTIVE".equalsIgnoreCase(product.getStatus())) {
            throw new BusinessException("PRODUCT_INACTIVE", "Selected frame is inactive.");
        }
    }

    private void validateStatusTransition(CustomOrderStatus currentStatus, CustomOrderStatus nextStatus) {
        if (!ALLOWED_TRANSITIONS.getOrDefault(currentStatus, EnumSet.noneOf(CustomOrderStatus.class)).contains(nextStatus)) {
            throw new BusinessException("INVALID_ORDER_STATUS_TRANSITION", "Invalid custom order status transition.");
        }
    }

    private void applyTerminalTimestamp(CustomOrderEntity order, CustomOrderStatus nextStatus) {
        if (nextStatus == CustomOrderStatus.COMPLETED) {
            order.setCompletedAt(LocalDateTime.now());
        }
        if (nextStatus == CustomOrderStatus.CANCELED) {
            order.setCanceledAt(LocalDateTime.now());
        }
    }

    private void applyExtraRevisionFee(CustomOrderEntity order) {
        BigDecimal rate = order.getExtraRevisionFeeRate() == null
                ? DEFAULT_EXTRA_REVISION_FEE_RATE
                : order.getExtraRevisionFeeRate();
        BigDecimal extraFee = order.getTotalAmount().multiply(rate).setScale(2, RoundingMode.HALF_UP);
        order.setTotalAmount(order.getTotalAmount().add(extraFee));
        order.setRemainingAmount(order.getRemainingAmount().add(extraFee));
    }

    private void restoreReservedFrame(CustomOrderEntity order, UserEntity actor) {
        ProductEntity frame = order.getSelectedFrameProduct();
        InventoryItemEntity inventory = inventoryItemRepository.findWithLockByProductId(frame.getId())
                .orElseThrow(() -> new BusinessException("INVENTORY_NOT_FOUND", "Frame inventory not found."));
        inventory.setQuantityOnHand(inventory.getQuantityOnHand() + 1);
        inventoryItemRepository.save(inventory);
        saveInventoryTransaction(
                frame,
                InventoryTransactionType.ADJUST,
                1,
                order.getId(),
                actor,
                "Restore reserved frame because custom order was canceled after flower evaluation fail."
        );
    }

    private void saveInventoryTransaction(
            ProductEntity product,
            InventoryTransactionType type,
            Integer quantityChange,
            Long orderId,
            UserEntity actor,
            String note
    ) {
        InventoryTransactionEntity inventoryTransaction = new InventoryTransactionEntity();
        inventoryTransaction.setProduct(product);
        inventoryTransaction.setTransactionType(type);
        inventoryTransaction.setQuantityChange(quantityChange);
        inventoryTransaction.setReferenceType("CUSTOM_ORDER");
        inventoryTransaction.setReferenceId(orderId);
        inventoryTransaction.setCreatedBy(actor);
        inventoryTransaction.setNote(note);
        inventoryTransactionRepository.save(inventoryTransaction);
    }

    private void saveStatusHistory(
            CustomOrderEntity order,
            CustomOrderStatus fromStatus,
            CustomOrderStatus toStatus,
            UserEntity actor,
            String reason
    ) {
        CustomOrderStatusHistoryEntity history = new CustomOrderStatusHistoryEntity();
        history.setCustomOrder(order);
        history.setFromStatus(fromStatus);
        history.setToStatus(toStatus);
        history.setChangedBy(actor);
        history.setChangedAt(LocalDateTime.now());
        history.setReason(reason);
        customOrderStatusHistoryRepository.save(history);
    }

    private UserEntity getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND", "User not found."));
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String generateOrderCode() {
        String timestamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        int random = ThreadLocalRandom.current().nextInt(100, 999);
        return "CO-" + timestamp + "-" + random;
    }
}
