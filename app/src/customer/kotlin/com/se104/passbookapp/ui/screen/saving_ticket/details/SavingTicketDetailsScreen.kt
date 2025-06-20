package com.se104.passbookapp.ui.screen.saving_ticket.details

import DetailsRow
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.se104.passbookapp.data.model.WithdrawalTicket
import com.se104.passbookapp.navigation.ActionSuccess
import com.se104.passbookapp.ui.screen.components.AppButton
import com.se104.passbookapp.ui.screen.components.CustomBottomSheet
import com.se104.passbookapp.ui.screen.components.ErrorModalBottomSheet
import com.se104.passbookapp.ui.screen.components.HeaderDefaultView
import com.se104.passbookapp.ui.screen.components.Nothing
import com.se104.passbookapp.ui.screen.components.text_field.PassbookTextField
import com.se104.passbookapp.utils.StringUtils
import com.se104.passbookapp.utils.hasPermission
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingTicketDetailsScreen(
    navController: NavController,
    viewModel: SavingTicketDetailsViewModel = hiltViewModel(),
    permissions: List<String>,
) {
    val isStaff by rememberSaveable { mutableStateOf(permissions.hasPermission("VIEW_USERS")) }
    val isWithDraw by rememberSaveable { mutableStateOf(permissions.hasPermission("CREATE_WITHDRAWALTICKET")) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showErrorSheet by rememberSaveable { mutableStateOf(false) }
    var showWithDrawSheet by rememberSaveable { mutableStateOf(false) }


    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        viewModel.event.flowWithLifecycle(lifecycleOwner.lifecycle).collect { event ->
            when (event) {
                is SavingTicketDetailsState.Event.Withdraw -> {
                    showWithDrawSheet = true
                }

                is SavingTicketDetailsState.Event.ShowError -> {
                    showErrorSheet = true
                }

                SavingTicketDetailsState.Event.NavigateToActionSuccess -> {
                    navController.navigate(ActionSuccess)
                }

                SavingTicketDetailsState.Event.OnBack -> {
                    navController.popBackStack()
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
            text = "Chi tiết phiếu",
            onBack = {
                viewModel.onAction(SavingTicketDetailsState.Action.OnBack)
            }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.background, MaterialTheme.shapes.extraLarge)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DetailsRow(
                icon = Icons.Default.Tag,
                text = uiState.savingTicket.id.toString(),
                title = "Mã phiếu",
                titleColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.primary
            )
            if (isStaff) {
                DetailsRow(
                    icon = Icons.Default.Person,
                    text = uiState.savingTicket.citizenId,
                    title = "CCCD",
                    titleColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.primary
                )
            }
            DetailsRow(
                icon = Icons.Default.Category,
                title = "Loại tiết kiệm",
                text = uiState.savingTicket.savingTypeName,
                titleColor = MaterialTheme.colorScheme.outline,
                textColor = MaterialTheme.colorScheme.onBackground
            )
            DetailsRow(
                title = "Kỳ hạn",
                icon = Icons.Default.DateRange,
                text = if(uiState.savingTicket.duration==0) "Không có" else "${uiState.savingTicket.duration} tháng",
                titleColor = MaterialTheme.colorScheme.outline,
                textColor = MaterialTheme.colorScheme.onBackground
            )
            DetailsRow(
                icon = Icons.Default.AttachMoney,
                title = "Lãi suất",
                text = "${uiState.savingTicket.interestRate}",
                titleColor = MaterialTheme.colorScheme.outline,
                textColor = MaterialTheme.colorScheme.onBackground
            )
            DetailsRow(
                title = "Số tiền gửi",
                icon = Icons.Default.MonetizationOn,
                text = StringUtils.formatCurrency(uiState.savingTicket.amount),
                titleColor = MaterialTheme.colorScheme.outline,
                textColor = MaterialTheme.colorScheme.onBackground
            )
            DetailsRow(
                title = "Số dư còn lại",
                icon = Icons.Default.MonetizationOn,
                text = StringUtils.formatCurrency(uiState.savingTicket.balance),
                titleColor = MaterialTheme.colorScheme.outline,
                textColor = MaterialTheme.colorScheme.onBackground
            )

            DetailsRow(
                title = "Ngày tạo",
                icon = Icons.Default.Timer,
                text = StringUtils.formatDateTime(uiState.savingTicket.createdAt)!!,
                titleColor = MaterialTheme.colorScheme.outline,
                textColor = MaterialTheme.colorScheme.onBackground
            )
            if(uiState.savingTicket.maturityDate!=null){
                DetailsRow(
                    title = "Ngày đáo hạn",
                    icon = Icons.Default.Timer,
                    text = StringUtils.formatLocalDate(uiState.savingTicket.maturityDate)!!,
                    titleColor = MaterialTheme.colorScheme.outline,
                    textColor = MaterialTheme.colorScheme.onBackground
                )
            }

            Text(
                text = "Danh sách lần rút",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
            WithDrawTicketList(
                withDrawTickets = uiState.savingTicket.withdrawalTickets,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }


        AppButton(
            text = "Rút tiền",
            onClick = {
                viewModel.onAction(SavingTicketDetailsState.Action.Withdraw)
            },
            enable = isWithDraw && uiState.savingTicket.balance != BigDecimal.ZERO && !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        )

    }




    if (showWithDrawSheet) {
        CustomBottomSheet(
            title = "Rút tiền từ phiếu",
            onDismiss = {
                showWithDrawSheet = false
            },
            content = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {
                    PassbookTextField(
                        value = uiState.request.withdrawalAmount.toPlainString(),
                        onValueChange = {
                            viewModel.onAction(
                                SavingTicketDetailsState.Action.OnChangeAmountWithdrawal(
                                    it.toBigDecimalOrNull()
                                )
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.MoneyOff,
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
                    AppButton(
                        onClick = {
                            viewModel.onAction(SavingTicketDetailsState.Action.OnCreateWithdrawalTicket)
                        },
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = "Rút",
                        backgroundColor = MaterialTheme.colorScheme.primary,
                    )
                }

            }
        )
    }

    if (showErrorSheet) {
        ErrorModalBottomSheet(
            description = uiState.error.toString(),
            onDismiss = {
                showErrorSheet = false
            },
        )
    }
}

@Composable
fun WithDrawTicketList(
    modifier: Modifier = Modifier,
    withDrawTickets: List<WithdrawalTicket>,
) {
    if (withDrawTickets.isEmpty()) {

        Nothing(
            text = "Không có lần rút nào",
            icon = Icons.Default.MoneyOff,
            modifier = modifier
        )
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            items(
                withDrawTickets, key = { it -> it.id },
            ) {
                WithDrawTicketCard(withDrawTicket = it)

            }

        }


    }
}


@Composable
fun WithDrawTicketCard(withDrawTicket: WithdrawalTicket) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.inversePrimary, MaterialTheme.shapes.medium)
            .padding(12.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DetailsRow(
                icon = Icons.Default.Timer,
                title = "Thời gian",
                text = StringUtils.formatDateTime(withDrawTicket.createdAt)!!,
                titleColor = MaterialTheme.colorScheme.outline
            )
            DetailsRow(
                icon = Icons.Default.MonetizationOn,
                title = "Giao dịch",
                text = "-${StringUtils.formatCurrency(withDrawTicket.withdrawalAmount)}",
                titleColor = MaterialTheme.colorScheme.outline,
                textColor = MaterialTheme.colorScheme.error
            )
        }
    }
}