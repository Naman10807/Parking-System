package com.parking.service.impl;

import com.parking.dto.request.LoginRequest;
import com.parking.dto.request.RegisterRequest;
import com.parking.dto.response.LoginResponse;
import com.parking.entity.User;
import com.parking.exception.DuplicateResourceException;
import com.parking.exception.InvalidCredentialsException;
import com.parking.repository.UserRepository;
import com.parking.security.JwtService;
import com.parking.security.UserPrincipal;
import com.parking.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public LoginResponse register(RegisterRequest request) {
        String username = request.getUsername().trim();

        if (userRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("Username '" + username + "' is already taken");
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        User savedUser = userRepository.save(user);
        UserPrincipal userPrincipal = new UserPrincipal(savedUser);

        return buildLoginResponse(userPrincipal);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername().trim(),
                            request.getPassword()));

            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return buildLoginResponse(userPrincipal);
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    private LoginResponse buildLoginResponse(UserPrincipal userPrincipal) {
        String token = jwtService.generateToken(userPrincipal);
        return LoginResponse.builder()
                .token(token)
                .username(userPrincipal.getUsername())
                .role(userPrincipal.getRole())
                .build();
    }
}
