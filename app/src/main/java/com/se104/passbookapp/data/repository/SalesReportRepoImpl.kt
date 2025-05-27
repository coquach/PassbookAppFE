package com.se104.passbookapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.se104.passbookapp.data.dto.filter.SalesReportFilter
import com.se104.passbookapp.data.model.SalesReport
import com.se104.passbookapp.data.paging.SalesReportPagingSource
import com.se104.passbookapp.data.remote.api.SalesReportApiService
import com.se104.passbookapp.domain.repository.SalesReportRepository
import com.se104.passbookapp.utils.Constants.ITEMS_PER_PAGE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SalesReportRepoImpl @Inject constructor(
    private val salesReportApiService: SalesReportApiService,
) : SalesReportRepository {
    override fun getSavingTickets(filter: SalesReportFilter): Flow<PagingData<SalesReport>> {
        return Pager(
            config = PagingConfig(
                pageSize = ITEMS_PER_PAGE,
                initialLoadSize = ITEMS_PER_PAGE,
                prefetchDistance = 2,
                enablePlaceholders = true

            ),
            pagingSourceFactory = {
                SalesReportPagingSource(salesReportApiService, filter)
            }
        ).flow
    }
}