package com.se104.passbookapp.data.remote.api


import com.se104.passbookapp.data.model.MonthlyReport
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ReportApiService {
    companion object {
        const val PR = "reports"
    }

    @GET("$PR/monthly")
    suspend fun getMonthlyReport(
        @Query("month") month: Int,
        @Query("year") year: Int
    ): Response<MonthlyReport>

    @GET("$PR/list-month")
    suspend fun getMonthlyReports(
        @Query("fromYear") fromYear: Int,
        @Query("fromMonth") fromMonth: Int,
        @Query("toYear") toYear: Int,
        @Query("toMonth") toMonth: Int
    ): Response<List<MonthlyReport>>

}