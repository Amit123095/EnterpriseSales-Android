package com.amit.enterprisesales.backend.service;

import com.amit.enterprisesales.backend.dto.auth.LoginRequest;
import com.amit.enterprisesales.backend.dto.auth.LoginResponse;
import com.amit.enterprisesales.backend.dto.auth.RegisterRequest;
import com.amit.enterprisesales.backend.dto.auth.RegisterResponse;
import com.amit.enterprisesales.backend.entity.User;
import com.amit.enterprisesales.backend.entity.UserRole;
import com.amit.enterprisesales.backend.exception.EmailAlreadyRegisteredException;
import com.amit.enterprisesales.backend.repository.UserRepository;
import com.amit.enterprisesales.backend.security.JwtService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import com.amit.enterprisesales.backend.dto.auth.CurrentUserResponse;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final long jwtExpirationMs;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            @Value("${security.jwt.expiration-ms}") long jwtExpirationMs
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.jwtExpirationMs = jwtExpirationMs;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {

        String email = request.email()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (request.password().getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException(
                    "Password cannot exceed 72 UTF-8 bytes"
            );
        }

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new EmailAlreadyRegisteredException(
                    "An account with this email already exists"
            );
        }

        User user = new User();

        user.setFullName(request.fullName().trim());
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setUserRole(UserRole.SALES_REP);
        user.setActive(true);

        try {
            User savedUser = userRepository.saveAndFlush(user);

            return new RegisterResponse(
                    savedUser.getId(),
                    savedUser.getFullName(),
                    savedUser.getEmail(),
                    savedUser.getUserRole(),
                    savedUser.isActive(),
                    savedUser.getCreatedAt()
            );

        } catch (DataIntegrityViolationException exception) {
            throw new EmailAlreadyRegisteredException(
                    "An account with this email already exists"
            );
        }
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {

        String email = request.email()
                .trim()
                .toLowerCase(Locale.ROOT);

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email or password"
                ));

        boolean passwordMatches = passwordEncoder.matches(
                request.password(),
                user.getPasswordHash()
        );

        if (!passwordMatches || !user.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }

        String accessToken = jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getUserRole().name()
        );

        return new LoginResponse(
                accessToken,
                "Bearer",
                jwtExpirationMs / 1000,
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getUserRole().name()
        );
    }

    @Transactional(readOnly = true)
    public CurrentUserResponse getCurrentUser(String email) {

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "User account not found"
                ));

        if (!user.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "User account is inactive"
            );
        }

        return new CurrentUserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getUserRole().name()
        );
    }
}
