package com.se104.passbookapp.domain.use_case.permission

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.model.Permission
import com.se104.passbookapp.domain.repository.GroupRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetPermissionsNotAdminUseCase @Inject constructor(
    private val groupRepository: GroupRepository
) {
    operator fun invoke() = flow<ApiResponse<List<Permission>>> {
        try {
            groupRepository.getPermissions().collect{
                emit(it)
            }
        }catch (e: Exception){
            emit(ApiResponse.Failure("Đã có lỗi xảy ra khi tải danh sách quyền", 999))
        }
    }.flowOn(Dispatchers.IO)
}