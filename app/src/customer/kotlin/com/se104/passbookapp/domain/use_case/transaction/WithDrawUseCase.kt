package com.se104.passbookapp.domain.use_case.transaction

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.model.Transaction
import com.se104.passbookapp.domain.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.math.BigDecimal
import javax.inject.Inject

class WithDrawUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository,
)  {
    operator fun invoke(userId: Long, amount: BigDecimal) = flow<ApiResponse<Transaction>> {
        try {
            transactionRepository.withdraw(userId, amount).collect { emit(it) }
        } catch (e: Exception) {
            emit(ApiResponse.Failure("Đã có lỗi xảy ra khi rút tiền", 999))
        }
    }.flowOn(Dispatchers.IO)
}