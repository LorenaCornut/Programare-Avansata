package org.example.controller;

import org.example.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public static class LoginRequest {
        private String username;
        private String password;

        // Getters and setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        logger.info("Received login request with username: {} and password: {}", username, password);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            logger.info("Authentication successful for username: {}", username);
            logger.info("Authentication object: {}", authentication);
            logger.info("Principal: {}", authentication.getPrincipal());
            String token = jwtUtil.generateToken(((User) authentication.getPrincipal()).getUsername());
            logger.debug("Generated JWT token: {}", token);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return response;
        } catch (Exception e) {
            logger.error("Authentication failed for username: {}", username, e);
            throw e;
        }
    }
}
