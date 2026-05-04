package com.dearfloral.module.inventory.service;

import com.dearfloral.common.api.PageMeta;
import com.dearfloral.common.enums.ProductKind;
import com.dearfloral.module.inventory.dto.InventoryItemResponse;
import com.dearfloral.module.inventory.entity.InventoryItemEntity;
import com.dearfloral.module.inventory.repository.InventoryItemRepository;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

    private final InventoryItemRepository inventoryItemRepository;

    public InventoryService(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    @Transactional(readOnly = true)
    public Page<InventoryItemResponse> getInventoryItems(
            ProductKind productKind,
            Boolean isSellableDirectly,
            Boolean isCustomSelectable,
            String keyword,
            int page,
            int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        Specification<InventoryItemEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (productKind != null) {
                predicates.add(cb.equal(root.get("product").get("productKind"), productKind));
            }
            if (isSellableDirectly != null) {
                predicates.add(cb.equal(root.get("product").get("isSellableDirectly"), isSellableDirectly));
            }
            if (isCustomSelectable != null) {
                predicates.add(cb.equal(root.get("product").get("isCustomSelectable"), isCustomSelectable));
            }
            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword.trim().toLowerCase(Locale.ROOT) + "%";
                predicates.add(cb.like(cb.lower(root.get("product").get("name")), like));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return inventoryItemRepository.findAll(spec, pageable).map(this::toResponse);
    }

    public PageMeta toPageMeta(Page<?> pageData) {
        return PageMeta.builder()
                .page(pageData.getNumber())
                .limit(pageData.getSize())
                .totalItems(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .build();
    }

    private InventoryItemResponse toResponse(InventoryItemEntity item) {
        return new InventoryItemResponse(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getProduct().getProductKind(),
                item.getProduct().getIsSellableDirectly(),
                item.getProduct().getIsCustomSelectable(),
                item.getQuantityOnHand()
        );
    }
}
