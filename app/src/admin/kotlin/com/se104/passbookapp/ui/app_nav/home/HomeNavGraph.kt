package com.se104.passbookapp.ui.app_nav.home

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.se104.passbookapp.ui.navigation.Auth
import com.se104.passbookapp.ui.navigation.Home
import com.se104.passbookapp.ui.navigation.Login
import com.se104.passbookapp.ui.navigation.SignUp
import com.se104.passbookapp.ui.screen.auth.AuthScreen
import com.se104.passbookapp.ui.screen.auth.login.LoginScreen
import com.se104.passbookapp.ui.screen.auth.signup.SignUpScreen
import com.se104.passbookapp.ui.screen.home.HomeScreen


fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    shouldShowBottomNav: MutableState<Boolean>
) {
    composable<Home> {
        shouldShowBottomNav.value = true
        HomeScreen(navController)
    }


}