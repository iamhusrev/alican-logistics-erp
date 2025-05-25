package com.iamhusrev.alican.controller;


import com.iamhusrev.alican.dto.UserLoginResponse;
import com.iamhusrev.alican.dto.UserRequest;
import com.iamhusrev.alican.entity.UserEntity;
import com.iamhusrev.alican.service.AuthenticationService;
import com.iamhusrev.alican.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserEntity> register(@RequestBody @Valid UserRequest request) {
        UserEntity registeredUser = authenticationService.signup(request);

        return ResponseEntity.ok(registeredUser);
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