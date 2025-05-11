package com.se104.passbookapp.ui.screen.components


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.se104.passbookapp.ui.theme.button

@Composable
fun AppButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    backgroundColor: Color = MaterialTheme.colorScheme.button,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    shape: Shape = MaterialTheme.shapes.small ,
    padding: PaddingValues = PaddingValues(vertical = 18.dp, horizontal = 24.dp),
    enable: Boolean = true
) {
    Button (
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = shape,
        contentPadding = padding,
        enabled = enable
    ) {
        Text(text = text, color = textColor, style = MaterialTheme.typography.labelLarge)
    }
}