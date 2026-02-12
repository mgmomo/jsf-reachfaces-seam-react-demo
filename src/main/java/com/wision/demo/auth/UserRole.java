package com.wision.demo.auth;

public enum UserRole {

    ADMIN, USER, GUEST;

    public static UserRole fromHeader(String value) {
        if (value == null || value.trim().isEmpty()) {
            return GUEST;
        }
        try {
            return valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return GUEST;
        }
    }

    public boolean isAdmin() {
        return this == ADMIN;
    }

    public boolean isUser() {
        return this == USER;
    }

    public boolean isGuest() {
        return this == GUEST;
    }

    public boolean isAtLeast(UserRole required) {
        return this.ordinal() <= required.ordinal();
    }
}
