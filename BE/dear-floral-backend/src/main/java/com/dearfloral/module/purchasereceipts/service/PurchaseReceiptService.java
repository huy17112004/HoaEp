package com.dearfloral.module.purchasereceipts.service;

import com.dearfloral.common.api.PageMeta;
import com.dearfloral.common.enums.InventoryTransactionType;
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
import java.util.List;
import java.util.Locale;
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
}
