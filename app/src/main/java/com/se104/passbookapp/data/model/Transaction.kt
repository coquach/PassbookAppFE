package com.se104.passbookapp.data.model

import com.se104.passbookapp.utils.json_format.BigDecimalSerializer
import com.se104.passbookapp.utils.json_format.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime

@Serializable
data class Transaction(
    val id: Long,
    val citizenID: String,
    val userFullName: String,
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val remainingBalance: BigDecimal,
    val transactionType: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
)
