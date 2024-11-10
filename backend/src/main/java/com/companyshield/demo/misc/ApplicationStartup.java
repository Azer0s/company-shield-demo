package com.companyshield.demo.misc;

import com.companyshield.demo.domain.UserEntity;
import com.companyshield.demo.domain.UserRole;
import com.companyshield.demo.domain.transactional.CreateUserRequest;
import com.companyshield.demo.persistence.UserRepository;
import com.companyshield.demo.service.TransactionService;
import com.companyshield.demo.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
@AllArgsConstructor
@Log
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private final UserService userService;
    private final UserRepository userRepository;
    private final TransactionService transactionService;
    private final Environment environment;

    private void createUser(String username, String password, UserRole role) {
        if (!userService.createUser(new CreateUserRequest(username, password, List.of(role)))) {
            System.exit(-1);
        }
        log.info("User " + username + " created successfully");

        if (role != UserRole.ADMIN) {
            var user = userRepository.findByUsername(username).orElseThrow();
            if (!transactionService.deposit(user.getId(), BigDecimal.valueOf(1000))) {
                System.exit(-1);
            }
            log.info("User " + username + " deposited $1000 successfully");
        }
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // only create the admin user if the mode is not production
        if (Arrays.asList(environment.getActiveProfiles()).contains("production")) {
            return;
        }

        createUser("admin", "admin", UserRole.ADMIN);

        createUser("alice", "alice", UserRole.USER);
        createUser("bob", "bob", UserRole.USER);
        createUser("jane", "jane", UserRole.USER);
        createUser("john", "john", UserRole.USER);
        createUser("joe", "joe", UserRole.USER);
    }
}