package com.dearfloral;

import com.dearfloral.config.storage.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableConfigurationProperties(FileStorageProperties.class)
public class DearFloralBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DearFloralBackendApplication.class, args);
    }
}
