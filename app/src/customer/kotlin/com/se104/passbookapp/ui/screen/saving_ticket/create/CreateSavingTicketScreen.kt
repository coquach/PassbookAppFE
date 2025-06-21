package com.se104.passbookapp.ui.screen.saving_ticket.create

import DetailsRow
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.se104.passbookapp.data.model.User
import com.se104.passbookapp.navigation.ActionSuccess
import com.se104.passbookapp.ui.screen.components.AppButton
import com.se104.passbookapp.ui.screen.components.ComboBoxSample
import com.se104.passbookapp.ui.screen.components.CustomPagingDropdown
import com.se104.passbookapp.ui.screen.components.ErrorModalBottomSheet
import com.se104.passbookapp.ui.screen.components.HeaderDefaultView
import com.se104.passbookapp.ui.screen.components.LazyPagingSample
import com.se104.passbookapp.ui.screen.components.text_field.PassbookTextField
import com.se104.passbookapp.utils.StringUtils
import com.se104.passbookapp.utils.hasAllPermissions
import com.se104.passbookapp.utils.hasPermission
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSavingTicketsScreen(
    navController: NavController,
    viewModel: CreateSavingTicketViewModel = hiltViewModel(),
    permissions: List<String>,
) {

    val isStaff by remember(permissions) { mutableStateOf(permissions.hasAllPermissions("VIEW_USERS")) }
    val isCreate by remember(permissions) { mutableStateOf(permissions.hasPermission("CREATE_SAVINGTICKET")) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showErrorSheet by remember { mutableStateOf(false) }


    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        viewModel.event.flowWithLifecycle(lifecycleOwner.lifecycle).collect { event ->
            when (event) {
                CreateSavingTicketState.Event.OnBack -> {
                    navController.popBackStack()
                }

                CreateSavingTicketState.Event.ShowError -> {
                    showErrorSheet = true
                }

                CreateSavingTicketState.Event.NavigateToActionSuccess -> {
                    navController.navigate(ActionSuccess)
                }

            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderDefaultView(
            text = "Tạo phiếu",
            onBack = {
                viewModel.onAction(CreateSavingTicketState.Action.OnBack)
            }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.extraLarge)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DetailsRow(
                icon = Icons.Default.Category,
                title = "Loại tiết kiệm",
                text = uiState.savingType.typeName,
                titleColor = MaterialTheme.colorScheme.outline,
                textColor = MaterialTheme.colorScheme.onBackground
            )
            DetailsRow(
                icon = Icons.Default.DateRange,
                title = "Kỳ hạn",
                text = "${uiState.savingType.duration} tháng",
                titleColor = MaterialTheme.colorScheme.outline,
                textColor = MaterialTheme.colorScheme.onBackground
            )
            DetailsRow(
                icon = Icons.Default.MonetizationOn,
                title = "Lãi suất",
                text = "${uiState.savingType.interestRate}",
                titleColor = MaterialTheme.colorScheme.outline,
                textColor = MaterialTheme.colorScheme.onBackground

            )
            DetailsRow(
                title = "Ngày đáo hạn",
                icon = Icons.Default.Timer,
                text = "${
                    StringUtils.formatLocalDate(
                        LocalDate.now().plusMonths(uiState.savingType.duration.toLong())
                    )
                }",
                titleColor = MaterialTheme.colorScheme.outline,
                textColor = MaterialTheme.colorScheme.onBackground
            )
            if (isStaff) {
                PassbookTextField(
                    value = uiState.citizenID,
                    onValueChange = {
                        viewModel.onAction(CreateSavingTicketState.Action.OnCitizenIDSearch(it))
                    },
                    modifier = Modifier.fillMaxWidth(),
                    labelText = "Khách hàng",
                    placeholder = {
                        Text(text = "Nhập mã số CMND")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                )

            } else {
                viewModel.getCitizenId()
            }

            PassbookTextField(
                labelText = "Số tiền gửi",
                value = uiState.amount.toPlainString(),
                onValueChange = {
                    viewModel.onAction(CreateSavingTicketState.Action.OnAmountChange(it.toBigDecimalOrNull()))
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.MonetizationOn,
                        tint = MaterialTheme.colorScheme.outline,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                },
                placeholder = {
                    Text(
                        text = "50.0000",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),

                )
        }


        Spacer(modifier = Modifier.weight(1f))

        AppButton(
            onClick = {
                viewModel.onAction(CreateSavingTicketState.Action.OnCreate)
            },
            modifier = Modifier
                .fillMaxWidth(),
            text = "Xác nhận",
            backgroundColor = MaterialTheme.colorScheme.primary,
            enable = isCreate && uiState.citizenID.isNotBlank() && !uiState.isLoading
        )


    }
    if (showErrorSheet) {
        ErrorModalBottomSheet(
            description = uiState.errorMessage.toString(),
            onDismiss = {
                showErrorSheet = false
            },

            )
    }

}

