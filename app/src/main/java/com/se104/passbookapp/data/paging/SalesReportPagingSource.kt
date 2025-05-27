package com.se104.passbookapp.data.paging

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.apiRequestFlow
import com.se104.passbookapp.data.dto.filter.SalesReportFilter
import com.se104.passbookapp.data.dto.response.PageResponse
import com.se104.passbookapp.data.model.SalesReport
import com.se104.passbookapp.data.remote.api.SalesReportApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SalesReportPagingSource @Inject constructor(
    private val salesReportApiService: SalesReportApiService,
    private val filter: SalesReportFilter,
) : ApiPagingSource<SalesReport>() {
    override suspend fun fetch(
        page: Int,
        size: Int,
    ): Flow<ApiResponse<PageResponse<SalesReport>>> {
        return apiRequestFlow {
            salesReportApiService.getSavingTickets(
                page = page,
                size = size,
                startDate = filter.startDate,
                endDate = filter.endDate,
            )
        }
    }
}