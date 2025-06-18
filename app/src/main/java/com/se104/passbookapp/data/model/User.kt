package com.se104.passbookapp.data.model

import com.se104.passbookapp.utils.json_format.BigDecimalSerializer
import com.se104.passbookapp.utils.json_format.LocalDateSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalDate

@Serializable
data class User(
    val id: Long,
    val username: String,
    val email: String,
    val phone: String,
    val fullName: String,
    @Serializable(with = LocalDateSerializer::class)
    val dateOfBirth: LocalDate,
    val citizenID: String,
    val address: String,
    @Serializable(with = BigDecimalSerializer::class)
    val balance: BigDecimal

)
