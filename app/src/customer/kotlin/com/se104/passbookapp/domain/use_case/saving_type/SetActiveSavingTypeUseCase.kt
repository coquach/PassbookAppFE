package com.se104.passbookapp.domain.use_case.saving_type

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.domain.repository.SavingTypeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class SetActiveSavingTypeUseCase @Inject constructor(
    private val savingTypeRepository: SavingTypeRepository
){
    operator fun invoke(id: Long, isActive: Boolean) = flow<ApiResponse<Unit>>  {
        try {
            savingTypeRepository.setActiveSavingType(id, isActive).collect{
                emit(it)
            }
        }catch (e : Exception){
            emit(ApiResponse.Failure("Đã có lỗi xảy ra khi cập nhật trạng thái loại tiết kiệm", 999))
        }
    }.flowOn(Dispatchers.IO)
}