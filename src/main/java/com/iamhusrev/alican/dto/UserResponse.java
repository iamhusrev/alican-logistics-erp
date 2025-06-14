package com.iamhusrev.alican.dto;

import com.iamhusrev.alican.enums.UserRole;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String email;
    private String fullName;
    private Locale locale;
    private UserRole userRole;
}