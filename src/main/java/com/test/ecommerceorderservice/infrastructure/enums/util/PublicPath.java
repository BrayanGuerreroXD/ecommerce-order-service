package com.test.ecommerceorderservice.infrastructure.enums.util;

public enum PublicPath {
    ERROR("/error"),
    LOGIN("/login"),
    AUTH("/auth"),
    DOCS("/docs"),
    SWAGGER("/swagger-ui/"),
    TEST("/test"),
    VALID_ACCESS("/valid-access");

    private final String value;

    PublicPath(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean containValue(String findValue) {
        for (PublicPath path : values()) {
            if (findValue.contains(path.value)) {
                return true;
            }
        }
        return false;
    }

}