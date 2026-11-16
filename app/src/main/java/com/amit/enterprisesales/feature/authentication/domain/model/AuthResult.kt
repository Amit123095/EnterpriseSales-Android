package com.amit.enterprisesales.feature.authentication.domain.model

sealed interface AuthResult {

    data class Success(
        val session: AuthSession
    ) : AuthResult

    data class Failure(
        val message: String
    ) : AuthResult
}