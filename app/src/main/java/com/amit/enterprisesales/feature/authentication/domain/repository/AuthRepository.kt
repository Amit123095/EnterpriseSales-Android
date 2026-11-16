package com.amit.enterprisesales.feature.authentication.domain.repository

import com.amit.enterprisesales.feature.authentication.domain.model.AuthResult

interface AuthRepository {

    suspend fun login(
        email: String,
        password: String
    ): AuthResult
}