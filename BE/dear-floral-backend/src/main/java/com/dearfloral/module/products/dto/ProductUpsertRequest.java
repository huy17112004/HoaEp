package com.dearfloral.module.products.dto;

import com.dearfloral.common.enums.ProductKind;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProductUpsertRequest {

    @NotBlank(message = "name is required.")
    @Size(max = 200, message = "name must be at most 200 characters.")
    private String name;

    @Size(max = 255, message = "slug must be at most 255 characters.")
    private String slug;

    @Size(max = 2000, message = "description must be at most 2000 characters.")
    private String description;

    @NotNull(message = "price is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "price must be greater than 0.")
    private BigDecimal price;

    @NotNull(message = "categoryId is required.")
    private Long categoryId;

    @NotNull(message = "productKind is required.")
    private ProductKind productKind;

    @NotNull(message = "isSellableDirectly is required.")
    private Boolean isSellableDirectly;

    @NotNull(message = "isCustomSelectable is required.")
    private Boolean isCustomSelectable;

    @Size(max = 500, message = "imageUrl must be at most 500 characters.")
    private String imageUrl;

    private MultipartFile imageFile;

    @Size(max = 100, message = "size must be at most 100 characters.")
    private String size;

    @Size(max = 100, message = "material must be at most 100 characters.")
    private String material;

    @Size(max = 100, message = "flowerType must be at most 100 characters.")
    private String flowerType;

    @NotBlank(message = "status is required.")
    @Size(max = 50, message = "status must be at most 50 characters.")
    private String status;
}
