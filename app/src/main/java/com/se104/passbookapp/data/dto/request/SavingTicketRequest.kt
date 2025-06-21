package com.se104.passbookapp.data.dto.request

import java.math.BigDecimal

data class SavingTicketRequest (
    val citizenID: String?=null,
    val savingTypeId: Long?=null,
    val amount: BigDecimal=BigDecimal.ZERO,
)