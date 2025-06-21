package com.se104.passbookapp.domain.use_case.auth

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SendOtpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String) = flow<ApiResponse<Unit>> {
        try {
                authRepository.verifyEmail(mapOf("email" to email)).collect {
                    emit(it)
                }
        }catch (e: Exception){
            emit(ApiResponse.Failure("Đã có lỗi xảy ra khi gửi email xác nhận", 999))
        }
    }.flowOn(Dispatchers.IO)
}