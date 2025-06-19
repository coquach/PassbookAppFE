package com.se104.passbookapp.navigation

import com.se104.passbookapp.data.model.SavingTicket
import com.se104.passbookapp.data.model.SavingType
import com.se104.passbookapp.data.model.User
import kotlinx.serialization.Serializable

interface NavRoute



@Serializable
object Welcome: NavRoute

@Serializable
object Auth : NavRoute

@Serializable
data class SignUp(val email: String) : NavRoute

@Serializable
object Login : NavRoute


@Serializable
object SendEmail : NavRoute

@Serializable
data class VerifyEmail(val email: String) : NavRoute

@Serializable
object ForgotPassword : NavRoute

@Serializable
object ChangePassword : NavRoute



@Serializable
object Home : NavRoute

@Serializable
object Setting: NavRoute



@Serializable
object SavingType: NavRoute

@Serializable
object SavingTicket: NavRoute

@Serializable
data class SavingTicketDetails(val savingTicket: SavingTicket) : NavRoute

@Serializable
object Transaction: NavRoute

@Serializable
object Profile: NavRoute

@Serializable
object SelectSavingType: NavRoute

@Serializable
data class CreateSavingTicket(val savingType: SavingType) : NavRoute

@Serializable
object ActionSuccess: NavRoute

@Serializable
object Report: NavRoute

@Serializable
object Parameters: NavRoute

@Serializable
object UserManage: NavRoute

@Serializable
data class UserDetail(val user: User): NavRoute