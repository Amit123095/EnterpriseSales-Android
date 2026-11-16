
package com.amit.enterprisesales.backend.dto.auth;

import java.util.UUID;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        UUID userId,
        String fullName,
        String email,
        String role
) {
}
