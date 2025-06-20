package com.se104.passbookapp.data.paging

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.apiRequestFlow
import com.se104.passbookapp.data.dto.filter.TransactionFilter
import com.se104.passbookapp.data.dto.response.PageResponse
import com.se104.passbookapp.data.model.Transaction
import com.se104.passbookapp.data.remote.api.TransactionApiService
import com.se104.passbookapp.utils.StringUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionPagingSource @Inject constructor(
    private val transactionApiService: TransactionApiService,
    private val filter: TransactionFilter,
    private val isCustomer: Boolean,
) : ApiPagingSource<Transaction>() {
    override suspend fun fetch(
        page: Int,
        size: Int,
    ): Flow<ApiResponse<PageResponse<Transaction>>> {
        return apiRequestFlow {
            if (isCustomer) {
                transactionApiService.getTransactionsForCustomer(
                    page = page,
                    size = size,
                    transactionType = filter.transactionType,
                    startDate = StringUtils.formatLocalDate(filter.startDate),
                    endDate = StringUtils.formatLocalDate(filter.endDate),
                    order = filter.order,
                    sortBy = filter.sortBy,
                )
            } else {
                transactionApiService.getTransactions(
                    page = page,
                    size = size,
                    citizenID = filter.citizenID,
                    transactionType = filter.transactionType,
                    startDate = StringUtils.formatLocalDate(filter.startDate),
                    endDate = StringUtils.formatLocalDate(filter.endDate),
                    order = filter.order,
                    sortBy = filter.sortBy,
                )
            }
        }
    }
}