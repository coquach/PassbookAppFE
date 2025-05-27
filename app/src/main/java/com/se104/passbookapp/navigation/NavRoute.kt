package com.se104.passbookapp.navigation

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

@Serializable
object SavingType: NavRoute

@Serializable
object SavingTicket: NavRoute

@Serializable
object Transaction: NavRoute