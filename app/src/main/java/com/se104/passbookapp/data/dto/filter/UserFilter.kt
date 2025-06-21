package com.se104.passbookapp.data.dto.filter

import java.time.Instant

data class UserFilter(
    val order: String = "desc",
    val sortBy: String = "fullName",
    val groupId: Int? = null,
    val fullName: String? = null,
    val citizenID: String? = null,
    val isActive: Boolean = true,
    val isRefresh: String?=null
)
