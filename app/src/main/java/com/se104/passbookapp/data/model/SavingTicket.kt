package com.se104.passbookapp.data.model

import com.se104.passbookapp.utils.json_format.BigDecimalSerializer
import com.se104.passbookapp.utils.json_format.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime

@Serializable
data class SavingTicket(
    val id: Long,
    val userId: Long,
    val savingTypeId: Long,
    val savingTypeName: String,
    @Serializable(with = BigDecimalSerializer::class)
    val duration: BigDecimal,
   @Serializable(with = BigDecimalSerializer::class)
    val interestRate: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal,
    @Serializable(with = LocalDateTimeSerializer::class)
    val startDate: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val maturityDate: LocalDateTime,
    @Serializable(with = BigDecimalSerializer::class)
    val balance: BigDecimal,

    val withdrawalTickets: List<WithdrawalTicket>,
)

