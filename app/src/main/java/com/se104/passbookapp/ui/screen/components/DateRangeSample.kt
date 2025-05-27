package com.example.foodapp.ui.screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.se104.passbookapp.ui.screen.components.FoodAppTextField
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerSample(
    startDateText: String = "Ngày bắt đầu",
    endDateText: String = "Ngày kết thúc",
    startDate: LocalDate?,
    endDate: LocalDate?,
    modifier: Modifier = Modifier.width(200.dp),
    fieldHeight: Dp = 56.dp,
    isColumn: Boolean = false, // Thêm tham số này
    onDateRangeSelected: (LocalDate?, LocalDate?) -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = startDate?.atStartOfDay(ZoneId.systemDefault())
            ?.toInstant()?.toEpochMilli(),
        initialSelectedEndDateMillis = endDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()
            ?.toEpochMilli()
    )
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Release) {
                showDialog = true
            }
        }
    }



    if (isColumn) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            DateFields(
                modifier,
                fieldHeight,
                startDateText,
                endDateText,
                startDate,
                endDate,
                interactionSource
            ) {
                showDialog = true
            }
        }
    } else {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DateFields(
                modifier,
                fieldHeight,
                startDateText,
                endDateText,
                startDate,
                endDate,
                interactionSource
            ) {
                showDialog = true
            }
        }
    }

    // Dialog chọn phạm vi ngày
    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    val pickedStartDate = dateRangePickerState.selectedStartDateMillis?.let {
                        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    val pickedEndDate = dateRangePickerState.selectedEndDateMillis?.let {
                        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    }

                    onDateRangeSelected(pickedStartDate, pickedEndDate)
                    showDialog = false
                }) {
                    Text("Xác nhận")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Hủy")
                }
            }
        ) {
            DateRangePicker(
                state = dateRangePickerState,
                title = {
                    Text(
                        text = "Chọn ngày",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 20.dp),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                headline = {
                    val startDate = dateRangePickerState.selectedStartDateMillis?.let {
                        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    val endDate = dateRangePickerState.selectedEndDateMillis?.let {
                        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
                    }

                    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = startDate?.format(dateFormatter) ?: "__/__/____",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(text = "  →  ", style = MaterialTheme.typography.bodyLarge)
                        Text(
                            text = endDate?.format(dateFormatter) ?: "__/__/____",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                },
            )
        }
    }
}

@Composable
private fun DateFields(
    modifier: Modifier,
    fieldHeight: Dp,
    startDateText: String,
    endDateText: String,
    startDate: LocalDate?,
    endDate: LocalDate?,
    interactionSource: MutableInteractionSource,
    onClick: () -> Unit,
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val startText = startDate?.format(formatter) ?: LocalDate.now().format(formatter)
    val endText = endDate?.format(formatter) ?: LocalDate.now().format(formatter)

    FoodAppTextField(
        value = startText,
        onValueChange = {},
        readOnly = true,
        labelText = startDateText,
        trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
        interactionSource = interactionSource,
        modifier = modifier.clickable { onClick() },
        fieldHeight = fieldHeight
    )

    FoodAppTextField(
        value = endText,
        onValueChange = {},
        readOnly = true,
        labelText = endDateText,
        trailingIcon = { Icon(Icons.Default.DateRange, contentDescription = null) },
        interactionSource = interactionSource,
        modifier = modifier.clickable { onClick() },
        fieldHeight = fieldHeight
    )
}