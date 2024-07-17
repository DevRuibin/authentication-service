package com.example.authorizationserver.dto;

import com.example.authorizationserver.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Set;


@Builder
@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private Set<Role> roles;
}
