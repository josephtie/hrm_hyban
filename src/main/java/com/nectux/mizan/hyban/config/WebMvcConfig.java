package com.nectux.mizan.hyban.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${app.upload-dir:uploads}")
    private String uploadDir;

    @Value("${app.logo-dir:src/main/resources/static/logo}")
    private String logoDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String logoLocation = Paths.get(logoDir).toAbsolutePath().toString();
        registry.addResourceHandler("/static/logo/**")
                .addResourceLocations("file:" + logoLocation + "/", "classpath:/static/logo/");

        String uploadsLocation = Paths.get(uploadDir).toAbsolutePath().toString();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadsLocation + "/");
    }
}
