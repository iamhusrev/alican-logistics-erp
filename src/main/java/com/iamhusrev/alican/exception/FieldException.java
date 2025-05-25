package com.iamhusrev.alican.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.validation.FieldError;

import lombok.Getter;

@Getter
public class FieldException extends RuntimeException {
    private final Map<String, String> fieldErrors;

    public FieldException(List<FieldError> fieldErrors) {
        this.fieldErrors = new HashMap<>();
        fieldErrors.forEach(error -> this.fieldErrors.put(error.getField(), error.getDefaultMessage()));
    }
} 