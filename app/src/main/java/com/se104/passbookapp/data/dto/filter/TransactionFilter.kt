package com.se104.passbookapp.data.dto.filter

import java.time.LocalDate

data class TransactionFilter(
    val order: String = "desc",
    val sortBy: String = "id",
    val userId: Long?=null,
    val transactionType: String?=null,
    val startDate: LocalDate?=null,
    val endDate: LocalDate?=null,
    )
