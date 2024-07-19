package com.example.authorizationserver;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

import static com.example.authorizationserver.ApiConfig.Endpoint;


@SpringBootTest
@Testcontainers
public class ApiConfigTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");


    private final ApiConfig apiConfig;

    public ApiConfigTest(@Autowired ApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    @Test
    void contextLoads() {
    }




    @Test
    void shouldLoadConfigFile(){
        Map<String, Endpoint> endpoints = apiConfig.getEndpoints();
        System.out.println(endpoints);
        assertThat(endpoints).isNotNull();
        assertThat(endpoints).isNotEmpty();
    }


}
