package com.se104.passbookapp.data.dto.filter

data class UserFilter(
    val order: String = "desc",
    val sortBy: String = "id",
    val fullName: String? = null,
    val citizenID: String? = null,
    val isActive: Boolean = true,
)
