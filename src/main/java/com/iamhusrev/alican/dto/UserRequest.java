package com.iamhusrev.alican.dto;

import java.util.Locale;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    public interface Create {
    }

    public interface Update {
    }

    @NotBlank(message = "validation.full_name")
    private String fullName;

    @NotBlank(message = "validation.email")
    @Email(message = "validation.email")
    private String email;

    @NotBlank(message = "validation.locale")
    private String locale;

    @NotBlank(message = "validation.password")
    @Size(min = 6, message = "validation.password")
    private String password;
}