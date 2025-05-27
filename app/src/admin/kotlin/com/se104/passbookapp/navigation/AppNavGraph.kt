package com.se104.passbookapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.se104.passbookapp.navigation.auth.authGraph
import com.se104.passbookapp.navigation.home.homeGraph
import com.se104.passbookapp.navigation.saving_ticket.savingTicketGraph
import com.se104.passbookapp.navigation.saving_type.savingTypeGraph
import com.se104.passbookapp.navigation.transaction.transactionGraph


@Composable
fun AppNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    shouldShowBottomNav: MutableState<Boolean>,
    startDestination: NavRoute,
    isDarkMode: Boolean,
    onThemeUpdated: () -> Unit,
) {
    PassbookAppNavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(innerPadding)
    ) {
        authGraph(navController, shouldShowBottomNav)
        homeGraph(navController, shouldShowBottomNav)
        savingTypeGraph(navController, shouldShowBottomNav)
        savingTicketGraph(navController, shouldShowBottomNav)
        transactionGraph(navController, shouldShowBottomNav)
    }

}


