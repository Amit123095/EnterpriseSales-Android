package com.amit.enterprisesales.feature.authentication.data.remote.datasource

import com.amit.enterprisesales.feature.authentication.data.remote.api.AuthApiService
import com.amit.enterprisesales.feature.authentication.data.remote.dto.LoginRequestDto
import com.amit.enterprisesales.feature.authentication.data.remote.dto.LoginResponseDto
import javax.inject.Inject

class AuthRemoteDataSource @Inject constructor(
    private val authApiService: AuthApiService
) {

    suspend fun login(
        request: LoginRequestDto
    ): LoginResponseDto {
        return authApiService.login(request)
    }
}