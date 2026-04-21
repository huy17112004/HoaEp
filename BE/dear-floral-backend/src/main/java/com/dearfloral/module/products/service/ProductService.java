package com.dearfloral.module.products.service;

import com.dearfloral.common.api.PageMeta;
import com.dearfloral.common.enums.ProductKind;
import com.dearfloral.common.exception.BusinessException;
import com.dearfloral.common.exception.NotFoundException;
import com.dearfloral.module.auth.entity.UserEntity;
import com.dearfloral.module.auth.repository.UserRepository;
import com.dearfloral.module.categories.entity.ProductCategoryEntity;
import com.dearfloral.module.categories.repository.ProductCategoryRepository;
import com.dearfloral.module.products.dto.ProductResponse;
import com.dearfloral.module.products.dto.ProductUpsertRequest;
import com.dearfloral.module.products.entity.ProductEntity;
import com.dearfloral.module.products.repository.ProductRepository;
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
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final UserRepository userRepository;
    private final LocalFileStorageService localFileStorageService;

    public ProductService(
            ProductRepository productRepository,
            ProductCategoryRepository productCategoryRepository,
            UserRepository userRepository,
            LocalFileStorageService localFileStorageService
    ) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.userRepository = userRepository;
        this.localFileStorageService = localFileStorageService;
    }

    public Page<ProductResponse> getPublicProducts(
            String keyword,
            Long categoryId,
            ProductKind productKind,
            Boolean isSellableDirectly,
            int page,
            int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        Specification<ProductEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(cb.upper(root.get("status")), "ACTIVE"));
            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword.trim().toLowerCase(Locale.ROOT) + "%";
                predicates.add(cb.like(cb.lower(root.get("name")), like));
            }
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }
            if (productKind != null) {
                predicates.add(cb.equal(root.get("productKind"), productKind));
            }
            if (isSellableDirectly != null) {
                predicates.add(cb.equal(root.get("isSellableDirectly"), isSellableDirectly));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return productRepository.findAll(spec, pageable).map(this::toResponse);
    }

    public Page<ProductResponse> getAdminProducts(
            String keyword,
            Long categoryId,
            ProductKind productKind,
            Boolean isSellableDirectly,
            Boolean isCustomSelectable,
            int page,
            int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit);
        Specification<ProductEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (keyword != null && !keyword.isBlank()) {
                String like = "%" + keyword.trim().toLowerCase(Locale.ROOT) + "%";
                predicates.add(cb.like(cb.lower(root.get("name")), like));
            }
            if (categoryId != null) {
                predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            }
            if (productKind != null) {
                predicates.add(cb.equal(root.get("productKind"), productKind));
            }
            if (isSellableDirectly != null) {
                predicates.add(cb.equal(root.get("isSellableDirectly"), isSellableDirectly));
            }
            if (isCustomSelectable != null) {
                predicates.add(cb.equal(root.get("isCustomSelectable"), isCustomSelectable));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return productRepository.findAll(spec, pageable).map(this::toResponse);
    }

    public ProductResponse getPublicProductDetail(Long productId) {
        ProductEntity entity = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("PRODUCT_NOT_FOUND", "Product not found."));
        if (!"ACTIVE".equalsIgnoreCase(entity.getStatus())) {
            throw new NotFoundException("PRODUCT_NOT_FOUND", "Product not found.");
        }
        return toResponse(entity);
    }

    @Transactional
    public ProductResponse createProduct(ProductUpsertRequest request, Long actorUserId) {
        ProductCategoryEntity category = productCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("CATEGORY_NOT_FOUND", "Category not found."));

        String slug = resolveSlug(request.getSlug(), request.getName());
        if (productRepository.existsBySlug(slug)) {
            throw new BusinessException("PRODUCT_SLUG_EXISTS", "Product slug already exists.");
        }

        ProductEntity product = new ProductEntity();
        applyCommonProductData(product, request, category, actorUserId);
        product.setSlug(slug);
        return toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse updateProduct(Long productId, ProductUpsertRequest request, Long actorUserId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("PRODUCT_NOT_FOUND", "Product not found."));
        ProductCategoryEntity category = productCategoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("CATEGORY_NOT_FOUND", "Category not found."));

        String slug = resolveSlug(request.getSlug(), request.getName());
        if (productRepository.existsBySlugAndIdNot(slug, productId)) {
            throw new BusinessException("PRODUCT_SLUG_EXISTS", "Product slug already exists.");
        }

        applyCommonProductData(product, request, category, actorUserId);
        product.setSlug(slug);
        return toResponse(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("PRODUCT_NOT_FOUND", "Product not found."));
        product.setStatus("INACTIVE");
        productRepository.save(product);
    }

    public PageMeta toPageMeta(Page<?> pageData) {
        return PageMeta.builder()
                .page(pageData.getNumber())
                .limit(pageData.getSize())
                .totalItems(pageData.getTotalElements())
                .totalPages(pageData.getTotalPages())
                .build();
    }

    private void applyCommonProductData(
            ProductEntity product,
            ProductUpsertRequest request,
            ProductCategoryEntity category,
            Long actorUserId
    ) {
        validateProductKindRules(request.getProductKind(), request.getIsSellableDirectly(), request.getIsCustomSelectable());

        product.setCategory(category);
        product.setName(request.getName().trim());
        product.setDescription(request.getDescription() == null ? null : request.getDescription().trim());
        product.setPrice(request.getPrice());
        product.setProductKind(request.getProductKind());
        product.setIsSellableDirectly(request.getIsSellableDirectly());
        product.setIsCustomSelectable(request.getIsCustomSelectable());

        String storedImage = localFileStorageService.saveProductImage(request.getImageFile());
        if (storedImage != null) {
            product.setImageUrl(storedImage);
        } else if (request.getImageUrl() != null && !request.getImageUrl().isBlank()) {
            product.setImageUrl(request.getImageUrl().trim());
        }

        product.setSize(request.getSize() == null ? null : request.getSize().trim());
        product.setMaterial(request.getMaterial() == null ? null : request.getMaterial().trim());
        product.setFlowerType(request.getFlowerType() == null ? null : request.getFlowerType().trim());
        product.setStatus(request.getStatus().trim().toUpperCase(Locale.ROOT));

        if (actorUserId != null) {
            UserEntity actor = userRepository.findById(actorUserId)
                    .orElseThrow(() -> new NotFoundException("USER_NOT_FOUND", "User not found."));
            product.setCreatedBy(actor);
        }
    }

    private void validateProductKindRules(ProductKind kind, Boolean isSellableDirectly, Boolean isCustomSelectable) {
        if (kind == ProductKind.FRAME_OPTION) {
            if (!Boolean.FALSE.equals(isSellableDirectly) || !Boolean.TRUE.equals(isCustomSelectable)) {
                throw new BusinessException(
                        "INVALID_FRAME_OPTION_FLAGS",
                        "frame_option must have isSellableDirectly=false and isCustomSelectable=true."
                );
            }
        }
        if (kind == ProductKind.STANDARD_PRODUCT) {
            if (!Boolean.TRUE.equals(isSellableDirectly)) {
                throw new BusinessException(
                        "INVALID_STANDARD_PRODUCT_FLAGS",
                        "standard_product must have isSellableDirectly=true."
                );
            }
        }
    }

    private String resolveSlug(String slugInput, String name) {
        if (slugInput != null && !slugInput.isBlank()) {
            return normalizeSlug(slugInput);
        }
        return normalizeSlug(name);
    }

    private String normalizeSlug(String value) {
        return value.trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-{2,}", "-");
    }

    private ProductResponse toResponse(ProductEntity entity) {
        return new ProductResponse(
                entity.getId(),
                entity.getCategory().getId(),
                entity.getCategory().getName(),
                entity.getName(),
                entity.getSlug(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getProductKind(),
                entity.getIsSellableDirectly(),
                entity.getIsCustomSelectable(),
                entity.getImageUrl(),
                entity.getSize(),
                entity.getMaterial(),
                entity.getFlowerType(),
                entity.getStatus()
        );
    }
}
