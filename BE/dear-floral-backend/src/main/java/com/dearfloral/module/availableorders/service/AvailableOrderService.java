package com.dearfloral.module.availableorders.service;

import com.dearfloral.common.api.PageMeta;
import com.dearfloral.common.enums.AvailableOrderStatus;
import com.dearfloral.common.enums.InventoryTransactionType;
import com.dearfloral.common.enums.ProductKind;
import com.dearfloral.common.enums.RoleCode;
import com.dearfloral.common.exception.BusinessException;
import com.dearfloral.common.exception.NotFoundException;
import com.dearfloral.module.auth.entity.UserEntity;
import com.dearfloral.module.auth.repository.UserRepository;
import com.dearfloral.module.availableorders.dto.AvailableOrderItemResponse;
import com.dearfloral.module.availableorders.dto.AvailableOrderResponse;
import com.dearfloral.module.availableorders.dto.AvailableOrderStatusResponse;
import com.dearfloral.module.availableorders.dto.CreateAvailableOrderItemRequest;
import com.dearfloral.module.availableorders.dto.CreateAvailableOrderRequest;
import com.dearfloral.module.availableorders.dto.UpdateAvailableOrderStatusRequest;
import com.dearfloral.module.availableorders.entity.AvailableOrderEntity;
import com.dearfloral.module.availableorders.entity.AvailableOrderItemEntity;
import com.dearfloral.module.availableorders.entity.AvailableOrderPaymentEntity;
import com.dearfloral.module.availableorders.entity.AvailableOrderStatusHistoryEntity;
import com.dearfloral.module.availableorders.repository.AvailableOrderItemRepository;
import com.dearfloral.module.availableorders.repository.AvailableOrderPaymentRepository;
import com.dearfloral.module.availableorders.repository.AvailableOrderRepository;
import com.dearfloral.module.availableorders.repository.AvailableOrderStatusHistoryRepository;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AvailableOrderService {

    private static final String PAID_STATUS = "PAID";
    private static final Map<AvailableOrderStatus, EnumSet<AvailableOrderStatus>> ALLOWED_TRANSITIONS = Map.of(
            AvailableOrderStatus.RECEIVED, EnumSet.of(AvailableOrderStatus.PROCESSING, AvailableOrderStatus.CANCELED),
            AvailableOrderStatus.PROCESSING, EnumSet.of(AvailableOrderStatus.SHIPPING, AvailableOrderStatus.CANCELED),
            AvailableOrderStatus.SHIPPING, EnumSet.of(AvailableOrderStatus.COMPLETED),
            AvailableOrderStatus.COMPLETED, EnumSet.noneOf(AvailableOrderStatus.class),
            AvailableOrderStatus.CANCELED, EnumSet.noneOf(AvailableOrderStatus.class)
    );

    private final AvailableOrderRepository availableOrderRepository;
    private final AvailableOrderItemRepository availableOrderItemRepository;
    private final AvailableOrderPaymentRepository availableOrderPaymentRepository;
    private final AvailableOrderStatusHistoryRepository availableOrderStatusHistoryRepository;
    private final ProductRepository productRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final UserRepository userRepository;
    private final CustomerAddressRepository customerAddressRepository;
    private final AuditLogService auditLogService;

    public AvailableOrderService(
            AvailableOrderRepository availableOrderRepository,
            AvailableOrderItemRepository availableOrderItemRepository,
            AvailableOrderPaymentRepository availableOrderPaymentRepository,
            AvailableOrderStatusHistoryRepository availableOrderStatusHistoryRepository,
            ProductRepository productRepository,
            InventoryItemRepository inventoryItemRepository,
            InventoryTransactionRepository inventoryTransactionRepository,
            UserRepository userRepository,
            CustomerAddressRepository customerAddressRepository,
            AuditLogService auditLogService
    ) {
        this.availableOrderRepository = availableOrderRepository;
        this.availableOrderItemRepository = availableOrderItemRepository;
        this.availableOrderPaymentRepository = availableOrderPaymentRepository;
        this.availableOrderStatusHistoryRepository = availableOrderStatusHistoryRepository;
        this.productRepository = productRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.inventoryTransactionRepository = inventoryTransactionRepository;
        this.userRepository = userRepository;
        this.customerAddressRepository = customerAddressRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public AvailableOrderResponse createOrder(Long customerUserId, CreateAvailableOrderRequest request) {
        UserEntity customer = getUserOrThrow(customerUserId);
        CustomerAddressEntity shippingAddress = customerAddressRepository
                .findByIdAndCustomerUserId(request.shippingAddressId(), customerUserId)
                .orElseThrow(() -> new NotFoundException("ADDRESS_NOT_FOUND", "Shipping address not found."));

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<PendingOrderItem> pendingItems = new ArrayList<>();

        for (CreateAvailableOrderItemRequest itemRequest : request.items()) {
            ProductEntity product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new NotFoundException("PRODUCT_NOT_FOUND", "Product not found."));
            validateOrderableProduct(product);

            InventoryItemEntity inventory = inventoryItemRepository.findWithLockByProductId(product.getId())
                    .orElseThrow(() -> new BusinessException("INSUFFICIENT_INVENTORY", "Insufficient inventory."));
            if (inventory.getQuantityOnHand() < itemRequest.quantity()) {
                throw new BusinessException("INSUFFICIENT_INVENTORY", "Insufficient inventory.");
            }
            BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity()));
            totalAmount = totalAmount.add(subtotal);
            pendingItems.add(new PendingOrderItem(product, inventory, itemRequest.quantity(), subtotal));
        }

        AvailableOrderEntity order = new AvailableOrderEntity();
        order.setOrderCode(generateOrderCode());
        order.setCustomerUser(customer);
        order.setShippingAddress(shippingAddress);
        order.setOrderStatus(AvailableOrderStatus.RECEIVED);
        order.setPaymentStatus(PAID_STATUS);
        order.setTotalAmount(totalAmount);
        order.setOrderedAt(LocalDateTime.now());
        order.setNote(request.note() == null ? null : request.note().trim());
        AvailableOrderEntity savedOrder = availableOrderRepository.save(order);

        List<AvailableOrderItemEntity> orderItems = new ArrayList<>();
        for (PendingOrderItem pending : pendingItems) {
            pending.inventoryItem().setQuantityOnHand(pending.inventoryItem().getQuantityOnHand() - pending.quantity());
            inventoryItemRepository.save(pending.inventoryItem());

            AvailableOrderItemEntity item = new AvailableOrderItemEntity();
            item.setAvailableOrder(savedOrder);
            item.setProduct(pending.product());
            item.setQuantity(pending.quantity());
            item.setUnitPrice(pending.product().getPrice());
            item.setSubtotal(pending.subtotal());
            orderItems.add(availableOrderItemRepository.save(item));

            InventoryTransactionEntity inventoryTransaction = new InventoryTransactionEntity();
            inventoryTransaction.setProduct(pending.product());
            inventoryTransaction.setTransactionType(InventoryTransactionType.RESERVE);
            inventoryTransaction.setQuantityChange(-pending.quantity());
            inventoryTransaction.setReferenceType("AVAILABLE_ORDER");
            inventoryTransaction.setReferenceId(savedOrder.getId());
            inventoryTransaction.setCreatedBy(customer);
            inventoryTransaction.setNote("Reserve inventory for available order " + savedOrder.getOrderCode());
            inventoryTransactionRepository.save(inventoryTransaction);
        }

        AvailableOrderPaymentEntity payment = new AvailableOrderPaymentEntity();
        payment.setAvailableOrder(savedOrder);
        payment.setPaymentMethod(request.paymentMethod().trim().toUpperCase());
        payment.setAmount(totalAmount);
        payment.setPaymentStatus(PAID_STATUS);
        payment.setTransactionRef(request.transactionRef() == null ? null : request.transactionRef().trim());
        payment.setPaymentProofUrl(request.paymentProofUrl() == null ? null : request.paymentProofUrl().trim());
        payment.setPaidAt(LocalDateTime.now());
        payment.setNote("Payment captured on order creation.");
        availableOrderPaymentRepository.save(payment);

        AvailableOrderStatusHistoryEntity statusHistory = new AvailableOrderStatusHistoryEntity();
        statusHistory.setAvailableOrder(savedOrder);
        statusHistory.setFromStatus(AvailableOrderStatus.RECEIVED);
        statusHistory.setToStatus(AvailableOrderStatus.RECEIVED);
        statusHistory.setChangedBy(customer);
        statusHistory.setChangedAt(LocalDateTime.now());
        statusHistory.setReason("Order created.");
        availableOrderStatusHistoryRepository.save(statusHistory);

        return toOrderResponse(savedOrder, orderItems);
    }

    @Transactional
    public AvailableOrderStatusResponse updateOrderStatus(
            Long orderId,
            UpdateAvailableOrderStatusRequest request,
            Long actorUserId
    ) {
        AvailableOrderEntity order = availableOrderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("ORDER_NOT_FOUND", "Order not found."));
        UserEntity actor = getUserOrThrow(actorUserId);

        AvailableOrderStatus currentStatus = order.getOrderStatus();
        AvailableOrderStatus nextStatus = request.status();
        if (!ALLOWED_TRANSITIONS.getOrDefault(currentStatus, EnumSet.noneOf(AvailableOrderStatus.class)).contains(nextStatus)) {
            throw new BusinessException("INVALID_ORDER_STATUS_TRANSITION", "Invalid available order status transition.");
        }

        order.setOrderStatus(nextStatus);
        if (nextStatus == AvailableOrderStatus.COMPLETED) {
            order.setCompletedAt(LocalDateTime.now());
        }
        if (nextStatus == AvailableOrderStatus.CANCELED) {
            order.setCanceledAt(LocalDateTime.now());
        }
        AvailableOrderEntity savedOrder = availableOrderRepository.save(order);

        AvailableOrderStatusHistoryEntity statusHistory = new AvailableOrderStatusHistoryEntity();
        statusHistory.setAvailableOrder(savedOrder);
        statusHistory.setFromStatus(currentStatus);
        statusHistory.setToStatus(nextStatus);
        statusHistory.setChangedBy(actor);
        statusHistory.setChangedAt(LocalDateTime.now());
        statusHistory.setReason(request.reason() == null ? null : request.reason().trim());
        availableOrderStatusHistoryRepository.save(statusHistory);
        auditLogService.logAction(
                actorUserId,
                "AVAILABLE_ORDER_STATUS_UPDATED",
                "AVAILABLE_ORDER",
                savedOrder.getId(),
                "from=" + currentStatus + ",to=" + nextStatus
        );

        return new AvailableOrderStatusResponse(savedOrder.getId(), savedOrder.getOrderCode(), savedOrder.getOrderStatus());
    }

    public AvailableOrderResponse getOrderDetailForCustomer(Long customerUserId, Long orderId) {
        AvailableOrderEntity order = availableOrderRepository.findByIdAndCustomerUserId(orderId, customerUserId)
                .orElseThrow(() -> new NotFoundException("ORDER_NOT_FOUND", "Order not found."));
        List<AvailableOrderItemEntity> items = availableOrderItemRepository.findByAvailableOrderId(order.getId());
        return toOrderResponse(order, items);
    }

    @Transactional(readOnly = true)
    public AvailableOrderResponse getOrderDetail(Long orderId, Long actorUserId, RoleCode actorRole) {
        AvailableOrderEntity order = getOrderForActor(orderId, actorUserId, actorRole);
        List<AvailableOrderItemEntity> items = availableOrderItemRepository.findByAvailableOrderId(order.getId());
        return toOrderResponse(order, items);
    }

    @Transactional(readOnly = true)
    public Page<AvailableOrderResponse> getMyOrders(Long customerUserId, int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Specification<AvailableOrderEntity> spec = (root, query, cb) -> cb.equal(root.get("customerUser").get("id"), customerUserId);
        return availableOrderRepository.findAll(spec, pageable)
                .map(order -> toOrderResponse(order, availableOrderItemRepository.findByAvailableOrderId(order.getId())));
    }

    @Transactional(readOnly = true)
    public Page<AvailableOrderResponse> getAdminOrders(
            String keyword,
            AvailableOrderStatus orderStatus,
            String paymentStatus,
            int page,
            int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        Specification<AvailableOrderEntity> spec = (root, query, cb) -> {
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
        return availableOrderRepository.findAll(spec, pageable)
                .map(order -> toOrderResponse(order, availableOrderItemRepository.findByAvailableOrderId(order.getId())));
    }

    public PageMeta toPageMeta(Page<?> pageData) {
        return PageMeta.builder()
                .page(pageData.getNumber())
                .limit(pageData.getSize())
                .totalItems(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .build();
    }

    private UserEntity getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND", "User not found."));
    }

    private void validateOrderableProduct(ProductEntity product) {
        if (product.getProductKind() != ProductKind.STANDARD_PRODUCT) {
            throw new BusinessException("INVALID_PRODUCT_KIND", "Available order accepts standard products only.");
        }
        if (!Boolean.TRUE.equals(product.getIsSellableDirectly())) {
            throw new BusinessException("PRODUCT_NOT_SELLABLE", "Product is not sellable directly.");
        }
        if (!"ACTIVE".equalsIgnoreCase(product.getStatus())) {
            throw new BusinessException("PRODUCT_INACTIVE", "Product is inactive.");
        }
    }

    private AvailableOrderEntity getOrderForActor(Long orderId, Long actorUserId, RoleCode actorRole) {
        if (actorRole == RoleCode.CUSTOMER) {
            return availableOrderRepository.findByIdAndCustomerUserId(orderId, actorUserId)
                    .orElseThrow(() -> new NotFoundException("ORDER_NOT_FOUND", "Order not found."));
        }
        return availableOrderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("ORDER_NOT_FOUND", "Order not found."));
    }

    private AvailableOrderResponse toOrderResponse(
            AvailableOrderEntity order,
            List<AvailableOrderItemEntity> items
    ) {
        List<AvailableOrderItemResponse> itemResponses = items.stream()
                .map(item -> new AvailableOrderItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getSubtotal()
                ))
                .toList();
        return new AvailableOrderResponse(
                order.getId(),
                order.getOrderCode(),
                order.getOrderStatus(),
                order.getPaymentStatus(),
                order.getTotalAmount(),
                order.getOrderedAt(),
                itemResponses
        );
    }

    private String generateOrderCode() {
        String timestamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        int random = ThreadLocalRandom.current().nextInt(100, 999);
        return "AO-" + timestamp + "-" + random;
    }

    private record PendingOrderItem(
            ProductEntity product,
            InventoryItemEntity inventoryItem,
            Integer quantity,
            BigDecimal subtotal
    ) {
    }
}
