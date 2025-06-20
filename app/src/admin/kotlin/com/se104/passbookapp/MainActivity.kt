package com.se104.passbookapp

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.se104.passbookapp.navigation.AppNavGraph
import com.se104.passbookapp.navigation.Auth
import com.se104.passbookapp.navigation.BottomBarWithCutoutFAB
import com.se104.passbookapp.navigation.BottomNavItem
import com.se104.passbookapp.navigation.BottomNavigationBar
import com.se104.passbookapp.navigation.SelectSavingType
import com.se104.passbookapp.navigation.bottomBarAnimatedScroll
import com.se104.passbookapp.navigation.bottomBarVisibility
import com.se104.passbookapp.ui.screen.components.LoadingAnimation
import com.se104.passbookapp.ui.theme.PassBookAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainActivity : BasePassbookAppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val darkMode = isSystemInDarkTheme()
            var isDarkMode by remember { mutableStateOf(darkMode) }
            val navController = rememberNavController()
            val screen = viewModel.startDestination.value
            val permissions by viewModel.permissions.collectAsStateWithLifecycle()

            PassBookAppTheme(darkTheme = isDarkMode) {


                LaunchedEffect(Unit) {
                    viewModel.navigateEventFlow.collectLatest { event ->
                        when (event) {
                            is MainViewModel.UiEvent.NavigateToAuth -> {
                                navController.navigate(Auth) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        }
                    }
                }




                    if (screen == null) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            LoadingAnimation()
                        }
                    } else {
                        val navItems = listOf(
                            BottomNavItem.Home,
                            BottomNavItem.SavingType,
                            BottomNavItem.UserManage,
                            BottomNavItem.Setting,
                        )
                        val bottomBarOffsetHeightPx = remember { mutableFloatStateOf(0f) }
                        Scaffold(
                            modifier = Modifier.bottomBarAnimatedScroll(
                                height = 80.dp,
                                offsetHeightPx = bottomBarOffsetHeightPx
                            ),
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            bottomBar = {
                                BottomBarWithCutoutFAB(
                                    navController = navController,
                                    state = bottomBarVisibility(navController),
                                    modifier = Modifier
                                        .offset {
                                            IntOffset(
                                                x = 0,
                                                y = -bottomBarOffsetHeightPx.floatValue.roundToInt()
                                            )
                                        },
                                    navItems = navItems,
                                )
                            }
                        ) { paddingValues ->
                            AppNavGraph(
                                startDestination = screen,
                                isDarkMode = isDarkMode,
                                onThemeUpdated = {
                                    isDarkMode = !isDarkMode
                                },
                                navController = navController,
                                paddingValues = paddingValues,
                                permissions = permissions
                            )
                        }
                    }



            }
        }
    }
}

