package com.example.authorizationserver.dto;

public record RegisterRequest(String username, String password, String email, String phoneNumber) {
}
