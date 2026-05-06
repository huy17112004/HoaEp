package com.dearfloral.module.purchasereceipts.service;

import com.dearfloral.common.api.PageMeta;
import com.dearfloral.common.enums.InventoryTransactionType;
import com.dearfloral.common.exception.BusinessException;
import com.dearfloral.common.exception.NotFoundException;
import com.dearfloral.module.auth.entity.UserEntity;
import com.dearfloral.module.auth.repository.UserRepository;
import com.dearfloral.module.inventory.entity.InventoryItemEntity;
import com.dearfloral.module.inventory.entity.InventoryTransactionEntity;
import com.dearfloral.module.inventory.repository.InventoryItemRepository;
import com.dearfloral.module.inventory.repository.InventoryTransactionRepository;
import com.dearfloral.module.products.entity.ProductEntity;
import com.dearfloral.module.products.repository.ProductRepository;
import com.dearfloral.module.reports.service.AuditLogService;
import com.dearfloral.module.purchasereceipts.dto.CreatePurchaseReceiptRequest;
import com.dearfloral.module.purchasereceipts.dto.PurchaseReceiptItemResponse;
import com.dearfloral.module.purchasereceipts.dto.PurchaseReceiptResponse;
import com.dearfloral.module.purchasereceipts.dto.UpdatePurchaseReceiptRequest;
import com.dearfloral.module.purchasereceipts.entity.PurchaseReceiptEntity;
import com.dearfloral.module.purchasereceipts.entity.PurchaseReceiptItemEntity;
import com.dearfloral.module.purchasereceipts.repository.PurchaseReceiptItemRepository;
import com.dearfloral.module.purchasereceipts.repository.PurchaseReceiptRepository;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PurchaseReceiptService {

    private final PurchaseReceiptRepository purchaseReceiptRepository;
    private final PurchaseReceiptItemRepository purchaseReceiptItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final InventoryTransactionRepository inventoryTransactionRepository;
    private final AuditLogService auditLogService;

    public PurchaseReceiptService(
            PurchaseReceiptRepository purchaseReceiptRepository,
            PurchaseReceiptItemRepository purchaseReceiptItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository,
            InventoryItemRepository inventoryItemRepository,
            InventoryTransactionRepository inventoryTransactionRepository,
            AuditLogService auditLogService
    ) {
        this.purchaseReceiptRepository = purchaseReceiptRepository;
        this.purchaseReceiptItemRepository = purchaseReceiptItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.inventoryTransactionRepository = inventoryTransactionRepository;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public PurchaseReceiptResponse createReceipt(CreatePurchaseReceiptRequest request, Long actorUserId) {
        UserEntity actor = userRepository.findById(actorUserId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND", "User not found."));

        PurchaseReceiptEntity receipt = new PurchaseReceiptEntity();
        receipt.setReceiptCode(generateReceiptCode());
        receipt.setReceiptDate(request.receiptDate());
        receipt.setCreatedBy(actor);
        receipt.setNote(request.note() == null ? null : request.note().trim());
        PurchaseReceiptEntity savedReceipt = purchaseReceiptRepository.save(receipt);

        List<PurchaseReceiptItemEntity> savedItems = new ArrayList<>();
        for (var itemRequest : request.items()) {
            ProductEntity product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new NotFoundException("PRODUCT_NOT_FOUND", "Product not found."));
            BigDecimal subtotal = itemRequest.unitCost().multiply(BigDecimal.valueOf(itemRequest.quantity()));

            PurchaseReceiptItemEntity item = new PurchaseReceiptItemEntity();
            item.setPurchaseReceipt(savedReceipt);
            item.setProduct(product);
            item.setQuantity(itemRequest.quantity());
            item.setUnitCost(itemRequest.unitCost());
            item.setSubtotal(subtotal);
            savedItems.add(purchaseReceiptItemRepository.save(item));

            InventoryItemEntity inventoryItem = inventoryItemRepository.findByProductId(product.getId())
                    .orElseGet(() -> {
                        InventoryItemEntity newItem = new InventoryItemEntity();
                        newItem.setProduct(product);
                        newItem.setQuantityOnHand(0);
                        return newItem;
                    });
            inventoryItem.setQuantityOnHand(inventoryItem.getQuantityOnHand() + itemRequest.quantity());
            inventoryItemRepository.save(inventoryItem);

            InventoryTransactionEntity transaction = new InventoryTransactionEntity();
            transaction.setProduct(product);
            transaction.setTransactionType(InventoryTransactionType.IMPORT);
            transaction.setQuantityChange(itemRequest.quantity());
            transaction.setReferenceType("PURCHASE_RECEIPT");
            transaction.setReferenceId(savedReceipt.getId());
            transaction.setNote("Import by purchase receipt " + savedReceipt.getReceiptCode());
            transaction.setCreatedBy(actor);
            inventoryTransactionRepository.save(transaction);
        }

        auditLogService.logAction(
                actorUserId,
                "PURCHASE_RECEIPT_CREATED",
                "PURCHASE_RECEIPT",
                savedReceipt.getId(),
                "items=" + request.items().size()
        );

        return toResponse(savedReceipt, savedItems);
    }

    @Transactional
    public PurchaseReceiptResponse updateReceipt(Long receiptId, UpdatePurchaseReceiptRequest request, Long actorUserId) {
        UserEntity actor = userRepository.findById(actorUserId)
                .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND", "User not found."));

        PurchaseReceiptEntity receipt = purchaseReceiptRepository.findById(receiptId)
                .orElseThrow(() -> new NotFoundException("PURCHASE_RECEIPT_NOT_FOUND", "Purchase receipt not found."));

        List<PurchaseReceiptItemEntity> existingItems = purchaseReceiptItemRepository.findByPurchaseReceiptId(receiptId);

        Map<Long, Integer> oldQtyByProductId = new HashMap<>();
        for (PurchaseReceiptItemEntity existing : existingItems) {
            oldQtyByProductId.merge(existing.getProduct().getId(), existing.getQuantity(), Integer::sum);
        }

        Map<Long, Integer> newQtyByProductId = new HashMap<>();
        List<PurchaseReceiptItemEntity> updatedItems = new ArrayList<>();
        for (var itemRequest : request.items()) {
            ProductEntity product = productRepository.findById(itemRequest.productId())
                    .orElseThrow(() -> new NotFoundException("PRODUCT_NOT_FOUND", "Product not found."));
            BigDecimal subtotal = itemRequest.unitCost().multiply(BigDecimal.valueOf(itemRequest.quantity()));

            PurchaseReceiptItemEntity item = new PurchaseReceiptItemEntity();
            item.setPurchaseReceipt(receipt);
            item.setProduct(product);
            item.setQuantity(itemRequest.quantity());
            item.setUnitCost(itemRequest.unitCost());
            item.setSubtotal(subtotal);
            updatedItems.add(item);

            newQtyByProductId.merge(product.getId(), itemRequest.quantity(), Integer::sum);
        }

        for (Map.Entry<Long, Integer> entry : oldQtyByProductId.entrySet()) {
            Long productId = entry.getKey();
            int oldQty = entry.getValue();
            int newQty = newQtyByProductId.getOrDefault(productId, 0);
            applyInventoryDelta(receipt, actor, productId, newQty - oldQty);
        }
        for (Map.Entry<Long, Integer> entry : newQtyByProductId.entrySet()) {
            Long productId = entry.getKey();
            if (oldQtyByProductId.containsKey(productId)) {
                continue;
            }
            applyInventoryDelta(receipt, actor, productId, entry.getValue());
        }

        receipt.setReceiptDate(request.receiptDate());
        receipt.setNote(request.note() == null ? null : request.note().trim());
        purchaseReceiptRepository.save(receipt);

        purchaseReceiptItemRepository.deleteAll(existingItems);
        List<PurchaseReceiptItemEntity> savedItems = purchaseReceiptItemRepository.saveAll(updatedItems);

        auditLogService.logAction(
                actorUserId,
                "PURCHASE_RECEIPT_UPDATED",
                "PURCHASE_RECEIPT",
                receipt.getId(),
                "items=" + request.items().size()
        );

        return toResponse(receipt, savedItems);
    }

    @Transactional(readOnly = true)
    public Page<PurchaseReceiptResponse> getReceipts(
            LocalDate fromDate,
            LocalDate toDate,
            String keyword,
            int page,
            int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        Specification<PurchaseReceiptEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("receiptDate"), fromDate));
            }
            if (toDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("receiptDate"), toDate));
            }
            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword.trim().toLowerCase(Locale.ROOT) + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("receiptCode")), like),
                        cb.like(cb.lower(root.get("note")), like)
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return purchaseReceiptRepository.findAll(spec, pageable).map(receipt -> {
            List<PurchaseReceiptItemEntity> items = purchaseReceiptItemRepository.findByPurchaseReceiptId(receipt.getId());
            return toResponse(receipt, items);
        });
    }

    public PageMeta toPageMeta(Page<?> pageData) {
        return PageMeta.builder()
                .page(pageData.getNumber())
                .limit(pageData.getSize())
                .totalItems(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .build();
    }

    private PurchaseReceiptResponse toResponse(
            PurchaseReceiptEntity receipt,
            List<PurchaseReceiptItemEntity> items
    ) {
        List<PurchaseReceiptItemResponse> itemResponses = items.stream()
                .map(item -> new PurchaseReceiptItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitCost(),
                        item.getSubtotal()
                ))
                .toList();
        return new PurchaseReceiptResponse(
                receipt.getId(),
                receipt.getReceiptCode(),
                receipt.getReceiptDate(),
                receipt.getNote(),
                itemResponses
        );
    }

    private String generateReceiptCode() {
        String timestamp = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
        int random = ThreadLocalRandom.current().nextInt(100, 999);
        return "PR-" + timestamp + "-" + random;
    }

    private void applyInventoryDelta(
            PurchaseReceiptEntity receipt,
            UserEntity actor,
            Long productId,
            int delta
    ) {
        if (delta == 0) {
            return;
        }

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("PRODUCT_NOT_FOUND", "Product not found."));

        InventoryItemEntity inventoryItem = inventoryItemRepository.findWithLockByProductId(productId)
                .orElseGet(() -> {
                    InventoryItemEntity newItem = new InventoryItemEntity();
                    newItem.setProduct(product);
                    newItem.setQuantityOnHand(0);
                    return newItem;
                });

        int nextQuantity = inventoryItem.getQuantityOnHand() + delta;
        if (nextQuantity < 0) {
            throw new BusinessException("INSUFFICIENT_INVENTORY", "Insufficient inventory for receipt update.");
        }
        inventoryItem.setQuantityOnHand(nextQuantity);
        inventoryItemRepository.save(inventoryItem);

        InventoryTransactionEntity transaction = new InventoryTransactionEntity();
        transaction.setProduct(product);
        transaction.setTransactionType(InventoryTransactionType.ADJUST);
        transaction.setQuantityChange(delta);
        transaction.setReferenceType("PURCHASE_RECEIPT");
        transaction.setReferenceId(receipt.getId());
        transaction.setCreatedBy(actor);
        transaction.setNote("Adjust by purchase receipt update " + receipt.getReceiptCode());
        inventoryTransactionRepository.save(transaction);
    }
}
