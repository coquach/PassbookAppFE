package com.se104.passbookapp.data.dto.response

import kotlinx.serialization.Serializable


@Serializable
data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: String
)
