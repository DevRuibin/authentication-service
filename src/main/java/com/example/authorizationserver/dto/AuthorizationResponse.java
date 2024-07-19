package com.example.authorizationserver.dto;

import lombok.Builder;

@Builder
public record AuthorizationResponse(UserResponse user, boolean authorized) {
}
