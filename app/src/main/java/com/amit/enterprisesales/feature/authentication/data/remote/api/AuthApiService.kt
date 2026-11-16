package com.amit.enterprisesales.feature.authentication.data.remote.api

import com.amit.enterprisesales.feature.authentication.data.remote.dto.LoginRequestDto
import com.amit.enterprisesales.feature.authentication.data.remote.dto.LoginResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("api/v1/auth/login")
    suspend fun login(
        @Body request: LoginRequestDto
    ): LoginResponseDto
}