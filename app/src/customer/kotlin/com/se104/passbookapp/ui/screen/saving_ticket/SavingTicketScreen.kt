package com.se104.passbookapp.ui.screen.saving_ticket

import CardSample
import DetailsRow
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.NotInterested
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.se104.passbookapp.data.model.SavingTicket
import com.se104.passbookapp.navigation.SavingTicketDetails
import com.se104.passbookapp.ui.screen.components.ChipsGroupWrap
import com.se104.passbookapp.ui.screen.components.DateRangePickerSample
import com.se104.passbookapp.ui.screen.components.HeaderDefaultView
import com.se104.passbookapp.ui.screen.components.LazyPagingSample
import com.se104.passbookapp.ui.screen.components.Nothing
import com.se104.passbookapp.ui.screen.components.SearchField
import com.se104.passbookapp.ui.screen.components.TabWithPager
import com.se104.passbookapp.utils.StringUtils
import com.se104.passbookapp.utils.hasPermission


@Composable
fun SavingTicketScreen(
    navController: NavController,
    viewModel: SavingTicketViewModel = hiltViewModel(),
    permissions: List<String>,
) {

    val isViewAllTickets by rememberSaveable { mutableStateOf(permissions.hasPermission("VIEW_ALL_SAVINGTICKETS")) }
    val isViewMyTickets by rememberSaveable { mutableStateOf(permissions.hasPermission("VIEW_MY_SAVINGTICKETS")) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val savingTickets = remember(uiState.filter) {
        if (isViewAllTickets) {
            viewModel.getSavingTickets(uiState.filter)
        } else  {
            viewModel.getSavingTicketsForCustomer(uiState.filter)
        }
    }.collectAsLazyPagingItems()


    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        viewModel.event.flowWithLifecycle(lifecycleOwner.lifecycle).collect { event ->
            when (event) {
                is SavingTicketState.Event.NavigateToDetail -> {
                    navController.navigate(SavingTicketDetails(event.savingTicket))
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getActiveSavingTypes()
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
                if (isViewAllTickets) viewModel.onAction(
                    SavingTicketState.Action.SearchStaff(
                        it
                    )
                ) else
                    viewModel.onAction(SavingTicketState.Action.SearchCustomer(it))

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
            filters = listOf("Id", "Ngày tạo", "Ngày đáo hạn", "Kỳ hạn", "Lãi suất"),
            filterSelected = when (uiState.filter.sortBy) {
                "id" -> "Id"
                "duration" -> "Kỳ hạn"
                "interestRate" -> "Lãi suất"
                "createdAt" -> "Ngày tạo"
                "maturityDate" -> "Ngày đáo hạn"
                else -> "Id"
            },
            switchState = uiState.filter.order == "desc",
            placeHolder = if (isViewAllTickets) "Tìm kiếm theo CCCD" else "Tìm kiếm theo số tiền gửi..."
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

        ChipsGroupWrap(
            modifier = Modifier.padding(8.dp),
            options = uiState.savingTypes.map { it.typeName },
            selectedOption = uiState.savingTypeName,
            onOptionSelected = { savingTypeName ->
                val savingType = uiState.savingTypes.find { it.typeName == savingTypeName }
                savingType?.let {
                    viewModel.onAction(
                        SavingTicketState.Action.OnChangeSavingType(
                            it.id!!,
                            it.typeName
                        )
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
            isFlowLayout = false,
        )


        if (isViewAllTickets){
            TabWithPager(
                modifier = Modifier.fillMaxWidth().weight(1f),
                tabs = listOf("Đang hoạt động", "Đã ẩn"),
                pages = listOf(
                    {
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
                                },
                                isStaff = true
                            )
                        }
                    },
                    {
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
                                },
                                isStaff = true
                            )
                        }
                    }
                ),
                onTabSelected = {
                    when(it){
                        0 -> viewModel.onAction(SavingTicketState.Action.OnChangeIsActive(true))
                        1-> viewModel.onAction(SavingTicketState.Action.OnChangeIsActive(false))
                    }
                },
            )
        }else if (isViewMyTickets){
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
                    },
                    isStaff = false
                )
            }
        }else{
            Nothing(
                modifier = Modifier.fillMaxWidth().weight(1f),
                icon = Icons.Default.NotInterested,
                text = "Bạn không có quyền truy cập"
            )
        }







    }


}


@Composable
fun SavingTicketCard(
    savingTicket: SavingTicket,
    onClick: (SavingTicket) -> Unit,
    isStaff: Boolean = false,
) {
    CardSample(
        item = savingTicket,
        onClick = onClick
    ) {
        Column {
            SavingTicketDetails(savingTicket = savingTicket, isStaff = isStaff)
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
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DetailsRow(
                icon = Icons.Default.Tag,
                text = savingTicket.id.toString(),
                title = "Mã phiếu",
                titleColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.primary
            )
            if (isStaff) {
                DetailsRow(
                    icon = Icons.Default.Person,
                    text = savingTicket.citizenId,
                    title = "CCCD",
                    titleColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.primary
                )
            }
            DetailsRow(
                icon = Icons.Default.Category,
                title = "Loại tiết kiệm",
                text = savingTicket.savingTypeName,
                titleColor = MaterialTheme.colorScheme.outline,
                textColor = MaterialTheme.colorScheme.outline
            )
            DetailsRow(
                title = "Kỳ hạn",
                icon = Icons.Default.DateRange,
                text = if (savingTicket.duration == 0) "Không có" else "${savingTicket.duration} tháng",
                titleColor = MaterialTheme.colorScheme.outline,
                textColor = MaterialTheme.colorScheme.outline
            )

            DetailsRow(
                title = "Ngày tạo",
                icon = Icons.Default.AccessTime,
                text = StringUtils.formatDateTime(savingTicket.createdAt)!!,
                titleColor = MaterialTheme.colorScheme.outline,
                textColor = MaterialTheme.colorScheme.outline
            )

        }
    }
}