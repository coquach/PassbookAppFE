package com.se104.passbookapp.domain.repository

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.model.MonthlyReport
import kotlinx.coroutines.flow.Flow

interface ReportRepository {
    fun getMonthlyReport(month: Int, year: Int): Flow<ApiResponse<MonthlyReport>>
    fun getMonthlyReports(fromYear: Int, fromMonth: Int, toYear: Int, toMonth: Int): Flow<ApiResponse<List<MonthlyReport>>>

}