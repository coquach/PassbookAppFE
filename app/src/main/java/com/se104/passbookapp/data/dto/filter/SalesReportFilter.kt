package com.se104.passbookapp.data.dto.filter

data class SalesReportFilter(
    val order: String = "desc",
    val sortBy: String = "id",
    val startDate: String?=null,
    val endDate: String?=null,
)
