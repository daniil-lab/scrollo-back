package com.inst.base.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
@EnableWebMvc
public class ResourceConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**", "/resources", "/resources/*")
                .addResourceLocations("/uploads/")
                .addResourceLocations("/uploads/**")
                .addResourceLocations("file:/uploads/**")
                .addResourceLocations("file:/uploads/")
                .addResourceLocations("file:/uploads")
                .addResourceLocations("file:/uploads/*")
                .addResourceLocations("/uploads")
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }
}
