package com.se104.passbookapp.domain.repository

import androidx.paging.PagingData
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.filter.TransactionFilter
import com.se104.passbookapp.data.model.Transaction
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface TransactionRepository {
fun getTransactions(filter: TransactionFilter): Flow<PagingData<Transaction>>
fun getTransactionsForCustomer(filter: TransactionFilter): Flow<PagingData<Transaction>>
fun deposit(userId: Long, amount: BigDecimal): Flow<ApiResponse<Transaction>>
fun withdraw(userId: Long, amount: BigDecimal): Flow<ApiResponse<Transaction>>
}