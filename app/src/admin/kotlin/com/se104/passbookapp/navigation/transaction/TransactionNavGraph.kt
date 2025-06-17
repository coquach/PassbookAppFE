package com.se104.passbookapp.navigation.transaction

import androidx.compose.runtime.MutableState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.se104.passbookapp.navigation.SavingType

import com.se104.passbookapp.ui.screen.transaction.TransactionScreen

fun NavGraphBuilder.transactionGraph(
    navController: NavHostController,
    shouldShowBottomNav: MutableState<Boolean>
) {
    composable<SavingType> {
        shouldShowBottomNav.value = true
        TransactionScreen(navController)
    }

}