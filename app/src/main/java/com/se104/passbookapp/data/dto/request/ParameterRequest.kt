package com.se104.passbookapp.data.dto.request

import java.math.BigDecimal

data class ParameterRequest(
    val minAge: Int,
    val minTransactionAmount: BigDecimal,
    val maxTransactionAmount: BigDecimal,
    val minSavingAmount: BigDecimal,
)
