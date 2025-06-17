package com.se104.passbookapp.data.remote.api

import com.se104.passbookapp.data.dto.request.SavingTicketRequest
import com.se104.passbookapp.data.dto.response.PageResponse
import com.se104.passbookapp.data.model.SavingTicket
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface SavingTicketApiService {
    companion object {
        const val PR = "saving-tickets"
    }
    @GET(PR)
    suspend fun getSavingTickets(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sortBy") sortBy: String = "id",
        @Query("order") order: String = "asc",
        @Query("userId") userId: Long?= null,
        @Query("savingTypeId") savingTypeId: Long?= null,
        @Query("isActive") isActive: Boolean?= null,
        @Query("startDate") startDate: String?= null,
        @Query("endDate") endDate: String?= null,
    ): Response<PageResponse<SavingTicket>>



    @POST(PR)
    suspend fun createSavingTicket(@Body request: SavingTicketRequest): Response<SavingTicket>

    @PATCH("$PR/activate/{id}")
    suspend fun setSavingTicketActive(@Path("id") id: Long, @Body isActive: Boolean): Response<Unit>



}