package com.se104.passbookapp.domain.use_case.auth

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
    private val tokenManager: TokenManager
) {
    operator fun invoke(request: LoginRequest) = flow<ApiResponse<TokenResponse>> {
        emit(ApiResponse.Loading)
        try {
            authRepository.login(request).collect{ response ->
                when(response){
                    is ApiResponse.Success -> {
                        tokenManager.saveToken(response.data.accessToken, response.data.refreshToken)
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
            emit(ApiResponse.Failure(e.message.toString(), 401))

        }
    }.flowOn(Dispatchers.IO)
}