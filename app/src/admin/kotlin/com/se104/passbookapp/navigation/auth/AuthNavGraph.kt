package com.se104.passbookapp.navigation.auth

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.se104.passbookapp.navigation.Auth
import com.se104.passbookapp.navigation.Login
import com.se104.passbookapp.navigation.SignUp
import com.se104.passbookapp.ui.screen.auth.AuthScreen
import com.se104.passbookapp.ui.screen.auth.login.LoginScreen
import com.se104.passbookapp.ui.screen.auth.signup.SignUpScreen


fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    shouldShowBottomNav: MutableState<Boolean>
) {
    composable<Auth> {
        shouldShowBottomNav.value = false
        AuthScreen(navController)
    }
    composable<SignUp> {
        shouldShowBottomNav.value = false
        SignUpScreen(navController)
    }
    
    composable<Login> {
        shouldShowBottomNav.value = false
        LoginScreen(navController, isCustomer = true)
    }

}