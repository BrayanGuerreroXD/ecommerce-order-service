package com.test.ecommerceorderservice.infrastructure.enums;

public enum Role {
    CUSTOMER("ROLE_CUSTOMER"),
    ADMIN("ROLE_ADMIN"),

    ;

    private final String authority;

    Role(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
}
