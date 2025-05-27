package com.se104.passbookapp.data.dto.request

import java.math.BigDecimal

data class TransactionRequest(
    val userId: Long,
    val amount: BigDecimal,
    val transactionType: String,
)
