package com.iamhusrev.alican.util;

import java.util.Locale;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.iamhusrev.alican.entity.UserEntity;

public class AuthUtil {
    
    private AuthUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static Optional<UserEntity> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserEntity) {
            return Optional.of((UserEntity) authentication.getPrincipal());
        }
        return Optional.empty();
    }

    public static Long getCurrentUserId() {
        return getCurrentUser()
            .map(UserEntity::getId)
            .orElse(null);
    }

    public static String getCurrentUserEmail() {
        return getCurrentUser()
            .map(UserEntity::getEmail)
            .orElse(null);
    }

    public static String getCurrentUserFullName() {
        return getCurrentUser()
            .map(UserEntity::getFullName)
            .orElse(null);
    }

    public static String getCurrentUserLocaleString() {
        return getCurrentUser()
            .map(UserEntity::getLocale)
            .orElse("tr");
    }

    public static Locale getCurrentUserLocale() {
        String str = getCurrentUser()
            .map(UserEntity::getLocale)
            .orElse("tr");

        return Locale.of(str);
    }

    public static boolean isAuthenticated() {
        return getCurrentUser().isPresent();
    }

    public static boolean isAdmin() {
        return getCurrentUser()
            .map(user -> user.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("SYSTEM_ADMIN") || 
                                auth.getAuthority().equals("COMPANY_ADMIN")))
            .orElse(false);
    }
} 