package com.se104.passbookapp.data.dto.filter

import java.math.BigDecimal
import java.time.LocalDate

data class SavingTicketFilter (
    val order: String = "desc",
    val sortBy: String = "id",
    val userId: Long?=null,
    val savingTypeId: Long?=null,
    val isActive: Boolean=true,
    val startDate: LocalDate?=null,
    val endDate: LocalDate?=null,

)