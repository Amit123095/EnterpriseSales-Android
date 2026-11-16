package com.amit.enterprisesales.backend.dto.auth;

import com.amit.enterprisesales.backend.entity.UserRole;

import java.time.OffsetDateTime;
import java.util.UUID;

public record RegisterResponse(
        UUID id,
        String fullName,
        String email,
        UserRole userRole,
        boolean active,
        OffsetDateTime createdAt
) {
}
