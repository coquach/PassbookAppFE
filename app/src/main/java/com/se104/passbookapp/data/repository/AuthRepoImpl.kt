package com.se104.passbookapp.data.repository

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.apiRequestFlow
import com.se104.passbookapp.data.dto.request.LoginRequest
import com.se104.passbookapp.data.dto.request.RegisterRequest
import com.se104.passbookapp.data.dto.response.TokenResponse
import com.se104.passbookapp.data.remote.api.AuthApiService
import com.se104.passbookapp.di.TokenManager
import com.se104.passbookapp.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val authApiService: AuthApiService,

) : AuthRepository {
    override fun login(request: LoginRequest): Flow<ApiResponse<TokenResponse>> {
        return apiRequestFlow {
            authApiService.login(request)
        }
    }

    override fun register(request: RegisterRequest): Flow<ApiResponse<TokenResponse>> {
        return apiRequestFlow {
            authApiService.register(request)
        }
    }

    override fun refreshToken(token: String): Flow<ApiResponse<TokenResponse>> {
        return apiRequestFlow {
            authApiService.refreshToken(token)
        }
    }

    override fun logout(): Flow<ApiResponse<Unit>> {
        return apiRequestFlow {

            authApiService.logout()
        }
    }
}