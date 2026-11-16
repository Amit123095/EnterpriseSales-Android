package com.amit.enterprisesales.feature.authentication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amit.enterprisesales.feature.authentication.domain.model.AuthResult
import com.amit.enterprisesales.feature.authentication.domain.usecase.LoginUseCase
import com.amit.enterprisesales.feature.authentication.domain.usecase.ValidateLoginInputUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val validateLoginInput: ValidateLoginInputUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = null,
                statusMessage = null
            )
        }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                passwordError = null,
                statusMessage = null
            )
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update {
            it.copy(isPasswordVisible = !it.isPasswordVisible)
        }
    }

    fun onLoginClick() {
        val currentState = _uiState.value

        if (currentState.isLoading) return

        val validation = validateLoginInput(
            currentState.email,
            currentState.password
        )

        if (!validation.isValid) {
            _uiState.update {
                it.copy(
                    emailError = validation.emailError,
                    passwordError = validation.passwordError,
                    statusMessage = null
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    emailError = null,
                    passwordError = null,
                    statusMessage = null
                )
            }

            try {
                val result = loginUseCase(
                    email = currentState.email.trim(),
                    password = currentState.password
                )

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        statusMessage = when (result) {
                            is AuthResult.Success ->
                                "Welcome, ${result.session.user.name}!"

                            is AuthResult.Failure ->
                                result.message
                        }
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        statusMessage = "Something went wrong. Please try again."
                    )
                }
            }
        }
    }
}