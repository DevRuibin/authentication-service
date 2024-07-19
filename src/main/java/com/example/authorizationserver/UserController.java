package com.example.authorizationserver;

import com.example.authorizationserver.clients.AuthorizationClient;
import com.example.authorizationserver.dto.AuthorizationRequest;
import com.example.authorizationserver.dto.AuthorizationResponse;
import com.example.authorizationserver.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthorizationClient authorizationClient;
    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponse> getUserByUserName(@PathVariable String username,
                                                          @RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.startsWith("Bearer ") ? bearerToken.substring(7) : bearerToken;
        String path = "/api/v1/users/" + username;
        System.out.println(username);
        System.out.println(token);
        ResponseEntity<AuthorizationResponse> authorize = authorizationClient.authorize(new AuthorizationRequest("get-single-user",
                token, path));
        System.out.println(authorize.getBody());
        if(!Objects.requireNonNull(authorize.getBody()).authorized()){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }


}
