package com.se104.passbookapp.domain.repository

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.request.ParameterRequest
import com.se104.passbookapp.data.model.Parameter
import kotlinx.coroutines.flow.Flow

interface ParameterRepository {
    fun getParameters(): Flow<ApiResponse<Parameter>>
    fun updateParameters(request: ParameterRequest): Flow<ApiResponse<Parameter>>
}