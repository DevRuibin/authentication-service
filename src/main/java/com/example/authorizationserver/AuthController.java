package com.example.authorizationserver;

import com.example.authorizationserver.dto.LoginRequest;
import com.example.authorizationserver.dto.LoginResponse;
import com.example.authorizationserver.dto.RegisterRequest;
import com.example.authorizationserver.dto.RegisterResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        String token = userService.login(loginRequest.usernameOrEmailOrPhone(), loginRequest.password());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest registerRequest){
        String token = userService.register(registerRequest);
        return ResponseEntity.ok(new RegisterResponse(token));
    }
}
