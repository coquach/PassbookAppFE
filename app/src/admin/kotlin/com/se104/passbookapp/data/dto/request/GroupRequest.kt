package com.se104.passbookapp.data.dto.request

data class GroupRequest(
    val name: String,
    val description: String,
    val permissionIds: List<Int>
)
