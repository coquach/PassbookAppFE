package com.se104.passbookapp.data.model

import com.se104.passbookapp.utils.json_format.BigDecimalSerializer
import com.se104.passbookapp.utils.json_format.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime

@Serializable
data class WithdrawalTicket(
    val id: Long,
    @Serializable(with = BigDecimalSerializer::class)
    val withdrawalAmount: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val actualAmount: BigDecimal,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
)
