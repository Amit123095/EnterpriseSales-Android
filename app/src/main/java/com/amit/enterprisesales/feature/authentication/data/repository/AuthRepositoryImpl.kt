package com.amit.enterprisesales.feature.authentication.data.repository

import com.amit.enterprisesales.feature.authentication.data.remote.datasource.AuthRemoteDataSource
import com.amit.enterprisesales.feature.authentication.data.remote.dto.LoginRequestDto
import com.amit.enterprisesales.feature.authentication.domain.model.AuthResult
import com.amit.enterprisesales.feature.authentication.domain.model.AuthSession
import com.amit.enterprisesales.feature.authentication.domain.model.AuthUser
import com.amit.enterprisesales.feature.authentication.domain.repository.AuthRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.CancellationException

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ): AuthResult {

        return try {

            val response = remoteDataSource.login(
                LoginRequestDto(
                    email = email,
                    password = password
                )
            )

            AuthResult.Success(
                session = AuthSession(
                    accessToken = response.accessToken,
                    user = AuthUser(
                        id = response.userId,
                        name = response.fullName,
                        email = response.email
                    )
                )
            )

        } catch (exception: HttpException) {

            val message = when (exception.code()) {
                400 -> "Please check your login details"
                401 -> "Invalid email or password"
                403 -> "Access denied"
                404 -> "Authentication service not found"
                in 500..599 -> "Server temporarily unavailable"
                else -> "Login failed. Please try again."
            }

            AuthResult.Failure(message)

        } catch (exception: CancellationException) {

            throw exception

        } catch (exception: IOException) {

            AuthResult.Failure(
                "Unable to connect to the server. Check your internet connection."
            )
        } catch (exception: Exception) {

            AuthResult.Failure(
                "Something went wrong. Please try again."
            )
        }
    }
}
