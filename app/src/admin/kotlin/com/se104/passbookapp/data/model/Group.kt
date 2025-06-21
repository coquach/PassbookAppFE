package com.se104.passbookapp.data.model

data class Group(
    val id: Int?= null,
    val name: String="",
    val description: String= "",
    val permissions: List<Permission> = emptyList(),
)
