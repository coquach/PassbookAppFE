package com.se104.passbookapp.data.model

import com.se104.passbookapp.utils.json_format.BigDecimalSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class Parameter(
    val minAge: Int,
    @Serializable(with = BigDecimalSerializer::class)
    val minTransactionAmount: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val maxTransactionAmount: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val minSavingAmount: BigDecimal,
)
