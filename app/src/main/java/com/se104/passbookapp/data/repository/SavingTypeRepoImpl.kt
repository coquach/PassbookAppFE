package com.se104.passbookapp.data.repository

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.apiRequestFlow
import com.se104.passbookapp.data.dto.request.SavingTypeRequest
import com.se104.passbookapp.data.model.SavingType
import com.se104.passbookapp.data.remote.api.SavingTypeApiService
import com.se104.passbookapp.domain.repository.SavingTypeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SavingTypeRepoImpl @Inject constructor(
    private val savingTypeApiService: SavingTypeApiService
) : SavingTypeRepository {
    override fun getActiveSavingTypes(): Flow<ApiResponse<List<SavingType>>> {
        return apiRequestFlow {
            savingTypeApiService.getActiveSavingTypes()
        }
    }

    override fun getInactiveSavingTypes(): Flow<ApiResponse<List<SavingType>>> {
       return apiRequestFlow {
           savingTypeApiService.getInactiveSavingTypes()
       }
    }

    override fun createSavingType(request: SavingTypeRequest): Flow<ApiResponse<SavingType>> {
        return apiRequestFlow {
            savingTypeApiService.createSavingType(request)

        }
    }

    override fun setActiveSavingType(id: Long, isActive: Boolean): Flow<ApiResponse<Unit>> {
        return apiRequestFlow {
            savingTypeApiService.setActivateSavingType(id, isActive)
        }
    }

    override fun updateSavingType(
        id: Long,
        request: SavingTypeRequest,
    ): Flow<ApiResponse<SavingType>> {
        return apiRequestFlow {
            savingTypeApiService.updateSavingType(request, id)

        }
    }

    override fun deleteSavingType(id: Long): Flow<ApiResponse<Unit>> {
        return apiRequestFlow {
            savingTypeApiService.deleteSavingType(id)
        }
    }

}