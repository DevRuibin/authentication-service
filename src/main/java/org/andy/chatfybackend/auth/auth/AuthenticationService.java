package org.andy.chatfybackend.auth.auth;

import lombok.RequiredArgsConstructor;
import org.andy.chatfybackend.auth.basic_user.BasicUser;
import org.andy.chatfybackend.auth.basic_user.BasicUserRepository;
import org.andy.chatfybackend.auth.config.JwtService;
import org.andy.chatfybackend.auth.exceptions.DuplicateUserException;
import org.andy.chatfybackend.auth.exceptions.IncorrectPasswordException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final BasicUserRepository basicUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        if(basicUserRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateUserException("Email already exists");
        }
        var user = BasicUser.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        basicUserRepository.save(user);
        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .jwt(jwt)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            ));
        } catch (Exception e) {
            throw new IncorrectPasswordException("Incorrect password");
        }
        var user = basicUserRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .jwt(jwt)
                .build();
    }
}
