package com.se104.passbookapp.ui.screen.components

import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComboBoxSample(
    modifier: Modifier = Modifier,
    title:  String?= null,
    textPlaceholder: String,
    selected: String?,
    onPositionSelected: (String?) -> Unit,
    options: List<String>,
    fieldHeight : Dp = 56.dp,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(IntSize.Zero) }
    LaunchedEffect(selected, options) {
        if (selected == null && options.isNotEmpty()) {
            onPositionSelected(options.first())
        }
    }


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {  expanded = !expanded }
    ) {


        OutlinedTextField(
            value = selected ?: "",
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = modifier
                .menuAnchor()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size
                },
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })

        ) {
            // Thêm mục rỗng
            DropdownMenuItem(
                text = { Text(textPlaceholder) },
                onClick = {
                    onPositionSelected(null)
                    expanded = false
                }
            )

            options.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onPositionSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}