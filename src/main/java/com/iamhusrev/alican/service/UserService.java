package com.iamhusrev.alican.service;

import com.iamhusrev.alican.entity.UserEntity;
import com.iamhusrev.alican.repositroy.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserEntity> allUsers() {

        return new ArrayList<>(userRepository.findAll());
    }
}