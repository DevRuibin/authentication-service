package com.example.authorizationserver;

import com.example.authorizationserver.dto.AuthorizationRequest;
import com.example.authorizationserver.dto.AuthorizationResponse;
import com.example.authorizationserver.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthorizationService {
    private final ApiConfig apiConfig;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;


    public AuthorizationResponse authorize(AuthorizationRequest authorizationRequest) {
        String name = authorizationRequest.name();
        String token = authorizationRequest.token();
        Map<String, ApiConfig.Endpoint> endpoints = apiConfig.getEndpoints();
        if (endpoints.containsKey(name)) {
            ApiConfig.Endpoint endpoint = endpoints.get(name);
            String username = jwtTokenProvider.getUserNameFromJwtToken(token);
            UserDetails user = userService.loadUserByUsername(username);
            Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
            Set<? extends GrantedAuthority> roles = endpoint.getRoles();
            boolean hasAccess = authorities.stream()
                    .anyMatch(roles::contains);
            UserResponse userResponse = userService.getUserByUsername(username);
            if (hasAccess) {
                return AuthorizationResponse.builder()
                        .authorized(true).user(userResponse).build();
            }else{
                return AuthorizationResponse.builder()
                        .authorized(false).user(userResponse).build();
            }
        }
        return AuthorizationResponse.builder().authorized(false).build();
    }
}
