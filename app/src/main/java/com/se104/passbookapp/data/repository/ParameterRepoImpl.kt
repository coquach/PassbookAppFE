package com.se104.passbookapp.data.repository

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.apiRequestFlow
import com.se104.passbookapp.data.dto.request.ParameterRequest
import com.se104.passbookapp.data.model.Parameter
import com.se104.passbookapp.data.remote.api.ParameterApiService
import com.se104.passbookapp.domain.repository.ParameterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ParameterRepoImpl @Inject constructor(
    private val parameterApiService: ParameterApiService
): ParameterRepository {
    override fun getParameters(): Flow<ApiResponse<Parameter>> {
        return apiRequestFlow {
            parameterApiService.getParameters()
        }
    }

    override fun updateParameters(request: ParameterRequest): Flow<ApiResponse<Parameter>> {
        return apiRequestFlow {
            parameterApiService.updateParameters(request)
        }
    }
}