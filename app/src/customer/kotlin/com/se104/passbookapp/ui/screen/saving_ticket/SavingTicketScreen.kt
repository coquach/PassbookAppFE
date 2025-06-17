package com.se104.passbookapp.ui.screen.saving_ticket

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CreditCard
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.se104.passbookapp.ui.screen.components.SearchField
import com.se104.passbookapp.data.model.SavingTicket
import com.se104.passbookapp.data.model.WithdrawalTicket
import com.se104.passbookapp.navigation.ActionSuccess
import com.se104.passbookapp.ui.component.CardSample
import com.se104.passbookapp.ui.component.DetailsRow
import com.se104.passbookapp.ui.screen.components.AppButton
import com.se104.passbookapp.ui.screen.components.CustomBottomSheet
import com.se104.passbookapp.ui.screen.components.DateRangePickerSample
import com.se104.passbookapp.ui.screen.components.ErrorModalBottomSheet
import com.se104.passbookapp.ui.screen.components.HeaderDefaultView
import com.se104.passbookapp.ui.screen.components.LazyPagingSample
import com.se104.passbookapp.ui.screen.components.Nothing
import com.se104.passbookapp.ui.screen.components.text_field.PassbookTextField
import com.se104.passbookapp.utils.StringUtils
import java.math.BigDecimal
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingTicketScreen(
    navController: NavController,
    viewModel: SavingTicketViewModel = hiltViewModel(),
    isStaff: Boolean = false,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val savingTickets =remember(uiState.filter) {
        viewModel.getSavingTickets(uiState.filter)
    }.collectAsLazyPagingItems()
    var showDialogDetails by rememberSaveable { mutableStateOf(false) }
    var showWithDrawSheet by rememberSaveable { mutableStateOf(false) }
    var showErrorSheet by rememberSaveable { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        viewModel.event.flowWithLifecycle(lifecycleOwner.lifecycle).collect { event ->
            when (event) {
                SavingTicketState.Event.ShowError -> {
                    showErrorSheet = true
                }

                SavingTicketState.Event.NavigateToActionSuccess -> {
                    navController.navigate(ActionSuccess)
                }

                SavingTicketState.Event.Withdraw -> {
                    showWithDrawSheet = true
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
            text = "Phiếu tiết kiệm"
        )
        SearchField(
            searchInput = uiState.search,
            searchChange = {
                viewModel.onAction(SavingTicketState.Action.Search(it))
            },
            switchChange = {
                when (it) {
                    true -> viewModel.onAction(SavingTicketState.Action.OnChangeOrder("desc"))
                    false -> viewModel.onAction(SavingTicketState.Action.OnChangeOrder("asc"))
                }
            },
            filterChange = {
                when (it) {
                    "Id" -> viewModel.onAction(SavingTicketState.Action.OnChangeSortBy("id"))
                    "Kỳ hạn" -> viewModel.onAction(SavingTicketState.Action.OnChangeSortBy("duration"))
                    "Lãi suất" -> viewModel.onAction(SavingTicketState.Action.OnChangeSortBy("interestRate"))
                    "Ngày tạo" -> viewModel.onAction(SavingTicketState.Action.OnChangeSortBy("createdAt"))
                    "Ngày đáo hạn" -> viewModel.onAction(SavingTicketState.Action.OnChangeSortBy("maturityDate"))
                    else -> viewModel.onAction(SavingTicketState.Action.OnChangeSortBy("id"))

                }
                Log.d("SavingTicketScreen", "filterChange: ${uiState.filter}")
            },
            filters = listOf("Id",  "Ngày tạo", "Ngày đáo hạn","Kỳ hạn", "Lãi suất",),
            filterSelected = when(uiState.filter.sortBy){
                "id" -> "Id"
                "duration" -> "Kỳ hạn"
                "interestRate" -> "Lãi suất"
                "createdAt" -> "Ngày tạo"
                "maturityDate" -> "Ngày đáo hạn"
                else -> ""
            },
            switchState = uiState.filter.order =="desc"
        )
        DateRangePickerSample(
            modifier = Modifier.width(170.dp),
            startDateText = "Bắt đầu",
            endDateText = "Kết thúc",
            onDateRangeSelected = { startDate, endDate ->
                viewModel.onAction(SavingTicketState.Action.OnChangeDateFilter(startDate, endDate))
            },
            startDate = uiState.filter.startDate,
            endDate = uiState.filter.endDate,
        )

        LazyPagingSample(
            items = savingTickets,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            textNothing = "Không có phiếu tiết kiệm nào",
            iconNothing = Icons.Default.CreditCard,
            columns = 1,
            key = {
                it.id
            },
        ) {
            SavingTicketCard(
                savingTicket = it,
                onClick = {
                    viewModel.onAction(SavingTicketState.Action.OnSelectSavingTicket(it))
                    showDialogDetails = true
                },
            )
        }
    }
    if (showDialogDetails) {
        Dialog(
            onDismissRequest = {
                showDialogDetails = false
            },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(700.dp)
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(30.dp)

            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {


                    Text(
                        text = "Chi tiết phiếu",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth().weight(1f)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        DetailsRow(
                            icon = Icons.Default.Tag,
                            text = uiState.savingTicketSelected!!.id.toString(),
                            title = "Mã phiếu",
                        )
                        if (isStaff) {
                            DetailsRow(
                                icon = Icons.Default.Person,
                                text = uiState.savingTicketSelected!!.userId.toString(),
                                title = "Mã khách hàng"
                            )
                        }
                        DetailsRow(
                            icon = Icons.Default.Category,
                            title = "Loại tiết kiệm",
                            text = uiState.savingTicketSelected!!.savingTypeName
                        )
                        DetailsRow(
                            title = "Kỳ hạn",
                            icon = Icons.Default.DateRange,
                            text = "${uiState.savingTicketSelected!!.duration} tháng"
                        )
                        DetailsRow(
                            icon = Icons.Default.AttachMoney,
                            title = "Lãi suất",
                            text = "${uiState.savingTicketSelected!!.interestRate}%",

                            )
                        DetailsRow(
                            title = "Số tiền gửi",
                            icon = Icons.Default.MonetizationOn,
                            text = StringUtils.formatCurrency(uiState.savingTicketSelected!!.amount)
                        )
                        DetailsRow(
                            title = "Số dư còn lại",
                            icon = Icons.Default.MonetizationOn,
                            text = StringUtils.formatCurrency(uiState.savingTicketSelected!!.balance)
                        )

                        DetailsRow(
                            title = "Ngày tạo",
                            icon = Icons.Default.Timer,
                            text = StringUtils.formatDateTime(uiState.savingTicketSelected!!.createdAt)!!
                        )
                        DetailsRow(
                            title = "Ngày đáo hạn",
                            icon = Icons.Default.Timer,
                            text = StringUtils.formatLocalDate(uiState.savingTicketSelected!!.maturityDate)!!
                        )
                        Text(
                            text = "Danh sách lần rút",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )
                        WithDrawTicketList(withDrawTickets = uiState.savingTicketSelected!!.withdrawalTickets, modifier = Modifier.fillMaxWidth().weight(1f))


                    }

                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                    ) {
                        AppButton(
                            text = "Đóng",
                            onClick = {
                                showDialogDetails = false
                            },
                            enable = !uiState.isLoading,
                            backgroundColor = MaterialTheme.colorScheme.outline,

                        )
                        AppButton(
                            text = "Rút",
                            onClick = {
                                viewModel.onAction(SavingTicketState.Action.Withdraw)
                            },
                            enable = uiState.savingTicketSelected!!.balance != BigDecimal.ZERO && !uiState.isLoading
                        )
                    }
                }
            }
        }
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
                            viewModel.onAction(SavingTicketState.Action.OnChangeAmountWithdrawal(it.toBigDecimalOrNull()))
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
                            viewModel.onAction(SavingTicketState.Action.OnCreateWithdrawalTicket)
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
            .background(MaterialTheme.colorScheme.background)
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
                text = StringUtils.formatDateTime(withDrawTicket.createdAt)!!
            )
            DetailsRow(
                icon = Icons.Default.MonetizationOn,
                title = "Giao dịch",
                text = "-${StringUtils.formatCurrency(withDrawTicket.withdrawalAmount)}",
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}


@Composable
fun SavingTicketCard(savingTicket: SavingTicket, onClick: (SavingTicket) -> Unit) {
    CardSample(
        item = savingTicket,
        onClick = onClick
    ) {
        Column {
            SavingTicketDetails(savingTicket = savingTicket)
        }
    }
}

@Composable
fun SavingTicketDetails(savingTicket: SavingTicket, isStaff: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CreditCard,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(80.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DetailsRow(
                icon = Icons.Default.Tag,
                text = savingTicket.userId.toString(),
                title = "Mã phiếu",
            )
            if (isStaff) {
                DetailsRow(
                    icon = Icons.Default.Person,
                    text = savingTicket.userId.toString(),
                    title = "Mã khách hàng"
                )
            }
            DetailsRow(
                icon = Icons.Default.Category,
                title = "Loại tiết kiệm",
                text = savingTicket.savingTypeName
            )
            DetailsRow(
                title = "Kỳ hạn",
                icon = Icons.Default.DateRange,
                text = "${savingTicket.duration} tháng"
            )

            DetailsRow(
                title = "Ngày tạo",
                icon = Icons.Default.Timer,
                text = StringUtils.formatDateTime(savingTicket.createdAt)!!
            )

        }
    }
}