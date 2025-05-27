package com.se104.passbookapp.data.model

import com.se104.passbookapp.utils.json_format.BigDecimalSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class SavingType(
    val id: Long?= null,
    val typeName: String= "",
    val duration: Int=1,
    @Serializable(with = BigDecimalSerializer::class)
    val interestRate: BigDecimal= BigDecimal.ZERO,
)
