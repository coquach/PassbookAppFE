package com.se104.passbookapp

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.se104.passbookapp.ui.app_nav.AppNavGraph
import com.se104.passbookapp.ui.navigation.Auth
import com.se104.passbookapp.ui.navigation.BottomNavItem
import com.se104.passbookapp.ui.navigation.BottomNavigationBar
import com.se104.passbookapp.ui.theme.PassBookAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : BasePassbookAppActivity() {
    private val splashViewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !splashViewModel.isLoading.value
            }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.4f,
                    0f
                )
                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.4f,
                    0f
                )
                zoomX.duration = 500
                zoomY.duration = 500
                zoomX.interpolator = OvershootInterpolator()
                zoomY.interpolator = OvershootInterpolator()
                zoomX.doOnEnd {
                    screen.remove()
                }
                zoomY.doOnEnd {
                    screen.remove()
                }
                zoomY.start()
                zoomX.start()
            }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val darkMode = isSystemInDarkTheme()
            var isDarkMode by remember { mutableStateOf(darkMode) }

            val screen by splashViewModel.startDestination

            PassBookAppTheme(darkTheme = isDarkMode) {
                val navItems = listOf(
                    BottomNavItem.Home,
                    BottomNavItem.Setting
                )
                val navController = rememberNavController()
                val shouldShowBottomNav = remember {
                    mutableStateOf(true)
                }
                LaunchedEffect(Unit) {
                    splashViewModel.navigateEventFlow.collectLatest { event ->
                        when (event) {
                            is SplashViewModel.UiEvent.NavigateToAuth -> {
                                navController.navigate(Auth) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                    }
                }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    bottomBar = {
                        AnimatedVisibility(visible = shouldShowBottomNav.value) {
                            BottomNavigationBar(navController, navItems)
                        }

                    }

                ) { innerPadding ->


                        AppNavGraph(
                            navController = navController,
                            innerPadding = innerPadding,
                            shouldShowBottomNav = shouldShowBottomNav,
                            startDestination = screen,
                            isDarkMode = isDarkMode,
                            onThemeUpdated = {
                                isDarkMode = !isDarkMode
                            },

                        )

                }
            }
        }
    }
}

