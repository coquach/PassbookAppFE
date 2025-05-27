package com.se104.passbookapp.data.dto.request

import java.math.BigDecimal

data class WithdrawalTicketRequest(
    val savingTicketId: Long,
    val withdrawalAmount: BigDecimal,
    val withdrawalDate: String,
)
