package com.se104.passbookapp.data.dto.filter

import java.math.BigDecimal
import java.time.LocalDate

data class TransactionFilter(
    val order: String = "desc",
    val sortBy: String = "id",
    val citizenID: String?=null,
    val amount: BigDecimal?=null,
    val transactionType: String?=null,
    val startDate: LocalDate?=null,
    val endDate: LocalDate?=null,
    )
