package com.hospital.management.controller;

import com.hospital.management.dto.auth.AuthResponse;
import com.hospital.management.dto.auth.LoginRequest;
import com.hospital.management.dto.auth.RegisterRequest;
import com.hospital.management.entity.User;
import com.hospital.management.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Creates a new user account (ADMIN, DOCTOR, or STAFF).
     * Restricted to ADMIN so random callers can't self-register as ADMIN/DOCTOR/STAFF.
     * Log in as the seeded default admin (see DataSeeder) to call this the first time.
     */
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
