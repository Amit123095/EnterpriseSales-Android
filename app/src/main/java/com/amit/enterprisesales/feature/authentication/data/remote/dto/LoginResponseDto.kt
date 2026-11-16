package com.amit.enterprisesales.feature.authentication.data.remote.dto

data class LoginResponseDto(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Long,
    val userId: String,
    val fullName: String,
    val email: String,
    val role: String
)
