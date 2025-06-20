package com.se104.passbookapp.navigation


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Straight
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable

sealed class BottomNavItem(val route: NavRoute, val icon: ImageVector) {
    data object Home : BottomNavItem(com.se104.passbookapp.navigation.Home, Icons.Default.Home)
    data object Setting :
        BottomNavItem(com.se104.passbookapp.navigation.Setting, Icons.Default.Settings)

    data object SavingType :
        BottomNavItem(com.se104.passbookapp.navigation.SavingType, Icons.Default.Savings)

    data object SavingTicket :
        BottomNavItem(com.se104.passbookapp.navigation.SavingTicket, Icons.Default.Savings)

    data object Transaction :
        BottomNavItem(com.se104.passbookapp.navigation.Transaction, Icons.Default.CurrencyExchange)

    data object UserManage :
           BottomNavItem(com.se104.passbookapp.navigation.UserManage, Icons.Default.People)

}

@Composable
fun BottomNavigationBar(navController: NavHostController, navItems: List<BottomNavItem>) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route

    var selectedIndex by rememberSaveable { mutableStateOf(0) }


    val matchedIndex = navItems.indexOfFirst { it.route::class.qualifiedName == currentRoute }
    if (matchedIndex >= 0 && matchedIndex != selectedIndex) {
        selectedIndex = matchedIndex
    }


    AnimatedNavigationBar(
        modifier = Modifier
            .height(80.dp)
            .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 16.dp),
        selectedIndex = selectedIndex,
        barColor = MaterialTheme.colorScheme.primary,
        ballColor = MaterialTheme.colorScheme.primary,
        cornerRadius = shapeCornerRadius(cornerRadius = 34.dp),
        ballAnimation = Straight(tween(durationMillis = 350, easing = FastOutSlowInEasing)),
        indentAnimation = Height(tween(durationMillis = 200, easing = LinearOutSlowInEasing))
    ) {
        navItems.forEachIndexed { index, item ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .noRippleClickable {


                        navController.navigate(item.route) {

                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }

                            launchSingleTop = true

                            restoreState = true
                        }


                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = "Bottom Bar Icon",
                    tint = if (selectedIndex == index) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.inversePrimary
                )
            }
        }
    }

}

fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }

}

@Composable
fun BottomBarWithCutoutFAB(
    navController: NavHostController,
    navItems: List<BottomNavItem>,
    state: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    fabSize: Dp = 56.dp,
    fabMargin: Dp = 16.dp,
    onFabClick: (() -> Unit)?=null,
) {

    val currentRoute =
        navController.currentBackStackEntryAsState().value?.destination
    AnimatedVisibility(
        visible = state.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            if(onFabClick!=null){
                // FloatingActionButton
                FloatingActionButton(
                    onClick = onFabClick,
                    shape = RoundedCornerShape(50),
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(y = (-fabSize / 2)) // Đẩy lên vừa nửa FAB
                ) {
                    Icon(Icons.Filled.PostAdd, contentDescription = "Add", tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(30.dp))
                }

                // BottomAppBar with Cutout
                CutoutBottomAppBar(
                    fabSize = fabSize,
                    fabMargin = fabMargin,
                    color = MaterialTheme.colorScheme.onPrimary
                ) {
                    NavigationBar(
                        containerColor = Color.Transparent,
                        tonalElevation = 0.dp
                    ) {

                        val itemsLeft = navItems.take(navItems.size / 2)
                        val itemsRight = navItems.takeLast(navItems.size / 2)
                        itemsLeft.forEach { item ->
                            val selected = currentRoute?.hierarchy?.any {
                                it.route == item.route::class.qualifiedName
                            } == true

                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    navController.navigate(item.route) {

                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }

                                        launchSingleTop = true

                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = null,
                                        tint = if (selected)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.inversePrimary
                                    )
                                }
                            )
                        }

                        // Spacer giữa để né FAB
                        Spacer(modifier = Modifier.width(fabSize + fabMargin * 2))

                        itemsRight.forEach { item ->
                            val selected = currentRoute?.hierarchy?.any {
                                it.route == item.route::class.qualifiedName
                            } == true

                            NavigationBarItem(
                                selected = selected,
                                onClick = {
                                    navController.navigate(item.route) {

                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }

                                        launchSingleTop = true

                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = null,
                                        tint = if (selected)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.inversePrimary
                                    )
                                }
                            )
                        }

                    }
                }
            }
            else{
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    tonalElevation = 0.dp
                ) {


                    navItems.forEach { item ->
                        val selected = currentRoute?.hierarchy?.any {
                            it.route == item.route::class.qualifiedName
                        } == true

                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {

                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }

                                    launchSingleTop = true

                                    restoreState = true
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = null,
                                    tint = if (selected)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.inversePrimary
                                )
                            }
                        )
                    }




                }
            }
        }
    }

}




class BottomAppBarCutoutShape(
    private val fabDiameter: Float,
    private val fabMargin: Dp,
) : Shape {


    override fun createOutline(
        size: androidx.compose.ui.geometry.Size,
        layoutDirection: androidx.compose.ui.unit.LayoutDirection,
        density: Density,
    ): Outline {
        val fabMarginPx = with(density) { fabMargin.toPx() }
        val notchRadius = fabDiameter / 2 + fabMarginPx
        val centerX = size.width / 2
        val notchDepth = 80f

        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(centerX - notchRadius, 0f)

            cubicTo(
                centerX - notchRadius * 0.66f, 0f,
                centerX - notchRadius * 0.66f, notchDepth,
                centerX, notchDepth
            )
            cubicTo(
                centerX + notchRadius * 0.66f, notchDepth,
                centerX + notchRadius * 0.66f, 0f,
                centerX + notchRadius, 0f
            )

            lineTo(size.width, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()

        }
        return Outline.Generic(path)
    }


}

@Composable
fun CutoutBottomAppBar(
    modifier: Modifier = Modifier,
    fabSize: Dp = 56.dp,
    fabMargin: Dp = 8.dp,
    color: Color = MaterialTheme.colorScheme.surfaceVariant,
    content: @Composable RowScope. () -> Unit,
) {
    val fabDiameterPx = with(LocalDensity.current) { fabSize.toPx() }

    val cutoutShape = BottomAppBarCutoutShape(
        fabDiameter = fabDiameterPx,
        fabMargin = fabMargin
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = cutoutShape,
        shadowElevation = 8.dp,
        color = color
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Absolute.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }

}


@Composable
fun bottomBarVisibility(
    navController: NavController,
): MutableState<Boolean> {

    val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    when (navBackStackEntry?.destination?.route) {
        Home::class.qualifiedName -> bottomBarState.value = true
        SavingTicket::class.qualifiedName -> bottomBarState.value = true
        SavingType::class.qualifiedName -> bottomBarState.value = true
        Setting::class.qualifiedName -> bottomBarState.value = true
        Transaction::class.qualifiedName -> bottomBarState.value = true
        else -> bottomBarState.value = false
    }

    return bottomBarState
}


fun Modifier.bottomBarAnimatedScroll(
    height: Dp = 108.dp,
    offsetHeightPx: MutableState<Float>
): Modifier = composed {
    val bottomBarHeightPx = with(LocalDensity.current) {
        height.roundToPx().toFloat()
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = offsetHeightPx.value + delta
                offsetHeightPx.value = newOffset.coerceIn(-bottomBarHeightPx, 0f)

                return Offset.Zero
            }
        }
    }

    this.nestedScroll(nestedScrollConnection)
}

