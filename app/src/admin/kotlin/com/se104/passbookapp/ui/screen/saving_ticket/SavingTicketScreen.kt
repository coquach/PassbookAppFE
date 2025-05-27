package com.se104.passbookapp.ui.screen.saving_ticket

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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Timer
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
import com.se104.passbookapp.data.model.SavingTicket
import androidx.compose.runtime.getValue
import com.se104.passbookapp.ui.component.CardSample
import com.se104.passbookapp.ui.component.DetailsRow
import com.se104.passbookapp.ui.screen.components.HeaderDefaultView
import com.se104.passbookapp.ui.screen.components.LazyPagingSample
import com.se104.passbookapp.utils.StringUtils

@Composable
fun SavingTicketScreen(
    navController: NavController,
    viewModel: SavingTicketViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val savingTickets = viewModel.savingTickets.collectAsLazyPagingItems()

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderDefaultView(
            text = "Danh sách phiếu tiết kiệm"
        )
        SearchField(
            searchInput = uiState.search,
            searchChange = {
                viewModel.onAction(SavingTicketState.Action.Search(it))
            }
        )
        LazyPagingSample(
            items = savingTickets,
            modifier = Modifier.weight(1f),
            textNothing = "Không có phiếu tiết kiệm nào",
            iconNothing = Icons.Default.CreditCard,
            columns = 1,
            key = {
                it.id
            },
        ) {
            SavingTicketCard(
                savingTicket = it, onClick = {
                    viewModel.onAction(SavingTicketState.Action.GoToDetail(it))
                },
            )
        }
    }
}

@Composable
fun SavingTicketCard(savingTicket: SavingTicket, onClick: (SavingTicket) -> Unit){
    CardSample(
        item = savingTicket,
        onClick = onClick
    ){

    }
}

@Composable
fun SavingTicketDetails(savingTicket: SavingTicket) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CreditCard,
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
                text = savingTicket.userId.toString(),
                style = MaterialTheme.typography.titleMedium
            )
            DetailsRow(
                icon = Icons.Default.Category,
                text = savingTicket.savingTypeName
            )
            DetailsRow(
                icon = Icons.Default.DateRange,
                text = "${savingTicket.duration} tháng"
            )
            DetailsRow(
                icon = Icons.Default.MonetizationOn,
                text = "${savingTicket.interestRate}%"
            )
            DetailsRow(
                icon = Icons.Default.Timer,
                text = StringUtils.formatDateTime(savingTicket.startDate)!!
            )
            DetailsRow(
                icon = Icons.Default.Timer,
                text = StringUtils.formatDateTime(savingTicket.maturityDate)!!
            )
        }
    }
}