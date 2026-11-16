package com.amit.enterprisesales.feature.authentication.domain.model

data class LoginValidationResult(
    val emailError: String? = null,
    val passwordError: String? = null
) {
    val isValid: Boolean
        get() = emailError == null && passwordError == null
}