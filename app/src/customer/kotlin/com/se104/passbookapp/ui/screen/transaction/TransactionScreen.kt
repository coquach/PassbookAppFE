package com.se104.passbookapp.ui.screen.transaction

import DetailsRow
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.se104.passbookapp.data.model.Transaction
import com.se104.passbookapp.data.model.enums.TransactionType
import com.se104.passbookapp.ui.screen.components.DateRangePickerSample
import com.se104.passbookapp.ui.screen.components.HeaderDefaultView
import com.se104.passbookapp.ui.screen.components.LazyPagingSample
import com.se104.passbookapp.ui.screen.components.SearchField
import com.se104.passbookapp.ui.theme.confirm
import com.se104.passbookapp.utils.StringUtils
import com.se104.passbookapp.utils.hasPermission

@Composable
fun TransactionScreen(
    viewModel: TransactionViewModel = hiltViewModel(),
    permissions: List<String>,
) {
    val isStaff by rememberSaveable { mutableStateOf(permissions.hasPermission("VIEW_ALL_TRANSACTIONS")) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val transactions = remember(uiState.filter) {
        if (isStaff) {
            viewModel.getTransactions(uiState.filter)
        } else {
            viewModel.getTransactionsForCustomer(uiState.filter)
        }
    }.collectAsLazyPagingItems()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderDefaultView(
            text = "Lịch sử giao dịch"
        )
        SearchField(
            searchInput = uiState.search,
            searchChange = {
                if (isStaff) viewModel.onAction(TransactionState.Action.SearchStaff(it)) else
                    viewModel.onAction(TransactionState.Action.SearchCustomer(it))
            },
            switchState = uiState.filter.order == "desc",
            switchChange = {
                when (it) {
                    true -> viewModel.onAction(TransactionState.Action.OnChangeOrder("desc"))
                    false -> viewModel.onAction(TransactionState.Action.OnChangeOrder("asc"))
                }
            },
            filterChange = {
                when (it) {
                    "Id" -> viewModel.onAction(TransactionState.Action.OnChangeSortBy("id"))
                    "Giao dịch" -> viewModel.onAction(TransactionState.Action.OnChangeSortBy("amount"))
                    "Thời gian" -> viewModel.onAction(TransactionState.Action.OnChangeSortBy("createdAt"))
                    else -> viewModel.onAction(TransactionState.Action.OnChangeSortBy("id"))

                }
                Log.d("Transaction filter", "filterChange: ${uiState.filter}")
            },
            filters = listOf("Id", "Giao dịch", "Thời gian"),
            filterSelected =
                when (uiState.filter.sortBy) {
                    "id" -> "Id"
                    "amount" -> "Giao dịch"
                    "createdAt" -> "Thời gian"
                    else -> ""
                },
            placeHolder = if (isStaff) "Tìm kiếm theo CCCD.." else "Tìm kiếm"
        )
        DateRangePickerSample(
            modifier = Modifier.width(170.dp),
            startDateText = "Bắt đầu",
            endDateText = "Kết thúc",
            onDateRangeSelected = { startDate, endDate ->
                viewModel.onAction(TransactionState.Action.OnChangeDateFilter(startDate, endDate))
            },
            startDate = uiState.filter.startDate,
            endDate = uiState.filter.endDate,
        )
        LazyPagingSample(
            items = transactions,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            textNothing = "Không có giao dịch nào",
            iconNothing = Icons.Default.CurrencyExchange,
            columns = 1,
            key = {
                it.id
            },
        ) {
            TransactionDetails(transaction = it, isStaff = isStaff)
        }
    }
}



@Composable
fun TransactionDetails(transaction: Transaction, isStaff: Boolean = false) {

        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraLarge)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = MaterialTheme.shapes.extraLarge
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            DetailsRow(
                title = "Mã giao dịch",
                icon = Icons.Default.Tag,
                text = transaction.id.toString(),
                titleColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.primary
            )
            DetailsRow(
                title = "Thời gian",
                icon = Icons.Default.DateRange,
                text = StringUtils.formatDateTime(transaction.createdAt)!!,
                titleColor = MaterialTheme.colorScheme.outline,
                textColor = MaterialTheme.colorScheme.onBackground

            )

            if (isStaff) {
                DetailsRow(
                    title = "CCCD",
                    icon = Icons.Default.Person,
                    text = transaction.citizenID,
                    titleColor = MaterialTheme.colorScheme.primary,
                    textColor = MaterialTheme.colorScheme.onBackground
                )
            }

            if (transaction.transactionType == TransactionType.WITHDRAWAL.name || transaction.transactionType == TransactionType.SAVE.name) {
                DetailsRow(
                    title = "Giao dịch",
                    icon = Icons.Default.MonetizationOn,
                    text = "-${StringUtils.formatCurrency(transaction.amount)}",
                    titleColor = MaterialTheme.colorScheme.outline,
                    textColor = MaterialTheme.colorScheme.error
                )
            } else {
                DetailsRow(
                    title = "Giao dịch",
                    icon = Icons.Default.MonetizationOn,
                    text = "+${StringUtils.formatCurrency(transaction.amount)}",
                    titleColor = MaterialTheme.colorScheme.outline,
                    textColor = MaterialTheme.colorScheme.confirm
                )
            }
            DetailsRow(
                title = "Nội dung",
                icon = Icons.Default.Category,
                text = TransactionType.fromName(transaction.transactionType).display,
                titleColor = MaterialTheme.colorScheme.outline,
                textColor = MaterialTheme.colorScheme.onBackground
            )



    }
}