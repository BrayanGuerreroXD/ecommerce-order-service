package com.test.ecommerceorderservice.application.port.out;

public interface CachePort {
    void put(String key, Object value);
    Object get(String key);
    void evict(String key);
}