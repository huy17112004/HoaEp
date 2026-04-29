package com.dearfloral.config.storage;

import com.dearfloral.module.products.service.LocalFileStorageService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FileStorageWebConfig implements WebMvcConfigurer {

    private final LocalFileStorageService localFileStorageService;

    public FileStorageWebConfig(LocalFileStorageService localFileStorageService) {
        this.localFileStorageService = localFileStorageService;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String uploadLocation = localFileStorageService.resolveUploadDir().toUri().toString();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadLocation.endsWith("/") ? uploadLocation : uploadLocation + "/");
    }
}
