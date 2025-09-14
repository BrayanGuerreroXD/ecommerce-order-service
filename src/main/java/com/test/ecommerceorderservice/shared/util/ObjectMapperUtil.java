package com.test.ecommerceorderservice.shared.util;

import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ObjectMapperUtil {
    public static Map<String, Object> toMap(Object obj) {
        if (obj == null) return null;

        if (obj instanceof Map) {
            Map<String, Object> result = new HashMap<>();
            ((Map<?, ?>) obj).forEach((k, v) -> result.put(String.valueOf(k), toMap(v)));
            return result;
        }

        if (isPrimitiveOrWrapper(obj.getClass()) || obj instanceof String) {
            return Map.of("value", obj);
        }

        if (obj instanceof Collection) {
            List<Object> list = new ArrayList<>();
            for (Object item : (Collection<?>) obj) {
                list.add(toMap(item));
            }
            return Map.of("list", list);
        }

        Map<String, Object> result = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                result.put(field.getName(), toMap(value));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static boolean isPrimitiveOrWrapper(Class<?> type) {
        return type.isPrimitive() ||
                type == Boolean.class ||
                type == Integer.class ||
                type == Long.class ||
                type == Double.class ||
                type == Float.class ||
                type == Short.class ||
                type == Byte.class ||
                type == Character.class;
    }
}
