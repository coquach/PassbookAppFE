package com.se104.passbookapp.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.se104.passbookapp.data.model.User
import com.se104.passbookapp.ui.screen.auth.AuthScreen
import com.se104.passbookapp.ui.screen.auth.login.LoginScreen
import com.se104.passbookapp.ui.screen.permission.PermissionsSettingScreen
import com.se104.passbookapp.ui.screen.profile.ProfileScreen
import com.se104.passbookapp.ui.screen.setting.SettingScreen
import com.se104.passbookapp.ui.screen.user_manage.UserManageScreen
import com.se104.passbookapp.ui.screen.user_manage.details.UserDetailsScreen
import kotlin.reflect.typeOf


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
            PermissionsSettingScreen(permissions = permissions)

        }


       composable<UserManage> {
           UserManageScreen(navController, permission = permissions)
       }

        composable<UserDetail>(
            typeMap =mapOf(typeOf<User>() to userNavType)
        ) {
            UserDetailsScreen(navController, permission= permissions)
        }

        composable<Setting> {
            SettingScreen(navController, isDarkMode, onThemeUpdated, permissions = permissions)
        }

        composable<Profile> {
            ProfileScreen(navController, permissions = permissions)
        }


    }

}



