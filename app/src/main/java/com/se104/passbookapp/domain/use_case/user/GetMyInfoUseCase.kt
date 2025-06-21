package com.se104.passbookapp.domain.use_case.user

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.model.User
import com.se104.passbookapp.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetMyInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke() = flow<ApiResponse<User>>{
        try {
            userRepository.getMyInfo().collect{
                emit(it)
            }
        }catch (e : Exception){
            emit(ApiResponse.Failure("Đã có lỗi xảy ra khi tải thông tin người dùng", 999))
        }
    }.flowOn(Dispatchers.IO)
}