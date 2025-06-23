package com.se104.passbookapp.ui.screen.home

import DetailsRow
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AppSettingsAlt
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.NotInterested
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.se104.passbookapp.data.model.User
import com.se104.passbookapp.navigation.ActionSuccess
import com.se104.passbookapp.ui.screen.components.AppButton
import com.se104.passbookapp.ui.screen.components.BoxAvatar
import com.se104.passbookapp.ui.screen.components.CustomBottomSheet
import com.se104.passbookapp.ui.screen.components.ErrorModalBottomSheet
import com.se104.passbookapp.ui.screen.components.IconCustomButton
import com.se104.passbookapp.ui.screen.components.LazyPagingSample
import com.se104.passbookapp.ui.screen.components.LoadingAnimation
import com.se104.passbookapp.ui.screen.components.Nothing
import com.se104.passbookapp.ui.screen.components.Retry
import com.se104.passbookapp.ui.screen.components.SearchField
import com.se104.passbookapp.ui.screen.components.TabWithPager
import com.se104.passbookapp.ui.screen.components.text_field.PassbookTextField
import com.se104.passbookapp.ui.theme.button
import com.se104.passbookapp.utils.StringUtils
import com.se104.passbookapp.utils.hasAllPermissions
import com.se104.passbookapp.utils.hasPermission
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel(),
    permissions: List<String>,
) {
    val isViewAllUsersActive by remember(permissions) {
        mutableStateOf(permissions.hasPermission("VIEW_USERS"))
    }

    val isTransaction by remember(permissions) {
        mutableStateOf(permissions.hasAllPermissions("DEPOSIT", "WITHDRAW"))
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isOpenTransactionSheet by remember { mutableStateOf(false) }
    var showErrorSheet by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        viewModel.event.flowWithLifecycle(lifecycleOwner.lifecycle).collect { event ->
            when (event) {
                is HomeState.Event.ShowError -> {
                    showErrorSheet = true
                }
                HomeState.Event.ShowTransactionSheet -> {
                    isOpenTransactionSheet = true
                }

                HomeState.Event.NavigateToActionSuccess -> {
                    navController.navigate(ActionSuccess)
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getMyInfo()
    }




    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

            when (uiState.getInfoState) {
                is HomeState.GetInfoState.Error -> {
                    val message = (uiState.getInfoState as HomeState.GetInfoState.Error).message
                    Retry(
                        message = message,
                        onClicked = {
                            viewModel.getMyInfo()
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                HomeState.GetInfoState.Loading -> {
                    LoadingAnimation(
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                is HomeState.GetInfoState.Success -> {
                    val user = (uiState.getInfoState as HomeState.GetInfoState.Success).data
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start)
                    ) {

                        BoxAvatar(
                            modifier = Modifier.size(50.dp),
                        )
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.Start

                        ) {
                            Text(
                                text = viewModel.getGreetingTitle(),
                                style = MaterialTheme.typography.bodyLarge,


                                )
                            Text(
                                text = user.fullName,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )

                        }
                    }

                    if (isViewAllUsersActive) {
                        val users = remember(uiState.filter) {
                            viewModel.getUsers(uiState.filter)

                        }.collectAsLazyPagingItems()
                        SearchField(
                            searchInput = uiState.search,
                            searchChange = {
                                viewModel.onAction(HomeState.Action.OnSearch(it))

                            },
                            switchChange = {
                                when (it) {
                                    true -> viewModel.onAction(HomeState.Action.OnChangeOrder("desc"))
                                    false -> viewModel.onAction(HomeState.Action.OnChangeOrder("asc"))
                                }
                            },
                            filterChange = {
                                when (it) {
                                    "Tên" -> viewModel.onAction(HomeState.Action.OnChangeSortBy("fullName"))
                                    "Số dư" -> viewModel.onAction(HomeState.Action.OnChangeSortBy("balance"))
                                    else -> viewModel.onAction(HomeState.Action.OnChangeSortBy("fullName"))

                                }
                                Log.d("Home", "filterChange: ${uiState.filter}")
                            },
                            filters = listOf("Tên", "Số dư"),
                            filterSelected = when (uiState.filter.sortBy) {
                                "fullName" -> "Tên"
                                "balance" -> "Số dư"
                                else -> "Tên"
                            },
                            switchState = uiState.filter.order == "desc",
                            placeHolder = "Tìm kiếm theo CCCD..."
                        )
                        LazyPagingSample(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            items = users,
                            textNothing = "Không có khách hàng nào",
                            iconNothing = Icons.Default.People,
                            columns = 1,
                            key = {
                                it.id
                            },

                            ) {
                            UserSection(
                                onClick = {
                                    viewModel.onAction(HomeState.Action.OnSelectUser(it.id))
                                    viewModel.onAction(HomeState.Action.ShowTransactionSheet)
                                },
                                user = it,
                                isStaff = true,
                                enabled = isTransaction
                            )
                        }

                    } else  {
                                UserSection(
                                    onClick = {
                                        viewModel.onAction(HomeState.Action.OnSelectUser(user.id))
                                       viewModel.onAction(HomeState.Action.ShowTransactionSheet)
                                    },
                                    user = user,
                                    enabled = isTransaction
                                )
                        Nothing(
                            modifier = Modifier.fillMaxWidth().weight(1f),
                            icon = Icons.Default.AppSettingsAlt,
                            text = "Tính năng mới sẽ sớm được ra mắt ..."
                        )


                    }
                }


        }

    }
    if (isOpenTransactionSheet) {
        CustomBottomSheet(
            title = "Giao dịch",
            onDismiss = {
                isOpenTransactionSheet = false
            },
        ) {
            TabWithPager(
                tabs = listOf("Nạp tiền", "Rút tiền"),
                pages = listOf(
                    {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                            PassbookTextField(
                                value = uiState.amount.toPlainString(),
                                onValueChange = {
                                    viewModel.onAction(HomeState.Action.OnAmountChange(it.toBigDecimalOrNull()))
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.AttachMoney,
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
                                    viewModel.onAction(HomeState.Action.Deposit)
                                },
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = "Nạp",
                                backgroundColor = MaterialTheme.colorScheme.primary,
                                enable = isTransaction && uiState.amount != BigDecimal.ZERO && !uiState.loading
                            )
                        }
                    },
                    {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                            PassbookTextField(
                                value = uiState.amount.toPlainString(),
                                onValueChange = {
                                    viewModel.onAction(HomeState.Action.OnAmountChange(it.toBigDecimalOrNull()))
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
                                    viewModel.onAction(HomeState.Action.Withdraw)
                                },
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = "Rút",
                                backgroundColor = MaterialTheme.colorScheme.primary,
                                enable = isTransaction && uiState.amount != BigDecimal.ZERO && !uiState.loading
                            )
                        }
                    }
                ),
                modifier = Modifier.fillMaxWidth(),
                onTabSelected =
                    {
                        viewModel.onAction(HomeState.Action.OnAmountChange(BigDecimal.ZERO))

                    },
            )
        }
    }
    if (showErrorSheet) {
        ErrorModalBottomSheet(
            description = uiState.error.toString(),
            onDismiss = {
                showErrorSheet = false
            }
        )
    }

}

@Composable
fun UserSection(
    onClick: () -> Unit,
    user: User,
    isStaff: Boolean = false,
    enabled: Boolean
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.medium
            )
            .padding(18.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),

            ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Số dư tài khoản",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold

                )
                Button(
                    onClick = onClick,
                    modifier = Modifier
                        .width(150.dp)
                        .height(48.dp),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.button,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    enabled =enabled

                    ) {
                    Icon(
                        imageVector = Icons.Default.CurrencyExchange,
                        contentDescription = "Transaction",
                    )
                    Text(
                        text = "Giao dịch",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                var isVisible by remember { mutableStateOf(false) }
                Text(
                    text = StringUtils.formatCurrency(user.balance, !isVisible),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold

                )
                IconCustomButton(
                    onClick = {
                        isVisible = !isVisible
                    },
                    containerColor = Color.Transparent,
                    icon = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    iconColor = MaterialTheme.colorScheme.primary
                )
            }
            if (isStaff) {
                DetailsRow(
                    text = user.fullName,
                    title = "Họ và tên",
                    icon = Icons.Default.Person,
                )
                DetailsRow(
                    text = user.citizenID,
                    title = "CCCD",
                    icon = Icons.Default.Tag,
                )
            }


        }
    }
}