package com.se104.passbookapp.domain.repository

import androidx.paging.PagingData
import com.se104.passbookapp.data.dto.filter.SalesReportFilter
import com.se104.passbookapp.data.model.SalesReport
import kotlinx.coroutines.flow.Flow

interface SalesReportRepository {
    fun getSavingTickets(filter: SalesReportFilter): Flow<PagingData<SalesReport>>
}