package com.se104.passbookapp.domain.repository

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.request.SavingTypeRequest
import com.se104.passbookapp.data.model.SavingType
import kotlinx.coroutines.flow.Flow

interface SavingTypeRepository {
    fun getActiveSavingTypes(): Flow<ApiResponse<List<SavingType>>>
    fun getInactiveSavingTypes(): Flow<ApiResponse<List<SavingType>>>
    fun createSavingType(request: SavingTypeRequest): Flow<ApiResponse<SavingType>>
    fun updateSavingType(id: Long, request: SavingTypeRequest): Flow<ApiResponse<SavingType>>
    fun deleteSavingType(id: Long): Flow<ApiResponse<Unit>>
    fun setActiveSavingType(id: Long, isActive: Boolean): Flow<ApiResponse<Unit>>
}