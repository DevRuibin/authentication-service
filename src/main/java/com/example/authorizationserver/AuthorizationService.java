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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

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
            boolean hasAccess = checkRoles(authorities, roles);
            hasAccess = hasAccess || checkOwnerRole(roles, username,authorizationRequest.path(), endpoint);
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

    private boolean checkRoles(Collection<? extends GrantedAuthority> authorities, Set<? extends GrantedAuthority> roles) {
        return authorities.stream().anyMatch(roles::contains);
    }

    private boolean checkOwnerRole(Set<? extends GrantedAuthority> roles, String tokenUsername, String path, ApiConfig.Endpoint endpoint) {
        Matcher matcher = Pattern.compile(endpoint.getPath()).matcher(path);
        if (matcher.matches()) {
            String pathUsername = matcher.group("username");
            return !roles.contains(new SimpleGrantedAuthority("ROLE_OWNER")) || tokenUsername.equals(pathUsername);
        }
        return false;
    }
}
