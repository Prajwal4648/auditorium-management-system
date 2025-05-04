package com.example.auditoriumbooking.service;

import com.example.auditoriumbooking.model.User;
import com.example.auditoriumbooking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Custom exception for user service errors
    public static class UserServiceException extends RuntimeException {
        public UserServiceException(String message) {
            super(message);
        }
    }

    public User saveUser(User user, Set<String> roles) throws UserServiceException {
        // Validate input
        if (user == null) {
            throw new UserServiceException("User cannot be null");
        }
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new UserServiceException("Username cannot be empty");
        }
        if (user.getUsername().length() < 4 || user.getUsername().length() > 50) {
            throw new UserServiceException("Username must be between 4 and 50 characters");
        }
        if (!Pattern.matches("^[a-zA-Z0-9_]+$", user.getUsername())) {
            throw new UserServiceException("Username can only contain letters, numbers, and underscores");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new UserServiceException("Password cannot be empty");
        }
        if (user.getPassword().length() < 8) {
            throw new UserServiceException("Password must be at least 8 characters");
        }

        // Check for duplicate username
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserServiceException("Username already exists");
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Assign roles
        Set<String> userRoles = (roles == null || roles.isEmpty()) ? new HashSet<>() : new HashSet<>(roles);
        if (userRoles.isEmpty()) {
            userRoles.add("ROLE_USER");
        }
        user.setRoles(userRoles);

        // Save user
        return userRepository.save(user);
    }

    // Overloaded method for default ROLE_USER
    public User saveUser(User user) throws UserServiceException {
        return saveUser(user, null);
    }

    public Optional<User> findByUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return Optional.empty();
        }
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return userRepository.findById(id);
    }
}