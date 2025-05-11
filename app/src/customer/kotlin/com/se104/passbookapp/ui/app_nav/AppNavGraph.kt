package com.se104.passbookapp.ui.app_nav

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

import com.se104.passbookapp.ui.app_nav.auth.authGraph
import com.se104.passbookapp.ui.app_nav.home.homeGraph

import com.se104.passbookapp.ui.navigation.NavRoute
import com.se104.passbookapp.ui.navigation.PassbookAppNavHost


@Composable
fun AppNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    shouldShowBottomNav: MutableState<Boolean>,
    startDestination: NavRoute,
    isDarkMode: Boolean,
    onThemeUpdated: () -> Unit,
) {
        PassbookAppNavHost (
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            authGraph(navController, shouldShowBottomNav)
            homeGraph(navController, shouldShowBottomNav)

        }

}


