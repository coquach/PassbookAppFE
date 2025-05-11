package com.se104.passbookapp.ui.screen.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DialogSample(
    title: String,
    titleColor: Color = MaterialTheme.colorScheme.scrim,
    message: String,
    messageColor: Color = MaterialTheme.colorScheme.onBackground,
    onDismiss: () -> Unit,
    containerConfirmButtonColor: Color = MaterialTheme.colorScheme.error,
    labelConfirmButtonColor: Color = MaterialTheme.colorScheme.onError,
    onConfirm: (() -> Unit)? = null,
    confirmText: String = "Ok",
    dismissText: String = "Đóng",
    showConfirmButton: Boolean = true,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )
        },
        text = { Text(text = message, fontSize = 16.sp, color = messageColor, lineHeight = 24.sp) },
        containerColor = MaterialTheme.colorScheme.background,
        confirmButton = {
            if (showConfirmButton) {
                Button(
                    onClick = { onConfirm?.invoke(); onDismiss() },
                    colors = ButtonDefaults.buttonColors().copy(
                        containerColor = containerConfirmButtonColor,
                        contentColor = labelConfirmButtonColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(confirmText)
                }
            }
        },
        dismissButton = {
            Button(onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.outline
                ),
            ) {
                Text(dismissText)
            }
        }
    )
}