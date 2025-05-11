package com.se104.passbookapp.data.dto.request

data class LoginRequest (
    val username: String,
    val password: String,
    val deviceToken: String
)