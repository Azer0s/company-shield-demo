package com.companyshield.demo.misc;

import com.companyshield.demo.domain.UserEntity;
import com.companyshield.demo.domain.UserRole;
import com.companyshield.demo.persistence.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
@Log
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final Environment environment;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // only create the admin user if the mode is not production
        if (Arrays.asList(environment.getActiveProfiles()).contains("production")) {
            return;
        }

        var adminUser = UserEntity.builder()
                .username("admin")
                .password("admin")
                .roles(List.of(UserRole.ADMIN))
                .balance(BigDecimal.ZERO)
                .disabled(false)
                .build();

        if (!userRepository.create(adminUser)) {
            System.exit(-1);
        }

        log.info("Admin user created successfully");
    }
}