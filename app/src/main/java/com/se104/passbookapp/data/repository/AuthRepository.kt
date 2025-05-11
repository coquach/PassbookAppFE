package com.se104.passbookapp.data.repository

import com.se104.passbookapp.data.dto.apiRequestFlow
import com.se104.passbookapp.data.dto.request.LoginRequest
import com.se104.passbookapp.data.dto.request.RegisterRequest
import com.se104.passbookapp.data.remote.api.AuthApiService
import com.se104.passbookapp.di.TokenManager
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authApiService: AuthApiService,
) {
    fun login(request: LoginRequest) =
        apiRequestFlow {
            authApiService.login(request)
        }


    fun register(request: RegisterRequest) = apiRequestFlow {
        authApiService.register(request)
    }
    fun refreshToken(token: String) = apiRequestFlow {
        authApiService.refreshToken(token)
    }
}