package com.se104.passbookapp.ui.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.ArrowBackIosNew
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
    text: String,
    onBackIcon: ImageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
    onBack: (() -> Unit)? = null,
    icon: ImageVector? = null,
    iconClick: (() -> Unit)? = null,
    tintIcon: Color = MaterialTheme.colorScheme.primary,

    ) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (onBack != null) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(40.dp) // size to rộng xíu cho dễ bấm
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = onBackIcon,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp) // icon nhỏ hơn để căn giữa trong button
                )
            }
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

        if (icon != null) {
            IconButton(
                onClick = { iconClick?.invoke() },
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(40.dp) // size to rộng xíu cho dễ bấm
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp),
                    tint = tintIcon

                )
            }

        } else {
            Spacer(modifier = Modifier.weight(0.2f))
        }

    }

}

@Composable
fun IconBackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.button,
    iconColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
    IconButton(
        onClick = {
            onClick.invoke()
        },
        modifier = modifier
            .size(40.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color = containerColor)
            .padding(4.dp),


        ) {
        Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier
                .size(20.dp)
        )
    }
}