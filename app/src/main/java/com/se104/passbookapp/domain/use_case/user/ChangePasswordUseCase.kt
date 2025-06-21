package com.se104.passbookapp.domain.use_case.user

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(oldPassword: String, newPassword: String) = flow<ApiResponse<Unit>> {
        try {
            userRepository.changePassword(
                mapOf(
                    "oldPassword" to oldPassword,
                    "newPassword" to newPassword
                )
            ).collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(ApiResponse.Failure("Đã có lỗi xảy ra khi thay đổi mật khẩu", 999))
        }
    }.flowOn(Dispatchers.IO)
}