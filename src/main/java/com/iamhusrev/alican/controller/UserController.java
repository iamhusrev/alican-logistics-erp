package com.iamhusrev.alican.controller;

import com.iamhusrev.alican.entity.UserEntity;
import com.iamhusrev.alican.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;


    @GetMapping("/me")
    public ResponseEntity<UserEntity> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserEntity currentUser = (UserEntity) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }

    @GetMapping()
    public ResponseEntity<List<UserEntity>> allUsers() {
        List<UserEntity> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }
}