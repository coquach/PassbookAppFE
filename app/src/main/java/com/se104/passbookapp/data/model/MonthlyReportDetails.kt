package com.se104.passbookapp.data.model

import java.math.BigDecimal

data class MonthlyReportDetails(
    val savingTypeId: Long,
    val savingTypeName: String,
    val totalIncome: BigDecimal,
    val totalExpense: BigDecimal,
    val difference: BigDecimal
)
