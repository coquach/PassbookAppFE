package com.se104.passbookapp.utils

fun List<String>.hasPermission(permission: String): Boolean {
    return this.contains(permission)
}


fun List<String>.hasAllPermissions(vararg required: String): Boolean {
    return required.all { this.contains(it) }
}

fun List<String>.hasAnyPermission(vararg required: String): Boolean {
    return required.any { this.contains(it) }
}