package com.amit.enterprisesales.backend.dto.auth;

import java.util.UUID;

public record CurrentUserResponse(
        UUID userId,
        String fullName,
        String email,
        String role
) {
}
