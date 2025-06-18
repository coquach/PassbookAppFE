package com.se104.passbookapp.ui.screen.saving_ticket.create

import DetailsRow
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.se104.passbookapp.data.model.SavingType
import com.se104.passbookapp.ui.screen.components.HeaderDefaultView
import com.se104.passbookapp.ui.screen.components.LoadingAnimation
import com.se104.passbookapp.ui.screen.components.Retry

@Composable
fun SelectSavingTypeScreen(
    navController: NavController,
    viewModel: SelectSavingTypeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        viewModel.event.flowWithLifecycle(lifecycleOwner.lifecycle).collect {
            when (it) {
                is SelectSavingTypeState.Event.GoToDetail -> {

                }
                SelectSavingTypeState.Event.OnBack -> {
                    navController.popBackStack()
                }
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderDefaultView(
            text = "Chọn loại tiết kiệm",
            onBack = {
                viewModel.onAction(SelectSavingTypeState.Action.OnBack)
            }
        )
        when(uiState.savingTypesState){
            is SelectSavingTypeState.SavingTypes.Loading -> {
                LoadingAnimation(
                    modifier = Modifier.fillMaxWidth().weight(1f)
                )
            }
            is SelectSavingTypeState.SavingTypes.Error -> {
                val message = (uiState.savingTypesState as SelectSavingTypeState.SavingTypes.Error).message
                Retry(
                    message = message,
                    onClicked = {
                        viewModel.getSavingTypes()
                    },
                    modifier = Modifier.fillMaxWidth().weight(1f)
                )
            }
            is SelectSavingTypeState.SavingTypes.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth().weight(1f),

                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = {
                        items(items= uiState.savingTypes, key = { it.id!!}) {
                            SavingTypeSection(
                                savingType = it,
                                onItemClick = {
                                    viewModel.onAction(SelectSavingTypeState.Action.OnSelectSavingType(it))
                                })
                        }
                    }
                )
            }

        }

    }
}

@Composable
fun SavingTypeSection(
    savingType: SavingType,
    onItemClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp)
            .clickable {
                onItemClick
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)

        ) {
            Icon(
                imageVector = Icons.Default.Savings,
                contentDescription = "Saving Type",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(30.dp)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = savingType.typeName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DetailsRow(
                        text = "${savingType.duration} tháng",
                        icon = Icons.Default.DateRange,
                        title = "Kỳ hạn",

                    )
                    DetailsRow(
                        icon = Icons.Default.MonetizationOn,
                        title = "Lãi suất",
                        text = "${savingType.interestRate}%",

                        )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Click",
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(30.dp)
            )

        }
    }
}