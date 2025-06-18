package com.se104.passbookapp.domain.use_case.transaction

import com.se104.passbookapp.data.dto.filter.TransactionFilter
import com.se104.passbookapp.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionsForCustomerUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    operator fun invoke(filter: TransactionFilter) = transactionRepository.getTransactionsForCustomer(filter)
}