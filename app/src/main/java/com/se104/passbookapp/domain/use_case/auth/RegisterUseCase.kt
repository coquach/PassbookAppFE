package com.se104.passbookapp.domain.use_case.auth

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.request.RegisterRequest
import com.se104.passbookapp.data.dto.response.TokenResponse
import com.se104.passbookapp.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository,
) {
    operator fun invoke(request: RegisterRequest) = flow<ApiResponse<TokenResponse>> {
        emit(ApiResponse.Loading)
        try {
            authRepository.register(request).collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e.message.toString(), 401))

        }
    }.flowOn(Dispatchers.IO)
}