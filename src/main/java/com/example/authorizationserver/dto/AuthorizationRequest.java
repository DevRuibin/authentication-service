package com.example.authorizationserver.dto;

public record AuthorizationRequest(String name, String token, String path) {
}
