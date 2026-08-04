package com.TaskFlow.TF.Controllers;

import com.TaskFlow.TF.DTOs.UserRequests;
import com.TaskFlow.TF.DTOs.UserResponse;
import com.TaskFlow.TF.Security.JwtUtil;
import com.TaskFlow.TF.Services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager; // NEW: The "Bouncer"

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new account with username and password. Passwords are BCrypt hashed.")
    @ApiResponse(responseCode = "409", description = "Conflict (username already exists)")
    public UserResponse register(@Valid @RequestBody UserRequests request) {
        // This just calls the service we already updated!
        return userService.registerUser(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Logs in a user and returns a JWT token for subsequent API calls.")
    @ApiResponse(responseCode = "401", description = "Invalid username or password")
    public String login(@RequestBody UserRequests request) {
        // 1. PRINT WHAT SPRING ACTUALLY RECEIVED
        System.out.println("Login attempt - Username: '" + request.getUsername() + "'");
        System.out.println("Login attempt - Password: '" + request.getPassword() + "'");

        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

            // 2. PRINT BEFORE AUTHENTICATION
            System.out.println("Attempting to authenticate...");

            Authentication authentication = authenticationManager.authenticate(authToken);

            String  token = jwtUtil.generateToken(request.getUsername());
            return token;

        } catch (AuthenticationException e) {
            // 4. PRINT THE EXACT SPRING SECURITY EXCEPTION
            System.err.println("AUTHENTICATION FAILED: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            throw new RuntimeException("Invalid username or password");
        }
    }
}