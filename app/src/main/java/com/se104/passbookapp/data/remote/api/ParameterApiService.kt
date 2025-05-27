package com.se104.passbookapp.data.remote.api

import com.se104.passbookapp.data.dto.request.ParameterRequest
import com.se104.passbookapp.data.model.Parameter
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ParameterApiService {
    companion object {
        const val PR = "parameters"
    }
    @GET(PR)
    suspend fun getParameters(): Response<Parameter>

    @POST(PR)
    suspend fun updateParameters(@Body request: ParameterRequest): Response<Parameter>

}