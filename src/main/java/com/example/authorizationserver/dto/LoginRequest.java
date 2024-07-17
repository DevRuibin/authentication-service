package com.example.authorizationserver.dto;

public record LoginRequest (String usernameOrEmailOrPhone, String password){
}
