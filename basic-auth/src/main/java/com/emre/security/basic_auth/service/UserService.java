package com.emre.security.basic_auth.service;


import com.emre.security.basic_auth.dto.CreateUserRequest;
import com.emre.security.basic_auth.model.User;
import com.emre.security.basic_auth.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> getByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public User createUser(CreateUserRequest request){
        User user =User.builder()
                .name(request.name())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .authorities(request.authorities())
                .accountNonExpired(true)
                .isEnabled(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .build();
        return userRepository.save(user);
    }
}