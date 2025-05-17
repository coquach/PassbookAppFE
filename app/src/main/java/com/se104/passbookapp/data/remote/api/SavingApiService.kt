package com.se104.passbookapp.data.remote.api

import com.se104.passbookapp.data.dto.request.SavingTypeRequest
import com.se104.passbookapp.data.model.SavingType
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface SavingApiService {
    companion object {
        const val PR = "saving-types"
    }

    @GET("$PR/active")
    suspend fun getActiveSavingTypes(): Response<List<SavingType>>

    @GET("$PR/inactive")
    suspend fun getInactiveSavingTypes(): Response<List<SavingType>>

    @POST(PR)
    suspend fun createSavingType(@Body request: SavingTypeRequest): Response<SavingType>

    @PUT("$PR/{id}")
    suspend fun updateSavingType(
        @Body request: SavingTypeRequest,
        @Path("id") id: Long,
    ): Response<SavingType>

    @DELETE("$PR/{id}")
    suspend fun deleteSavingType(@Path("id") id: Long): Response<Unit>

    @PUT("$PR/set-activate/{id}")
    suspend fun setActivateSavingType(@Path("id") id: Long): Response<Unit>
}