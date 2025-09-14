package com.test.ecommerceorderservice.infrastructure.cache.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    @Primary
    public CacheManager fallbackCacheManager() {
        return new ConcurrentMapCacheManager();
    }
}