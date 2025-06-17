package com.se104.passbookapp.ui.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    searchInput: String,
    searchChange: (String) -> Unit,
    switchState: Boolean,
    switchChange: (Boolean) -> Unit,
    filterChange: (String) -> Unit,
    filters: List<String>,
    filterSelected: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        FoodAppTextField(
            value = searchInput,
            onValueChange = { searchChange(it) },
            placeholder = {
                Text(text = "Tìm kiếm ở đây", color = MaterialTheme.colorScheme.outline)
            },
            modifier = Modifier
                .weight(1f),
            singleLine = true,
            maxLines = 1,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    tint = MaterialTheme.colorScheme.outline,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors().copy(
                unfocusedContainerColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f),
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )
        )
        var expanded by remember { mutableStateOf(false) }
        var isSwitchOn by remember { mutableStateOf(switchState) }
        Box() {
            IconButton(
                onClick = {
                    expanded = !expanded
                },
                modifier = Modifier
                    .size(50.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(16.dp)
                    ),
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(220.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            ) {
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Thứ tự",
                                color = MaterialTheme.colorScheme.outline,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.ExtraBold,

                            )
                            Switch(
                                checked = isSwitchOn,
                                onCheckedChange = {
                                    isSwitchOn = it
                                    switchChange(it)
                                },
                                thumbContent = {
                                    if (isSwitchOn) {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDownward,
                                            contentDescription = null,
                                            modifier = Modifier.size(12.dp)
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.ArrowUpward,
                                            contentDescription = null,
                                            modifier = Modifier.size(12.dp)
                                        )
                                    }
                                }
                            )
                        }
                    },
                    onClick = {

                    }
                )
                DropdownMenuItem(
                    text = {
                       ChipsGroupWrap(
                           modifier = Modifier.padding(3.dp),
                           text = "Sắp xếp",
                           options = filters,
                           selectedOption = filterSelected,
                           onOptionSelected = {
                               filterChange(it)
                           },
                           containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                       )
                    },
                    onClick = {

                    }
                )

            }
        }


    }


}

@Composable
fun MultipleCheckboxList() {
    val items = listOf("Option 1", "Option 2", "Option 3")
    val checkedStates = remember { mutableStateListOf(false, false, false) }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        items.forEachIndexed { index, item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { checkedStates[index] = !checkedStates[index] }
                    .padding(8.dp)
            ) {
                Checkbox(
                    checked = checkedStates[index],
                    onCheckedChange = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(item)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MultipleCheckboxListPreview() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        MultipleCheckboxList()
    }

}






