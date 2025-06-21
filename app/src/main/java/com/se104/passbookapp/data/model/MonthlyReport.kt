package com.se104.passbookapp.data.model

import java.math.BigDecimal
import java.time.LocalDate

data class MonthlyReport(
    val reportMonth: LocalDate,
    val totalIncome: BigDecimal,
    val totalExpense: BigDecimal,
    val difference: BigDecimal
)
