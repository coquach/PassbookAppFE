package com.se104.passbookapp.ui.screen.components

import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun MyFloatingActionButton(
    onClick: () -> Unit,
    bgColor: Color,
    content: @Composable () -> Unit,
) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = bgColor,
    ) {
        content()
    }
}