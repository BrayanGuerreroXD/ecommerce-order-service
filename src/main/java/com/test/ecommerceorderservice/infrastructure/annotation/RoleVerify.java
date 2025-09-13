package com.test.ecommerceorderservice.infrastructure.annotation;

import com.test.ecommerceorderservice.infrastructure.enums.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleVerify {
    Role[] value() default {};
}