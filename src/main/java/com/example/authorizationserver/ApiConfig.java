package com.example.authorizationserver;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@ConfigurationProperties(prefix = "api.v1")
@Component
@ToString
public class ApiConfig {
    private Map<String, Endpoint> endpoints;

    @Setter
    @ToString
    @Getter
    public static class Endpoint {
        private String path;
        private Set<String> roles;

        public Set<SimpleGrantedAuthority> getRoles() {
            return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())).collect(Collectors.toSet());
        }
    }


}
