package com.se104.passbookapp.data.model

import com.se104.passbookapp.ui.screen.auth.signup.SignUp
import com.se104.passbookapp.utils.json_format.BigDecimalSerializer
import com.se104.passbookapp.utils.json_format.LocalDateSerializer
import com.se104.passbookapp.utils.json_format.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class SavingTicket(
    val id: Long,
    val userId: Long,
    val citizenId: String,
    val savingTypeId: Long,
    val savingTypeName: String,
    val duration: Int,
   @Serializable(with = BigDecimalSerializer::class)
    val interestRate: BigDecimal,
    @Serializable(with = BigDecimalSerializer::class)
    val amount: BigDecimal,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    @Serializable(with = LocalDateSerializer::class)
    val maturityDate: LocalDate?=null,
    @Serializable(with = BigDecimalSerializer::class)
    val balance: BigDecimal,

    val withdrawalTickets: List<WithdrawalTicket>,
)

