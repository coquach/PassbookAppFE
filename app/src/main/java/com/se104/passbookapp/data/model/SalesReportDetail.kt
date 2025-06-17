package com.se104.passbookapp.data.model

import com.se104.passbookapp.utils.json_format.BigDecimalSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class SalesReportDetail(
    val savingTypeId: Long,
    val savingTypeName: String,
    @Serializable(with = BigDecimalSerializer::class)
    val totalIncome: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val totalExpense: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val difference: BigDecimal,
)
