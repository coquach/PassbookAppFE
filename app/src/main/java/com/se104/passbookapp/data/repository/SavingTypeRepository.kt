package com.se104.passbookapp.data.repository

import com.se104.passbookapp.data.dto.apiRequestFlow
import com.se104.passbookapp.data.dto.request.SavingTypeRequest
import com.se104.passbookapp.data.remote.api.SavingApiService
import javax.inject.Inject

class SavingTypeRepository @Inject constructor(
    private val savingApiService: SavingApiService,
) {
    fun getActiveSavingTypes() = apiRequestFlow {
            savingApiService.getActiveSavingTypes()
        }

    fun getInactiveSavingTypes() = apiRequestFlow {
        savingApiService.getInactiveSavingTypes()
    }

    fun createSavingType(request: SavingTypeRequest) = apiRequestFlow {
        savingApiService.createSavingType(request)

    }
    fun updateSavingType(request: SavingTypeRequest, id: Long) = apiRequestFlow {
        savingApiService.updateSavingType(request, id)
    }
    fun deleteSavingType(id: Long) = apiRequestFlow {
        savingApiService.deleteSavingType(id)
    }
    fun setActivateSavingType(id: Long) = apiRequestFlow {
        savingApiService.setActivateSavingType(id)
    }
}