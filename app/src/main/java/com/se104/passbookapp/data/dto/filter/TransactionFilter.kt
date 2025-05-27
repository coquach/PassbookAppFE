package com.se104.passbookapp.data.dto.filter

data class TransactionFilter(
    val userId: Long?=null,
    val transactionType: String?=null,
    val startDate: String?=null,
    val endDate: String?=null,
    )
