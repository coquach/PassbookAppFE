package com.se104.passbookapp.ui.screen.components.text_field

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged

@Composable
fun ValidateTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    labelText: String?=null,
    errorMessage: String?,
    validate: () -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    maxLines: Int = 1,
){
    var isTouched by remember { mutableStateOf(false) }
    PassbookTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
            if (!isTouched) isTouched = true
        },
        labelText = labelText,
        isError = errorMessage != null,
        errorText = errorMessage,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                if (isTouched && !focusState.isFocused) {
                    validate
                }
            },
        keyboardOptions = keyboardOptions,
        singleLine = false,
        maxLines = maxLines
    )
}

fun validateField(
    value: String,
    errorMessage: String,
    condition: (String) -> Boolean
): String? {
    return if (condition(value)) null else errorMessage
}
