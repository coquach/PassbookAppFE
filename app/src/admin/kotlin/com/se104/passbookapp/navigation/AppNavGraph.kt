package com.se104.passbookapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.se104.passbookapp.ui.screen.auth.AuthScreen
import com.se104.passbookapp.ui.screen.auth.login.LoginScreen
import com.se104.passbookapp.ui.screen.parameters.ParametersScreen
import com.se104.passbookapp.ui.screen.saving_type.SavingTypeScreen
import com.se104.passbookapp.ui.screen.setting.SettingScreen


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


        composable<Auth> {
            AuthScreen(navController, false)
        }



        composable<Login> {
            LoginScreen(navController, isCustomer = false)
        }

        composable<Home> {


        }
        composable<SavingType> {
            SavingTypeScreen(permissions = permissions)
        }

        composable<Parameters>{
            ParametersScreen(navController, permissions = permissions)
        }

        composable<Setting> {
            SettingScreen(navController, isDarkMode, onThemeUpdated, permissions = permissions)
        }


    }

}



