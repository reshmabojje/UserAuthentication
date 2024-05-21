package com.controller;

import com.model.User;
import com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        logger.info("Attempting to register user: {}", user.getUsername());

        if (userService.findByUsername(user.getUsername()) != null) {
            logger.warn("Username is already taken: {}", user.getUsername());
            return ResponseEntity.badRequest().body("Username is already taken!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userService.saveUser(user);

        logger.info("User registered successfully: {}", user.getUsername());
        return ResponseEntity.ok(savedUser);
    }

   /* @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        logger.info("Attempting to log in user: {}", user.getUsername());

        User foundUser = userService.findByUsername(user.getUsername());

        if (foundUser == null) {
            logger.warn("User not found: {}", user.getUsername());
            return ResponseEntity.badRequest().body("Invalid username or password!");
        }

        logger.info("Found user: {}", foundUser.getUsername());
        logger.debug("Provided password: {}", user.getPassword());
        logger.debug("Encoded password in DB: {}", foundUser.getPassword());

        if (passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            logger.info("User logged in successfully: {}", user.getUsername());
            return ResponseEntity.ok("Login successful!");
        } else {
            logger.warn("Invalid password for user: {}", user.getUsername());
            return ResponseEntity.badRequest().body("Invalid username or password!");
        }
    }*/
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        logger.info("Attempting to log in user: {}", user.getUsername());

        // Find the user by username
        User foundUser = userService.findByUsername(user.getUsername());

        // Check if the user exists
        if (foundUser == null) {
            logger.warn("User not found: {}", user.getUsername());
            return ResponseEntity.badRequest().body("Invalid username or password!");
        }

        // Check if the provided password matches the stored password
        if (passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
            logger.info("User logged in successfully: {}", user.getUsername());
            return ResponseEntity.ok("Login successful!");
        } else {
            logger.warn("Invalid password for user: {}", user.getUsername());
            return ResponseEntity.badRequest().body("Invalid username or password!");
        }
    }

}
