package com.parking.config;

import com.parking.entity.Role;
import com.parking.entity.User;
import com.parking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
@RequiredArgsConstructor
@Slf4j
public class DefaultAdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed-default-users:false}")
    private boolean seedDefaultUsers;

    @Override
    public void run(String... args) {
        if (!seedDefaultUsers) {
            return;
        }
        if (!userRepository.existsByUsername("admin")) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .build();
            userRepository.save(admin);
            log.info("Default admin user created (username: admin, password: admin123)");
        }

        if (!userRepository.existsByUsername("attendant")) {
            User attendant = User.builder()
                    .username("attendant")
                    .password(passwordEncoder.encode("attendant123"))
                    .role(Role.ATTENDANT)
                    .build();
            userRepository.save(attendant);
            log.info("Default attendant user created (username: attendant, password: attendant123)");
        }
    }
}
