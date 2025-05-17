package com.se104.passbookapp.data.model

import com.example.foodapp.utils.json_format.BigDecimalSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class SavingType(
    val id: Long,
    val typeName: String,
    val duration: Int,
    @Serializable(with = BigDecimalSerializer::class)
    val interestRate: BigDecimal,
)
