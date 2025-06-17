package com.se104.passbookapp.ui.screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChipsGroupWrap(
    modifier: Modifier = Modifier,
    text: String?= null,
    options: List<String>,
    selectedOption: String?= null,
    onOptionSelected: (String) -> Unit,
    thresholdExpend: Int = 8,
    containerColor: Color = MaterialTheme.colorScheme.outline,
    isFlowLayout: Boolean = true,
    shouldSelectDefaultOption: Boolean = true
) {
    LaunchedEffect(options, selectedOption) {
        if (shouldSelectDefaultOption && options.isNotEmpty() && selectedOption == null ) {
            onOptionSelected(options.first())
        }
    }
    var isExpanded by remember { mutableStateOf(false) }


    Column(modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        text?.let{
            Text(
                text = text,
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.ExtraBold,

            )
        }




        val layoutModifier = Modifier
            .fillMaxWidth()


        if (isFlowLayout) {
            FlowRow(
                modifier = layoutModifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                options.take(if (isExpanded) options.size else thresholdExpend)
                    .forEach { optionText ->
                        val isSelected = optionText == selectedOption
                        FilterChip(
                            selected = isSelected,
                            onClick = { onOptionSelected(optionText) },
                            label = { Text(text = optionText, modifier = modifier) },
                            colors = FilterChipDefaults.filterChipColors().copy(
                                labelColor = MaterialTheme.colorScheme.onPrimary,
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                containerColor = containerColor
                            ),
                            border = BorderStroke(0.dp, Color.Transparent),
                            modifier = Modifier.padding(2.dp)
                        )
                    }

                if (!isExpanded && options.size > thresholdExpend) {
                    IconButton(
                        onClick = { isExpanded = true },
                        modifier = Modifier.size(48.dp).clip(CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreHoriz,
                            contentDescription = "Xem thêm",
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        } else {
            LazyRow(
                modifier = layoutModifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(items = options, key = { it }) { optionText ->
                    val isSelected = optionText == selectedOption
                    FilterChip(
                        selected = isSelected,
                        onClick = { onOptionSelected(optionText) },
                        label = { Text(text = optionText, modifier = Modifier.padding(8.dp)) },
                        colors = FilterChipDefaults.filterChipColors().copy(
                            labelColor = MaterialTheme.colorScheme.onPrimary,
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = containerColor
                        ),
                        border = BorderStroke(0.dp, Color.Transparent),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }



    }
}

