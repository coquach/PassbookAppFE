package com.se104.passbookapp.navigation.saving_ticket

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.se104.passbookapp.navigation.SavingType
import com.se104.passbookapp.ui.screen.saving_ticket.SavingTicketScreen


fun NavGraphBuilder.savingTicketGraph(
    navController: NavHostController,
    shouldShowBottomNav: MutableState<Boolean>
) {
    composable<SavingType> {
        shouldShowBottomNav.value = true
        SavingTicketScreen(navController)
    }

}