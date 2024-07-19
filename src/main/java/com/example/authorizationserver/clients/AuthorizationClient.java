package com.example.authorizationserver.clients;


import com.example.authorizationserver.dto.AuthorizationRequest;
import com.example.authorizationserver.dto.AuthorizationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "auth-service", url = "${application.config.auth-url}")
public interface AuthorizationClient {
    @PostMapping("/auth/authorize")
    ResponseEntity<AuthorizationResponse> authorize(@RequestBody AuthorizationRequest authorizationRequest);
}
