package com.example.authorizationserver;

import com.example.authorizationserver.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthorizationService authorizationService;

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


    @PostMapping("/authorize")
    public ResponseEntity<AuthorizationResponse> authorize(@RequestBody AuthorizationRequest authorizationRequest){
        return ResponseEntity.ok(authorizationService.authorize(authorizationRequest));
    }

}
