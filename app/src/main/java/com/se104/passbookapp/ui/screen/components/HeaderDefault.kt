package com.se104.passbookapp.ui.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.se104.passbookapp.ui.theme.button

@Composable
fun HeaderDefaultView(
    modifier: Modifier = Modifier,
    text: String,
    onBackIcon: ImageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
    onBack: (() -> Unit)? = null,
    icon: ImageVector? = null,
    iconClick: (() -> Unit)? = null,
    tintIcon: Color = MaterialTheme.colorScheme.primary,

    ) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) {
            IconCustomButton(
                onClick = onBack,
                icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                containerColor = Color.Transparent,
                iconColor = MaterialTheme.colorScheme.primary
            )
        } else {
            Spacer(modifier = Modifier.weight(0.2f))
        }

        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            color = tintIcon,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .weight(1f),
            textAlign = TextAlign.Center
        )

        if (icon != null && iconClick != null) {
          IconCustomButton(
              onClick = iconClick,
              icon = icon,

          )

        } else {
            Spacer(modifier = Modifier.weight(0.2f))
        }

    }

}

@Composable
fun IconCustomButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.button,
    icon: ImageVector,
    iconColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
    IconButton(
        onClick = {
            onClick.invoke()
        },
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = containerColor)
            .padding(4.dp),


        ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier
                .size(28.dp)
        )
    }
}