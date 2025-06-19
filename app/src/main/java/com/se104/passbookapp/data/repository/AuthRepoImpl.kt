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


    override fun logout(tokenAccess: String?): Flow<ApiResponse<Unit>> {
        return apiRequestFlow {

            authApiService.logout(tokenAccess)
        }
    }

    override fun forgotPassword(request: Map<String, String>): Flow<ApiResponse<Unit>> {
        return apiRequestFlow {
            authApiService.forgotPassword(request)
        }
    }

    override fun changePassword(request: Map<String, String>): Flow<ApiResponse<Unit>> {
       return apiRequestFlow {
           authApiService.changePassword(request)
       }
    }

    override fun verifyEmail(request: Map<String, String>): Flow<ApiResponse<Unit>> {
        return apiRequestFlow {
            authApiService.verifyEmail(request)
        }
    }

    override fun postMethodName(request: Map<String, String>): Flow<ApiResponse<Unit>> {
        return apiRequestFlow {
            authApiService.postMethodName(request)
        }
    }
}