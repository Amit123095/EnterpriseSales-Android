package com.amit.enterprisesales.feature.authentication.domain.usecase

import com.amit.enterprisesales.feature.authentication.domain.model.LoginValidationResult
import javax.inject.Inject

class ValidateLoginInputUseCase @Inject constructor() {

    operator fun invoke(
        email: String,
        password: String
    ): LoginValidationResult {

        val normalizedEmail = email.trim()

        val emailError = when {
            normalizedEmail.isBlank() ->
                "Email is required"

            !EMAIL_REGEX.matches(normalizedEmail) ->
                "Enter a valid email address"

            else -> null
        }

        val passwordError = when {
            password.isBlank() ->
                "Password is required"

            password.length < 8 ->
                "Password must contain at least 8 characters"

            else -> null
        }

        return LoginValidationResult(
            emailError = emailError,
            passwordError = passwordError
        )
    }

    private companion object {
        val EMAIL_REGEX = Regex(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        )
    }
}