package com.se104.passbookapp.navigation.home

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.se104.passbookapp.navigation.Home
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