package com.se104.passbookapp.data.dto.request

import java.math.BigDecimal

data class SavingTicketRequest (
    val userId: Long,
    val savingTypeId: Long,
    val amount: BigDecimal,
    val startDate: String,
    val maturityDate: String,
)