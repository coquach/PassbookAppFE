package com.se104.passbookapp.data.remote.api

import com.se104.passbookapp.data.dto.response.PageResponse
import com.se104.passbookapp.data.model.SalesReport
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SalesReportApiService {
    companion object{
        const val PR = "sales-reports"
    }
    
    @GET(PR)
    suspend fun getSavingTickets(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sortBy") sortBy: String = "id",
        @Query("order") order: String = "asc",
        @Query("startDate") startDate: String?= null,
        @Query("endDate") endDate: String?= null,
    ): Response<PageResponse<SalesReport>>
}