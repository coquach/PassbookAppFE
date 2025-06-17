package com.se104.passbookapp.ui.screen.home

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
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.se104.passbookapp.ui.screen.components.TabWithPager
import com.se104.passbookapp.ui.screen.components.AppButton
import com.se104.passbookapp.ui.screen.components.BoxAvatar
import com.se104.passbookapp.ui.screen.components.CustomBottomSheet
import com.se104.passbookapp.ui.screen.components.IconCustomButton
import com.se104.passbookapp.ui.screen.components.text_field.PassbookTextField
import com.se104.passbookapp.ui.theme.button
import com.se104.passbookapp.utils.StringUtils
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeCustomerScreen(
    navController: NavController,
    viewModel: HomeViewModel= hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var isOpenTransactionSheet by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            IconCustomButton(

                onClick = {},
                containerColor = Color.Transparent,
                iconColor = MaterialTheme.colorScheme.primary,
                icon = Icons.Default.Notifications,

                )

        }
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
                    text = "Chào buổi sáng",
                    style = MaterialTheme.typography.bodyLarge,

                    )
                Text(
                    text = "Nguyễn Văn A",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )

            }
        }

        Box(
            modifier = Modifier
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
                        onClick = {
                            isOpenTransactionSheet = true
                        },
                        modifier = Modifier
                            .width(150.dp)
                            .height(48.dp),
                        shape = MaterialTheme.shapes.extraLarge,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.button,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),

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
                ){
                    var isVisible by remember { mutableStateOf(false) }
                    Text(
                        text = StringUtils.formatCurrency(BigDecimal(50000), !isVisible),
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
                                onClick = {},
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = "Nạp",
                                backgroundColor = MaterialTheme.colorScheme.primary,
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
                                onClick = {},
                                modifier = Modifier
                                    .fillMaxWidth(),
                                text = "Rút",
                                backgroundColor = MaterialTheme.colorScheme.primary,
                            ) 
                        }
                    }
                ),
                modifier = Modifier.fillMaxWidth(),
                onTabSelected =
                    {
                        viewModel.onAction(HomeState.Action.SetAmountDefault)
                    },
            )
        }
    }

}