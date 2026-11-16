package com.amit.enterprisesales.feature.authentication.presentation

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isPasswordVisible: Boolean = false,
    val statusMessage: String? = null,
    val isLoading: Boolean = false
)