package com.test.ecommerceorderservice.infrastructure.config;

import com.test.ecommerceorderservice.infrastructure.interceptor.GenericInterceptor;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final GenericInterceptor genericInterceptor;

    @Override
    public void addInterceptors(@NotNull InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(genericInterceptor).addPathPatterns("/**");
    }
}