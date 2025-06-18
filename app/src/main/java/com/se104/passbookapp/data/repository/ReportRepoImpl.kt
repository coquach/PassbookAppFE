package com.se104.passbookapp.data.repository

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.apiRequestFlow
import com.se104.passbookapp.data.model.DailyReport
import com.se104.passbookapp.data.model.MonthlyReport
import com.se104.passbookapp.data.remote.api.ReportApiService
import com.se104.passbookapp.domain.repository.ReportRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReportRepoImpl @Inject constructor(
    private val reportApiService: ReportApiService
) : ReportRepository {
    override fun getDailyReports(
        month: Int,
        year: Int,
    ): Flow<ApiResponse<List<DailyReport>>> {
        return apiRequestFlow {
            reportApiService.getDailyReports(month, year)
        }
    }

    override fun getMonthlyReports(
        fromYear: Int,
        fromMonth: Int,
        toYear: Int,
        toMonth: Int,
    ): Flow<ApiResponse<List<MonthlyReport>>> {
        return apiRequestFlow {
            reportApiService.getMonthlyReports(fromYear, fromMonth, toYear, toMonth)
        }
    }
}