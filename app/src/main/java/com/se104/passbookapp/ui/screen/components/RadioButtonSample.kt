package com.se104.passbookapp.ui.screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RadioGroupWrap(
    modifier: Modifier = Modifier,
    text: String?=null,

    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    optionIcons: List<ImageVector>? = null,
    useFlowLayout: Boolean = true // true = FlowLayout, false = Column
) {
    LaunchedEffect(options, selectedOption) {
        if (options.isNotEmpty() && selectedOption.isBlank()) {
            onOptionSelected(options.first())
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        text?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }


        val layoutModifier = modifier
            .fillMaxWidth()
            .selectableGroup()

        val optionLayout: @Composable (@Composable () -> Unit) -> Unit =
            if (useFlowLayout) {
                { content ->
                    FlowRow(
                        modifier = layoutModifier,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        content = { content() }
                    )
                }
            } else {
                { content ->
                    Column(
                        modifier = layoutModifier,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        content = { content() }
                    )
                }
            }

        optionLayout {
            options.forEachIndexed { index, optionText ->
                val icon = optionIcons?.getOrNull(index)

                Row(
                    modifier = Modifier
                        .selectable(
                            selected = (optionText == selectedOption),
                            onClick = { onOptionSelected(optionText) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (optionText == selectedOption),
                        onClick = null,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.primary,
                            unselectedColor = MaterialTheme.colorScheme.outline
                        )
                    )
                    if (icon != null) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                    Text(
                        text = optionText,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
            }
        }
    }
}
