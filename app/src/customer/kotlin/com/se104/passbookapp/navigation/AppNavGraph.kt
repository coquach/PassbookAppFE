package com.se104.passbookapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.se104.passbookapp.data.model.SavingType
import com.se104.passbookapp.ui.screen.auth.AuthScreen
import com.se104.passbookapp.ui.screen.auth.login.LoginScreen
import com.se104.passbookapp.ui.screen.auth.signup.SignUpScreen
import com.se104.passbookapp.ui.screen.home.HomeCustomerScreen
import com.se104.passbookapp.ui.screen.saving_ticket.SavingTicketScreen
import com.se104.passbookapp.ui.screen.saving_ticket.create.CreateSavingTicketsScreen
import com.se104.passbookapp.ui.screen.saving_ticket.create.SelectSavingTypeScreen
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
    paddingValues: PaddingValues
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
            composable<SignUp> {

                SignUpScreen(navController)
            }


            composable<Login> {
                LoginScreen(navController, isCustomer = true)
            }

            composable<Home> {

                HomeCustomerScreen(navController)
            }
            composable<SavingTicket> {
                SavingTicketScreen(navController)
            }
            composable<Transaction> {
                TransactionScreen(navController)
            }
            composable<SelectSavingType>{
                SelectSavingTypeScreen(navController)
            }

            composable<CreateSavingTicket>(
                typeMap =mapOf(typeOf<SavingType>() to savingTypeNavType)
            ) {
                CreateSavingTicketsScreen(navController)
            }

            composable<ActionSuccess> {
                ActionSuccessScreen(navController)
            }

            composable<Setting> {
                SettingScreen(navController, isDarkMode, onThemeUpdated)
            }
        }

}



