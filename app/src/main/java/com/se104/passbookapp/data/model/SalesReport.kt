package com.se104.passbookapp.data.model

import com.se104.passbookapp.utils.json_format.BigDecimalSerializer
import com.se104.passbookapp.utils.json_format.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalDate

@Serializable
data class SalesReport (
    val id: Long,
    @Serializable(with = LocalDateSerializer::class)
    val reportDate: LocalDate,
    @Serializable(with = BigDecimalSerializer::class)
    val totalIncome: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val totalExpense: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val difference: BigDecimal,

    val salesReportDetails: List<SalesReportDetail>
)