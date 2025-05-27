package com.se104.passbookapp.data.dto.filter

import java.math.BigDecimal

data class SavingTicketFilter (
    val userId: Long?=null,
    val savingTypeId: Long?=null,
    val isActive: Boolean?=null,
    val minAmount: BigDecimal?=null,
    val maxAmount: BigDecimal?=null,
    val startDate: String?=null,
    val endDate: String?=null,

)