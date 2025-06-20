package com.se104.passbookapp.data.dto.request

import java.math.BigDecimal

data class RegisterRequest(
    val password: String,
    val email: String,
    val phone: String,
    val fullName: String,
    val dateOfBirth: String,
    val citizenID: String,
    val address: String,
)
