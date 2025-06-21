package com.se104.passbookapp.domain.use_case.saving_type

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.model.SavingType
import com.se104.passbookapp.domain.repository.SavingTypeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetInActiveSavingTypesUseCase @Inject constructor(
    private val savingTypeRepository: SavingTypeRepository
) {
    operator fun invoke() = flow<ApiResponse<List<SavingType>>> {
        try {
            savingTypeRepository.getInactiveSavingTypes().collect{
                emit(it)
            }
        }catch (e: Exception){
            emit(ApiResponse.Failure("Đã có lỗi xảy ra khi tải danh sách loại tiết kiệm ẩn", 999))
        }
    }.flowOn(Dispatchers.IO)
}