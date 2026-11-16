package com.amit.enterprisesales.feature.authentication.domain.usecase

import com.amit.enterprisesales.feature.authentication.domain.model.AuthResult
import com.amit.enterprisesales.feature.authentication.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val validateLoginInputUseCase: ValidateLoginInputUseCase
) {

    suspend operator fun invoke(
        email: String,
        password: String
    ): AuthResult {

        val validation = validateLoginInputUseCase(
            email = email,
            password = password
        )

        if (!validation.isValid) {
            return AuthResult.Failure(
                validation.emailError ?: validation.passwordError
                ?: "Invalid login details"
            )
        }

        return authRepository.login(
            email = email.trim(),
            password = password
        )
    }
}