package com.iamhusrev.alican.controller;

import com.iamhusrev.alican.dto.UserLoginResponse;
import com.iamhusrev.alican.dto.UserRequest;
import com.iamhusrev.alican.entity.UserEntity;
import com.iamhusrev.alican.service.AuthenticationService;
import com.iamhusrev.alican.service.JwtService;
import com.iamhusrev.alican.util.AuthUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<UserEntity> register(@RequestBody @Valid UserRequest request) {
        UserEntity registeredUser = authenticationService.signup(request);
        return ResponseEntity.ok(registeredUser);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SYSTEM_ADMIN') or hasRole('COMPANY_ADMIN') or authentication.principal.id == #id")
    public ResponseEntity<UserEntity> updateUser(
        @PathVariable("id") Integer id,
        @RequestBody @Valid UserRequest request
    ) {
        if (!AuthUtil.isAdmin() && !id.equals(AuthUtil.getCurrentUserId())) {
            return ResponseEntity.status(403).build();
        }

        UserEntity updatedUser = authenticationService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> authenticate(@RequestBody UserRequest request) {
        UserEntity authenticatedUser = authenticationService.authenticate(request);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        UserLoginResponse loginResponse = new UserLoginResponse();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
}