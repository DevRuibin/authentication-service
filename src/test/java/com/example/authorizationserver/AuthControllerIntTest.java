package com.example.authorizationserver;

import com.example.authorizationserver.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Testcontainers
public class AuthControllerIntTest {
    private final TestRestTemplate restTemplate;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");


    @Autowired
    public AuthControllerIntTest(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Test
    void testShouldLoginWhenValidUsernameAndPassword(){
        String username = "ruibin";
        String password = "123";
        LoginRequest loginRequest = new LoginRequest(username, password);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/auth/login", loginRequest, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void testShouldLoginWhenValidEmailAndPassword(){
        String username = "ruibin.zhang021@icloud.com";
        String password = "123";
        LoginRequest loginRequest = new LoginRequest(username, password);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/auth/login", loginRequest, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void testShouldLoginWhenValidPhoneAndPassword(){
        String username = "0123456789";
        String password = "123";
        LoginRequest loginRequest = new LoginRequest(username, password);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/auth/login", loginRequest, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void testShouldNotLoginWhenInvalidIdentifierAndPassword(){
        String username = "0123";
        String password = "123";
        LoginRequest loginRequest = new LoginRequest(username, password);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/auth/login", loginRequest, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @Rollback
    void testShouldRegisterUserWhenValidUsernameAndPassword(){
        String username = "ruibin2";
        String password = "123";
        RegisterRequest registerRequest = new RegisterRequest(username, password, "", "");
        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/auth/register", registerRequest, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();

        LoginRequest loginRequest = new LoginRequest(username, password);
        ResponseEntity<String> response2 = restTemplate.postForEntity("/api/v1/auth/login", loginRequest, String.class);
        assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response2.getBody()).isNotEmpty();
    }

    @Test
    void testShouldHaveAccessRightWhenValidTokenAndRole(){
        String username = "0123456789";
        String password = "123";
        LoginRequest loginRequest = new LoginRequest(username, password);
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity("/api/v1/auth/login", loginRequest, LoginResponse.class);
        String token = Objects.requireNonNull(response.getBody()).token();
        System.out.println(token);

        String endpoint = "/api/v1/users";
        String endpointName = "_api_v1_users";
        AuthorizationRequest authorizationRequest = new AuthorizationRequest(endpointName, token, endpoint);
        ResponseEntity<AuthorizationResponse> authorizationResponse = restTemplate.postForEntity("/api/v1/auth/authorize", authorizationRequest, AuthorizationResponse.class);
        assertThat(authorizationResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(authorizationResponse.getBody()).isNotNull();
        assertThat(authorizationResponse.getBody().authorized()).isTrue();

//        _api_v1_documents
    }


    @Test
    void testShouldHaveNoAccessRightWhenInvalidTokenAndRole(){
        String username = "0123456789";
        String password = "123";
        LoginRequest loginRequest = new LoginRequest(username, password);
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity("/api/v1/auth/login", loginRequest, LoginResponse.class);
        String token = Objects.requireNonNull(response.getBody()).token();
        System.out.println(token);

        String endpoint = "/api/v1/documents";
        String endpointName = "_api_v1_documents";
        AuthorizationRequest authorizationRequest = new AuthorizationRequest(endpointName, token, endpoint);
        ResponseEntity<AuthorizationResponse> authorizationResponse = restTemplate.postForEntity("/api/v1/auth/authorize", authorizationRequest, AuthorizationResponse.class);
        assertThat(authorizationResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(authorizationResponse.getBody()).isNotNull();
        assertThat(authorizationResponse.getBody().authorized()).isFalse();

    }



}
