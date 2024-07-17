package com.example.authorizationserver;

import com.example.authorizationserver.dto.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class InitDB implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(InitDB.class);
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) {
        log.info("Initializing database...");
        User user = User.builder()
                .username("ruibin")
                .password(passwordEncoder.encode("123"))
                .email("ruibin.zhang021@icloud.com")
                .phoneNumber("0123456789")
                .roles(Set.of(Role.ROLE_USER, Role.ROLE_ADMIN))
                .build();
        repository.save(user);
        log.info("Response: {} ", user);
    }
}




