package com.se104.passbookapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.apiRequestFlow
import com.se104.passbookapp.data.dto.filter.TransactionFilter
import com.se104.passbookapp.data.dto.request.TransactionRequest
import com.se104.passbookapp.data.model.Transaction
import com.se104.passbookapp.data.paging.TransactionPagingSource
import com.se104.passbookapp.data.remote.api.TransactionApiService
import com.se104.passbookapp.domain.repository.TransactionRepository
import com.se104.passbookapp.utils.Constants.ITEMS_PER_PAGE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionRepoImpl @Inject constructor(
    private val transactionApiService: TransactionApiService,
) : TransactionRepository {
    override fun getTransactions(filter: TransactionFilter): Flow<PagingData<Transaction>> {
        return Pager(
            config = PagingConfig(
                pageSize = ITEMS_PER_PAGE,
                initialLoadSize = ITEMS_PER_PAGE,
                prefetchDistance = 2,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                TransactionPagingSource(transactionApiService, filter, false)
            }).flow
    }

    override fun getTransactionsForCustomer(filter: TransactionFilter): Flow<PagingData<Transaction>> {
        return Pager(
            config = PagingConfig(
                pageSize = ITEMS_PER_PAGE,
                initialLoadSize = ITEMS_PER_PAGE,
                prefetchDistance = 2,
                enablePlaceholders = true

            ),
            pagingSourceFactory = {
                TransactionPagingSource(transactionApiService, filter, true)
            }
        ).flow
    }

    override fun createTransaction(request: TransactionRequest): Flow<ApiResponse<Transaction>> {
        return apiRequestFlow {
            transactionApiService.createTransaction(request)
        }
    }
}