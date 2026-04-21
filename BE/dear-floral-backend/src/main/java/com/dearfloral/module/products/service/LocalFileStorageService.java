package com.dearfloral.module.products.service;

import com.dearfloral.common.exception.BusinessException;
import com.dearfloral.config.storage.FileStorageProperties;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalFileStorageService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "webp");

    private final FileStorageProperties fileStorageProperties;

    public LocalFileStorageService(FileStorageProperties fileStorageProperties) {
        this.fileStorageProperties = fileStorageProperties;
    }

    public String saveProductImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String originalFilename = file.getOriginalFilename();
        String extension = extractExtension(originalFilename);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new BusinessException("INVALID_IMAGE_FORMAT", "Image format is not supported.");
        }

        String fileName = "product-" + UUID.randomUUID() + "." + extension;
        Path uploadDir = Path.of(fileStorageProperties.uploadDir(), "products");
        Path target = uploadDir.resolve(fileName);
        try {
            Files.createDirectories(uploadDir);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new BusinessException("FILE_STORAGE_ERROR", "Cannot store product image.");
        }
        return "products/" + fileName;
    }

    private String extractExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            throw new BusinessException("INVALID_IMAGE_FORMAT", "Image format is not supported.");
        }
        return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
    }
}
