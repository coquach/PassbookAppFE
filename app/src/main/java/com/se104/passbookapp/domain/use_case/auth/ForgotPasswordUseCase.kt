package com.se104.passbookapp.domain.use_case.auth

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ForgotPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String) = flow<ApiResponse<Unit>> {
        try {
            authRepository.forgotPassword(mapOf("email" to email)).collect {
                emit(it)
            }
        }catch (e: Exception){
            emit(ApiResponse.Failure("Đã có lỗi xảy ra khi gửi email thay đổi mật khẩu", 999))
        }
    }.flowOn(Dispatchers.IO)
}