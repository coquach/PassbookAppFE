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
import androidx.compose.material.icons.filled.NotInterested
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.se104.passbookapp.navigation.CreateSavingTicket
import com.se104.passbookapp.ui.screen.components.HeaderDefaultView
import com.se104.passbookapp.ui.screen.components.LoadingAnimation
import com.se104.passbookapp.ui.screen.components.Nothing
import com.se104.passbookapp.ui.screen.components.Retry
import com.se104.passbookapp.utils.hasAllPermissions
import com.se104.passbookapp.utils.hasPermission

@Composable
fun SelectSavingTypeScreen(
    navController: NavController,
    viewModel: SelectSavingTypeViewModel = hiltViewModel(),
    permissions: List<String>
) {
    val isView by rememberSaveable { mutableStateOf(permissions.hasPermission("VIEW_ACTIVE_SAVINGTYPES"))}
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        viewModel.event.flowWithLifecycle(lifecycleOwner.lifecycle).collect {
            when (it) {
                is SelectSavingTypeState.Event.GoToDetail -> {
                        navController.navigate(CreateSavingTicket(it.savingType))
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
        if (isView){
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
        } else{
            Nothing(
                modifier = Modifier.fillMaxWidth().weight(1f),
                icon = Icons.Default.NotInterested,
                text = "Bạn không có quyền truy cập"
            )
        }


    }
}

@Composable
fun SavingTypeSection(
    savingType: SavingType,
    onItemClick: (SavingType) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraLarge)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp)
            .clickable {
                onItemClick(savingType)
            }
    ) {


            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                DetailsRow(
                    title = "Loại tiết kiệm",
                    text = savingType.typeName,
                    icon = Icons.Default.Tag,
                    titleColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.primary

                    )

                    DetailsRow(
                        text = if(savingType.duration==0) "Không có" else "${savingType.duration} tháng",
                        icon = Icons.Default.DateRange,
                        title = "Kỳ hạn",
                        titleColor = MaterialTheme.colorScheme.outline,
                        textColor = MaterialTheme.colorScheme.onBackground

                    )
                    DetailsRow(
                        icon = Icons.Default.MonetizationOn,
                        title = "Lãi suất",
                        text = "${savingType.interestRate}",
                        titleColor = MaterialTheme.colorScheme.outline,
                        textColor = MaterialTheme.colorScheme.onBackground

                        )

            }

    }
}