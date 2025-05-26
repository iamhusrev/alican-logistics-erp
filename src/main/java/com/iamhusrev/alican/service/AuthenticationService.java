package com.iamhusrev.alican.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iamhusrev.alican.dto.UserRequest;
import com.iamhusrev.alican.entity.UserEntity;
import com.iamhusrev.alican.exception.DuplicateEmailException;
import com.iamhusrev.alican.repositroy.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public UserEntity signup(UserRequest input) {
        try {
            if (userRepository.findByEmail(input.getEmail()).isPresent()) {
                throw new DuplicateEmailException("Email already exists: " + input.getEmail());
            }

            UserEntity user = new UserEntity();
            user.setFullName(input.getFullName());
            user.setLocale(input.getLocale());
            user.setEmail(input.getEmail());
            user.setPassword(passwordEncoder.encode(input.getPassword()));
            user.setRoleId(input.getRoleId());

            return userRepository.save(user);
        } catch (DuplicateEmailException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error during user registration: " + e.getMessage(), e);
        }
    }

    @Transactional
    public UserEntity updateUser(Integer userId, UserRequest input) {
        try {
            UserEntity existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

            // Check if new email is different and already exists
            if (!existingUser.getEmail().equals(input.getEmail()) && 
                userRepository.findByEmail(input.getEmail()).isPresent()) {
                throw new DuplicateEmailException("Email already exists: " + input.getEmail());
            }

            existingUser.setFullName(input.getFullName());
            existingUser.setLocale(input.getLocale());
            existingUser.setEmail(input.getEmail());
            
            // Only update password if provided
            if (input.getPassword() != null && !input.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(input.getPassword()));
            }
            
            if (input.getRoleId() != null) {
                existingUser.setRoleId(input.getRoleId());
            }

            return userRepository.save(existingUser);
        } catch (EntityNotFoundException | DuplicateEmailException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error during user update: " + e.getMessage(), e);
        }
    }

    @Transactional
    public UserEntity authenticate(UserRequest input) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    input.getEmail(),
                    input.getPassword()
                )
            );

            return userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + input.getEmail()));
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
        }
    }
}