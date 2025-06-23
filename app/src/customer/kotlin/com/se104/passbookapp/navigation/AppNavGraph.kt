package com.se104.passbookapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.se104.passbookapp.data.model.SavingType
import com.se104.passbookapp.ui.screen.auth.AuthScreen
import com.se104.passbookapp.ui.screen.auth.change_password.ChangePasswordScreen
import com.se104.passbookapp.ui.screen.auth.forgot_password.ForgotPasswordScreen
import com.se104.passbookapp.ui.screen.auth.forgot_password.SendEmailSuccessScreen
import com.se104.passbookapp.ui.screen.auth.login.LoginScreen
import com.se104.passbookapp.ui.screen.auth.signup.SignUpScreen
import com.se104.passbookapp.ui.screen.auth.verify_email.SendEmailScreen
import com.se104.passbookapp.ui.screen.auth.verify_email.VerifyEmailScreen
import com.se104.passbookapp.ui.screen.home.HomeScreen
import com.se104.passbookapp.ui.screen.parameters.ParametersScreen
import com.se104.passbookapp.ui.screen.profile.ProfileScreen
import com.se104.passbookapp.ui.screen.report.ReportScreen
import com.se104.passbookapp.ui.screen.saving_ticket.SavingTicketScreen
import com.se104.passbookapp.ui.screen.saving_ticket.create.CreateSavingTicketsScreen
import com.se104.passbookapp.ui.screen.saving_ticket.create.SelectSavingTypeScreen
import com.se104.passbookapp.ui.screen.saving_ticket.details.SavingTicketDetailsScreen
import com.se104.passbookapp.ui.screen.saving_type.SavingTypeScreen
import com.se104.passbookapp.ui.screen.setting.SettingScreen
import com.se104.passbookapp.ui.screen.success.ActionSuccessScreen
import com.se104.passbookapp.ui.screen.transaction.TransactionScreen
import com.se104.passbookapp.ui.screen.welcome.WelcomeScreen
import kotlin.reflect.typeOf


@Composable
fun AppNavGraph(
    navController: NavHostController,
    isDarkMode: Boolean,
    startDestination: NavRoute,
    onThemeUpdated: () -> Unit,
    paddingValues: PaddingValues,
    permissions: List<String>

) {
        PassbookAppNavHost (
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues),
        ) {

            composable<Welcome> {
                WelcomeScreen(navController)
            }
            composable<Auth> {
                AuthScreen(navController)
            }
            composable<SendEmail> {
                SendEmailScreen(navController)
            }
            composable<VerifyEmail> {
                VerifyEmailScreen(navController)
            }
            composable<SignUp> {

                SignUpScreen(navController)
            }



            composable<Login> {
                LoginScreen(navController)
            }

            composable<ForgotPassword> {
                ForgotPasswordScreen(navController)
            }
            composable<SendEmailSuccess> {
                SendEmailSuccessScreen(navController)
            }

            composable<ChangePassword> {
                ChangePasswordScreen(navController)
            }
            composable<ResetPasswordSuccess> {
                SendEmailSuccessScreen(navController)
            }
            

            composable<Home> {

                HomeScreen(navController, permissions= permissions)
            }
            composable<SavingTicket> {
                SavingTicketScreen(navController, permissions = permissions)
            }

            composable<com.se104.passbookapp.navigation.SavingType> {
                SavingTypeScreen(navController,permissions = permissions)
            }

            composable<Parameters> {
                ParametersScreen(navController, permissions = permissions)
            }

            composable<SavingTicketDetails>(
                typeMap =mapOf(typeOf<com.se104.passbookapp.data.model.SavingTicket>() to savingTicketNavType)
            ) {
                SavingTicketDetailsScreen(navController, permissions = permissions)
            }
            composable<Transaction> {
                TransactionScreen(permissions = permissions)
            }
            composable<SelectSavingType>{
                SelectSavingTypeScreen(navController, permissions = permissions)
            }

            composable<CreateSavingTicket>(
                typeMap =mapOf(typeOf<SavingType>() to savingTypeNavType)
            ) {
                CreateSavingTicketsScreen(navController, permissions = permissions)
            }

            composable<ActionSuccess> {
                ActionSuccessScreen(navController)
            }

            composable<Setting> {
                SettingScreen(navController, isDarkMode, onThemeUpdated, permissions = permissions)
            }

            composable<Profile> {
                ProfileScreen(navController, permissions = permissions)
            }

            composable<Report> {
                ReportScreen(navController)
            }
        }

}



