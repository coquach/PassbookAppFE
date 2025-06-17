package com.se104.passbookapp.utils

enum class Permission {
    VIEW_DASHBOARD,
    CREATE_ITEM,
    DELETE_USER,
    MANAGE_ROLES,
    EXPORT_REPORT
}


enum class Role(val permissions: Set<Permission>) {
    Admin(setOf(
        Permission.VIEW_DASHBOARD,
        Permission.CREATE_ITEM,
        Permission.DELETE_USER,
        Permission.MANAGE_ROLES,
        Permission.EXPORT_REPORT
    )),
    Staff(setOf(
        Permission.VIEW_DASHBOARD,
        Permission.CREATE_ITEM,
        Permission.EXPORT_REPORT
    )),
    Customer(setOf(
        Permission.VIEW_DASHBOARD
    )),

}

fun Role.hasPermission(p: Permission) = permissions.contains(p)