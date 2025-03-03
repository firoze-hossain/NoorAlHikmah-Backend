package com.roze.hikmah;

import com.roze.hikmah.auth.Role;
import com.roze.hikmah.auth.RoleRepository;
import com.roze.hikmah.auth.User;
import com.roze.hikmah.auth.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class NoorAlHikmahBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoorAlHikmahBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Create roles if they don't exist
            Role userRole = roleRepository.findByName("USER")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("USER").build()));

            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("ADMIN").build()));

            Role scholarRole = roleRepository.findByName("SCHOLAR")
                    .orElseGet(() -> roleRepository.save(Role.builder().name("SCHOLAR").build()));

            // Create default admin if not exists
            if (userRepository.findByEmail("admin@hikmah.com").isEmpty()) {
                User admin = User.builder()
                        .email("admin@hikmah.com")
                        .password(passwordEncoder.encode("admin@123")) // Securely hash password
                        .roles(List.of(adminRole))
                        .enabled(true)
                        .build();
                userRepository.save(admin);
            }

            // Create default scholar if not exists
            if (userRepository.findByEmail("scholar@hikmah.com").isEmpty()) {
                User scholar = User.builder()
                        .email("scholar@hikmah.com")
                        .password(passwordEncoder.encode("scholar@123")) // Securely hash password
                        .roles(List.of(scholarRole))
                        .enabled(true)
                        .build();
                userRepository.save(scholar);
            }
        };
    }
}
