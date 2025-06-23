package com.se104.passbookapp.ui.screen.components.text_field

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp


@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String?,
    validate: () -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLines: Int = 1,
){
    val focusManager = LocalFocusManager.current
    var showPassword by remember { mutableStateOf(false) }
    var isTouched by remember { mutableStateOf(false) }
    PassbookTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
            if (!isTouched) isTouched = true
        },
        labelText = label,
        isError = errorMessage != null,
        errorText = errorMessage,
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                if (isTouched && !focusState.isFocused) {
                    validate
                }
            },

        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {

            IconButton(
                onClick = {
                    showPassword = !showPassword
                },
            ) {
                if (!showPassword) {
                    Icon(
                       imageVector = Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        keyboardOptions = keyboardOptions,
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                validate()
            }
        ),
        singleLine = false,
        maxLines = maxLines
    )
}