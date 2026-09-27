package com.hospital.management.service;

import com.hospital.management.dto.auth.AuthResponse;
import com.hospital.management.dto.auth.LoginRequest;
import com.hospital.management.dto.auth.RegisterRequest;
import com.hospital.management.entity.User;
import com.hospital.management.repository.UserRepository;
import com.hospital.management.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    public AuthService(AuthenticationManager authenticationManager,
                        UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        JwtUtil jwtUtil,
                        org.springframework.security.core.userdetails.UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    public User register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already taken");
        }
        User user = new User(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail(),
                request.getRole()
        );
        return userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(userDetails);
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        return new AuthResponse(token, userDetails.getUsername(), role);
    }
}
