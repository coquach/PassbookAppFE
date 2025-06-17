package com.se104.passbookapp.navigation

import com.se104.passbookapp.data.model.SavingType
import kotlinx.serialization.Serializable

interface NavRoute



@Serializable
object Welcome: NavRoute

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
object SavingType: NavRoute

@Serializable
object SavingTicket: NavRoute

@Serializable
object Transaction: NavRoute

@Serializable
object Notification: NavRoute

@Serializable
object SelectSavingType: NavRoute

@Serializable
data class CreateSavingTicket(val savingType: SavingType) : NavRoute

@Serializable
object ActionSuccess: NavRoute