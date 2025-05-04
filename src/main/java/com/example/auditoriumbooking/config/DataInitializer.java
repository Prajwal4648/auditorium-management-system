package com.example.auditoriumbooking.config;

import com.example.auditoriumbooking.model.User;
import com.example.auditoriumbooking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        // Check if admin user already exists
        if (userService.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.getRoles().add("ADMIN");
            userService.saveUser(admin);
        }
    }
}