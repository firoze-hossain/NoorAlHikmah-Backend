package com.roze.hikmah;

import com.roze.hikmah.auth.Role;
import com.roze.hikmah.auth.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class NoorAlHikmahBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoorAlHikmahBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName("USER").isEmpty()) {
                roleRepository.save(
                        Role.builder().name("USER").build()
                );
            }
            if (roleRepository.findByName("ADMIN").isEmpty()) {
                roleRepository.save(
                        Role.builder().name("ADMIN").build()
                );
            }
            if (roleRepository.findByName("SCHOLAR").isEmpty()) {
                roleRepository.save(
                        Role.builder().name("SCHOLAR").build()
                );
            }
        };
    }
}
