package com.se104.passbookapp.domain.use_case.group

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.request.GroupRequest
import com.se104.passbookapp.data.model.Group
import com.se104.passbookapp.domain.repository.GroupRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UpdateGroupUseCase @Inject constructor(
    private val groupRepository: GroupRepository

) {
    operator fun invoke(request: GroupRequest, id: Int) = flow<ApiResponse<Group>> {
        try {
            groupRepository.updateGroup(request = request, id = id).collect{
                emit(it)
            }
        }catch (e: Exception){
            emit(ApiResponse.Failure("Đã có lỗi xảy ra khi cập nhật nhóm người dùng", 999))
        }
    }.flowOn(Dispatchers.IO)
}