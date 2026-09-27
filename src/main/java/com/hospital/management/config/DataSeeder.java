package com.hospital.management.config;

import com.hospital.management.entity.Role;
import com.hospital.management.entity.User;
import com.hospital.management.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds a default ADMIN account on first startup so there's always
 * a way to log in and create further users (doctors/staff).
 *
 * Default credentials: admin / admin123
 * CHANGE THE PASSWORD after first login in any real deployment.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User("admin", passwordEncoder.encode("admin123"), "admin@hospital.com", Role.ADMIN);
            userRepository.save(admin);
            System.out.println(">>> Seeded default admin user: username=admin password=admin123");
        }
    }
}
