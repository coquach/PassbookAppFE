package com.se104.passbookapp.domain.use_case.user

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SetGroupForUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(userId: Long, groupId: Int) = flow<ApiResponse<Unit>> {
        try {
            userRepository.setGroupUser(userId = userId, groupId = groupId).collect{
                emit(it)
            }
        }catch (e: Exception){
            emit(ApiResponse.Failure("Đã có lỗi xảy ra khi cập nhật vai trò người dùng", 999))
        }
    }.flowOn(Dispatchers.IO)
}