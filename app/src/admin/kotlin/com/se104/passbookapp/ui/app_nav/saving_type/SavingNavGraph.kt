package com.se104.passbookapp.ui.app_nav.saving_type

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.se104.passbookapp.ui.navigation.Auth
import com.se104.passbookapp.ui.navigation.Login
import com.se104.passbookapp.ui.navigation.SavingType
import com.se104.passbookapp.ui.navigation.SignUp
import com.se104.passbookapp.ui.screen.auth.AuthScreen
import com.se104.passbookapp.ui.screen.auth.login.LoginScreen
import com.se104.passbookapp.ui.screen.auth.signup.SignUpScreen

fun NavGraphBuilder.savingGraph(
    navController: NavHostController,
    shouldShowBottomNav: MutableState<Boolean>
) {
    composable<SavingType> {
        shouldShowBottomNav.value = true
        SavingTypeScreen(navController)
    }

}