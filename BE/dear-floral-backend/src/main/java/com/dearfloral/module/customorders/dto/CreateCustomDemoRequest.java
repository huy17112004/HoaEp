package com.dearfloral.module.customorders.dto;

import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateCustomDemoRequest {
    @Size(max = 500, message = "demoImage must be at most 500 characters.")
    private String demoImage;

    private MultipartFile demoImageFile;
    private List<MultipartFile> demoImageFiles;

    private List<@Size(max = 500, message = "Each demo image must be at most 500 characters.") String> demoImages;

    @Size(max = 1000, message = "demoDescription must be at most 1000 characters.")
    private String demoDescription;
}
