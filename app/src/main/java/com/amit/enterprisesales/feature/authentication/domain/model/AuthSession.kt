package com.amit.enterprisesales.feature.authentication.domain.model

data class AuthSession(
    val accessToken: String,
    val user: AuthUser
)