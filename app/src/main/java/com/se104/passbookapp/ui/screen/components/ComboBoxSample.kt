package com.se104.passbookapp.ui.screen.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.se104.passbookapp.ui.screen.components.text_field.PassbookTextField
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComboBoxSample(
    modifier: Modifier = Modifier,
    title:  String?= null,
    textPlaceholder: String,
    search: String,
    onSearch: (String) -> Unit,
    onPositionSelected: (String?) -> Unit,
    options: List<String>,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val searchFocusRequester = remember { FocusRequester() }

    LaunchedEffect(expanded) {
        if (expanded) {
            delay(100)
            searchFocusRequester.requestFocus()
        }
    }



    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = !expanded }
    ) {
        PassbookTextField(
            value = search,
            onValueChange = {
                onSearch(it)
            },
            labelText = title,

            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryEditable, true)
                .focusRequester(searchFocusRequester)

        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.height(300.dp),


        ) {
            // Thêm mục rỗng
            if(options.isEmpty()){
                DropdownMenuItem(
                    text = { Text(text =textPlaceholder, modifier = Modifier.fillMaxSize().align(Alignment.CenterHorizontally), textAlign = TextAlign.Center) },
                    onClick = {
                        onPositionSelected(null)
                        expanded = false
                    }
                )
            }else{
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
}

@Composable
fun  CustomPagingDropdown(
    modifier: Modifier = Modifier,
    title: String,
    textPlaceholder: String,
    search: String,
    onSearch: (String) -> Unit,
    enabled: Boolean = true,
    dropdownContent: @Composable (onDismissDropdown: () -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val searchFocusRequester = remember { FocusRequester() }

    LaunchedEffect(expanded) {
        if (expanded) {
            delay(100)
            searchFocusRequester.requestFocus()
        }
    }


    Box(modifier) {
        PassbookTextField(
            value = search,
            onValueChange = {
                onSearch(it)
            },
            placeholder = {
                Text(text = textPlaceholder)
            },
            labelText = title,
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { if(enabled) expanded = !expanded }
                .focusRequester(searchFocusRequester)
        )
        Log.d("CustomPagingDropdown", "expanded: $expanded")
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .width(320.dp)
        ) {
            DropdownMenuItem(
                text = { Text(textPlaceholder) },
                onClick = {

                }
            )
            Column(
                modifier = Modifier.height(300.dp)
            ){
                dropdownContent{
                    expanded = false

                }
            }
        }
    }
}

