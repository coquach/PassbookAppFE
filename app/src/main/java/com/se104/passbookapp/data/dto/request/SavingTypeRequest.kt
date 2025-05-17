package com.se104.passbookapp.data.dto.request

import java.math.BigDecimal

data class SavingTypeRequest(
    val typeName: String,
    val duration: Int,
    val interestRate: BigDecimal
)
