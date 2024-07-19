package com.example.authorizationserver;

import com.example.authorizationserver.dto.RegisterRequest;
import com.example.authorizationserver.dto.UserResponse;
import com.example.authorizationserver.exceptions.UserExistsException;
import com.example.authorizationserver.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username: " + username)
        );
    }


    public String login(String usernameOrEmailOrPhone, String password) {
        Optional<User> user = userRepository.findByUsernameOrEmailOrPhoneNumber(usernameOrEmailOrPhone, usernameOrEmailOrPhone, usernameOrEmailOrPhone);
        if (user.isPresent()) {
            if(!passwordEncoder.matches(password, user.get().getPassword())) {
                throw new BadCredentialsException("Wrong password");
            }
            return jwtTokenProvider.generateToken(user.get());
        }else{
            throw new UsernameNotFoundException("User not found with username: " + usernameOrEmailOrPhone);
        }
    }

    public String register(RegisterRequest registerRequest){
        userRepository.findByUsername(registerRequest.username())
                .or(() -> userRepository.findByEmail(registerRequest.email()))
                .or(() -> userRepository.findByPhoneNumber(registerRequest.phoneNumber()))
                .ifPresent(user -> {throw new UserExistsException();});

        User user = User.builder()
                .username(registerRequest.username())
                .password(passwordEncoder.encode(registerRequest.password()))
                .email(registerRequest.email())
                .phoneNumber(registerRequest.phoneNumber())
                .build();
        userRepository.save(user);

        return login(user.getUsername(), registerRequest.password());
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map((User::toDTO)).toList();
    }

    public UserResponse getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if(user.isPresent()) {
            return user.get().toDTO();
        }else{
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}

