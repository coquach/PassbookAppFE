package com.se104.passbookapp.domain.repository

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.request.LoginRequest
import com.se104.passbookapp.data.dto.request.RegisterRequest
import com.se104.passbookapp.data.dto.response.TokenResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(request: LoginRequest): Flow<ApiResponse<TokenResponse>>
    fun register(request: RegisterRequest): Flow<ApiResponse<TokenResponse>>
    fun refreshToken(token: String): Flow<ApiResponse<TokenResponse>>
    fun logout(): Flow<ApiResponse<Unit>>
}