package com.se104.passbookapp.domain.use_case.saving_type

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.request.SavingTypeRequest
import com.se104.passbookapp.data.model.SavingType
import com.se104.passbookapp.domain.repository.SavingTypeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class UpdateSavingTypeUseCase @Inject constructor(
    private val savingTypeRepository: SavingTypeRepository,
) {
    operator fun invoke(savingType: SavingType) = flow<ApiResponse<SavingType>> {
        try {
            emit(ApiResponse.Loading)
            val id = savingType.id!!
            val request = SavingTypeRequest(
                typeName = savingType.typeName,
                duration = savingType.duration,
                interestRate = savingType.interestRate
            )
            savingTypeRepository.updateSavingType(id, request).collect {
                emit(it)
            }
        } catch (e: Exception) {
            emit(ApiResponse.Failure("Đã có lỗi xảy ra khi tạo loại tiết kiệm", 999))
        }
    }.flowOn(Dispatchers.IO)
}