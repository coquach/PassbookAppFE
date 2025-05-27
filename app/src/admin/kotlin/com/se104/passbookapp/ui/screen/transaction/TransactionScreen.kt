package com.se104.passbookapp.ui.screen.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.foodapp.ui.screen.components.SearchField
import com.se104.passbookapp.ui.screen.components.HeaderDefaultView
import com.se104.passbookapp.ui.screen.components.LazyPagingSample
import androidx.compose.runtime.getValue
import androidx.savedstate.savedState
import com.se104.passbookapp.data.model.Transaction
import com.se104.passbookapp.ui.component.CardSample
import com.se104.passbookapp.ui.component.DetailsRow
import com.se104.passbookapp.utils.StringUtils

@Composable
fun TransactionScreen(
    navController: NavController,
    viewModel: TransactionViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val transactions = viewModel.transactions.collectAsLazyPagingItems()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderDefaultView(
            text = "Danh sách giao dịch"
        )
        SearchField(
            searchInput = uiState.search,
            searchChange = {
                viewModel.onAction(TransactionState.Action.Search(it))
            }
        )
        LazyPagingSample(
            items = transactions,
            modifier = Modifier.weight(1f),
            textNothing = "Không có giao dịch nào",
            iconNothing = Icons.Default.CurrencyExchange,
            columns = 1,
            key = {
                it.id
            },
        ) {
            TransactionCard(transaction = it, onClick = {})
        }
    }
}

@Composable
fun TransactionCard(transaction: Transaction, onClick: (Transaction) -> Unit) {
    CardSample(
        item = transaction,
        onClick = onClick
    ) {
        TransactionDetails(transaction)

    }
}

@Composable
fun TransactionDetails(transaction: Transaction) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CurrencyExchange,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(100.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DetailsRow(
                icon = Icons.Default.Tag,
                text = transaction.id.toString(),
                style = MaterialTheme.typography.titleMedium
            )
            DetailsRow(
                icon = Icons.Default.Person,
                text = transaction.userFullName
            )
            DetailsRow(
                icon = Icons.Default.Category,
                text = transaction.transactionType
            )
            DetailsRow(
                icon = Icons.Default.MonetizationOn,
                text = StringUtils.formatCurrency(transaction.amount)
            )
            DetailsRow(
                icon = Icons.Default.DateRange,
                text = StringUtils.formatDateTime(transaction.transactionDateTime)!!
            )
        }
    }
}