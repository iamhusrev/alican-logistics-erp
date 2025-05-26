package com.iamhusrev.alican.enums;

import lombok.Getter;

@Getter
public enum UserRole {

    SYSTEM_ADMIN(0),
    COMPANY_ADMIN(1),
    COMPANY_MANAGER(2),
    COMPANY_USER(3),
    COMPANY_GUEST(4),
    UNAUTHORIZED(-1);

    private final int value;

    UserRole(int value) {
        this.value = value;
    }

    public static UserRole fromValue(Integer value) {
        if (value== null || value < 0 || value > 3) {
            return UNAUTHORIZED;
        }
        return switch (value) {
            case 0 -> SYSTEM_ADMIN;
            case 1 -> COMPANY_ADMIN;
            case 2 -> COMPANY_MANAGER;
            case 3 -> COMPANY_USER;
            default -> null;
        };
    }
}