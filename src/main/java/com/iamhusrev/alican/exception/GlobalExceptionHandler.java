package com.iamhusrev.alican.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.iamhusrev.alican.util.AuthUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = messageSource.getMessage(
                Objects.requireNonNull(error.getDefaultMessage()),
                null,
                LocaleContextHolder.getLocale()
            );
            errors.put(fieldName, errorMessage);
        });

        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", messageSource.getMessage("error.validation_failed", null, AuthUtil.getCurrentUserLocale()));
        response.put("errors", errors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(FieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleFieldException(FieldException ex) {
        Map<String, Object> response = new HashMap<>();
        Map<String, String> translatedErrors = new HashMap<>();

        ex.getFieldErrors().forEach((field, message) -> 
            translatedErrors.put(field, messageSource.getMessage(message, null, AuthUtil.getCurrentUserLocale()))
        );

        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", messageSource.getMessage("error.validation_failed", null, AuthUtil.getCurrentUserLocale()));
        response.put("errors", translatedErrors);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler({NoHandlerFoundException.class, ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, Object>> handleNoHandlerFoundException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("message", messageSource.getMessage("error.not_found", null, LocaleContextHolder.getLocale()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleSecurityException(Exception exception) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String messageKey = "error.internal_server";
        String descriptionKey = null;

        if (exception instanceof BadCredentialsException) {
            status = HttpStatus.UNAUTHORIZED;
            messageKey = "error.bad_credentials";
        } else if (exception instanceof AccountStatusException) {
            status = HttpStatus.FORBIDDEN;
            messageKey = "error.account_locked";
        } else if (exception instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN;
            messageKey = "error.forbidden";
        } else if (exception instanceof SignatureException) {
            status = HttpStatus.FORBIDDEN;
            messageKey = "error.jwt_invalid";
        } else if (exception instanceof ExpiredJwtException) {
            status = HttpStatus.FORBIDDEN;
            messageKey = "error.jwt_expired";
        }

        response.put("status", status.value());
        response.put("message", messageSource.getMessage(messageKey, null, AuthUtil.getCurrentUserLocale()));

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Map<String, Object>> handleDuplicateEmailException(DuplicateEmailException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("message", messageSource.getMessage("error.duplicate_email", null, AuthUtil.getCurrentUserLocale()));
        response.put("details", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, Object>> handleEntityNotFoundException(EntityNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("message", messageSource.getMessage("error.not_found", null, AuthUtil.getCurrentUserLocale()));
        response.put("details", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
}