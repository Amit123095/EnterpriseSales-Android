package com.amit.enterprisesales.feature.authentication.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidateLoginInputUseCaseTest {

    private val useCase = ValidateLoginInputUseCase()

    @Test
    fun validCredentials_returnsSuccess() {

        val result = useCase(
            email = "test@example.com",
            password = "password123"
        )

        assertTrue(result.isValid)
    }

    @Test
    fun emptyEmail_returnsEmailError() {

        val result = useCase(
            email = "",
            password = "password123"
        )

        assertEquals("Email is required", result.emailError)
    }

    @Test
    fun invalidEmail_returnsEmailError() {

        val result = useCase(
            email = "amitgmail.com",
            password = "password123"
        )

        assertEquals(
            "Enter a valid email address",
            result.emailError
        )
    }

    @Test
    fun emptyPassword_returnsPasswordError() {

        val result = useCase(
            email = "test@example.com",
            password = ""
        )

        assertEquals(
            "Password is required",
            result.passwordError
        )
    }

    @Test
    fun shortPassword_returnsPasswordError() {

        val result = useCase(
            email = "test@example.com",
            password = "12345"
        )

        assertEquals(
            "Password must contain at least 8 characters",
            result.passwordError
        )
    }
}