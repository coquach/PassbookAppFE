package com.se104.passbookapp.ui.navigation

import kotlinx.serialization.Serializable

interface NavRoute

@Serializable
object Auth : NavRoute

@Serializable
object SignUp : NavRoute

@Serializable
object Login : NavRoute

@Serializable
object Home : NavRoute

@Serializable
object Setting: NavRoute

@Serializable
object Welcome: NavRoute