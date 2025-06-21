package com.se104.passbookapp.domain.use_case.auth

import com.se104.passbookapp.BuildConfig
import com.se104.passbookapp.data.datastore.UserSessionRepository
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.request.LoginRequest
import com.se104.passbookapp.data.dto.response.TokenResponse
import com.se104.passbookapp.di.TokenManager
import com.se104.passbookapp.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val userSessionRepository: UserSessionRepository
) {
    operator fun invoke(request: LoginRequest) = flow<ApiResponse<TokenResponse>> {
        emit(ApiResponse.Loading)
        try {
            authRepository.login(request).collect{ response ->
                when(response){
                    is ApiResponse.Success -> {
                        val role = if (response.data.permissions.contains("ADMIN_PREVILAGE")) "admin" else "customer"
                        if(role != BuildConfig.FLAVOR){
                            authRepository.logout(response.data.accessToken)
                            emit(ApiResponse.Failure("App này không hỗ trợ tài khoản này", 403))
                            return@collect
                        }
                        tokenManager.saveToken(response.data.accessToken, response.data.refreshToken)
                        userSessionRepository.saveUserSession(response.data.userId, response.data.permissions)
                        emit(ApiResponse.Success(response.data))
                    }
                    is ApiResponse.Failure -> {
                        emit(ApiResponse.Failure(response.errorMessage, response.code))
                    }
                    is ApiResponse.Loading -> {
                        emit(ApiResponse.Loading)
                    }

                }
            }
        }catch (e: Exception){
            emit(ApiResponse.Failure("Đã có lỗi xảy ra trong quá trình đăng nhập", 403))

        }
    }.flowOn(Dispatchers.IO)
}