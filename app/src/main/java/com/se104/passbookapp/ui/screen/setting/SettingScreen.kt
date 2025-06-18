package com.se104.passbookapp.ui.screen.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.BrightnessMedium
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.SettingsSuggest
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.se104.passbookapp.navigation.Parameters
import com.se104.passbookapp.navigation.Report
import com.se104.passbookapp.ui.screen.components.AppButton
import com.se104.passbookapp.ui.screen.components.ErrorModalBottomSheet
import com.se104.passbookapp.ui.screen.components.HeaderDefaultView
import com.se104.passbookapp.ui.screen.components.PassbookAppDialog
import com.se104.passbookapp.ui.screen.components.SettingGroup
import com.se104.passbookapp.ui.screen.components.SettingItem
import com.se104.passbookapp.ui.screen.components.ThemeSwitcher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navController: NavController,
    darkTheme: Boolean,
    onThemeUpdated: () -> Unit,
    viewModel: SettingViewModel = hiltViewModel(),
    permissions: List<String>,
) {


    var showErrorSheet by remember { mutableStateOf(false) }
    var showDialogLogout by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()



    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        viewModel.event.flowWithLifecycle(lifecycleOwner.lifecycle).collect { event ->
            when (event) {
                is SettingState.Event.ShowError -> {
                    showErrorSheet = true}
                is SettingState.Event.NavigateToReport -> {
                    navController.navigate(Report)
                }
                is SettingState.Event.NavigateToParameters -> {
                    navController.navigate(Parameters)
                }
            }
        }
    }


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {


            HeaderDefaultView(
                text = "Cài đặt",
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Spacer(modifier = Modifier.size(200.dp))
            SettingGroup(
                items = listOf(
                    {
                        SettingItem(
                            Icons.Default.BrightnessMedium,
                            "Chủ đề ",
                            customView = {
                                ThemeSwitcher(
                                    darkTheme = darkTheme,
                                    size = 35.dp,
                                    padding = 6.dp,
                                    onClick = onThemeUpdated
                                )
                            }
                        )
                        SettingItem(Icons.Default.Lock, "Bảo mật", onClick = {})
                        if (permissions.contains("VIEW_PARAMETERS")) {
                            SettingItem(Icons.Default.SettingsSuggest, "Quản lí hệ thống", onClick = {
                                viewModel.onAction(SettingState.Action.OnClickParameters)
                            })
                        }
                        if (permissions.contains("VIEW_REPORTS")) {
                            SettingItem(Icons.Default.BarChart, "Thống kê", onClick = {
                                viewModel.onAction(SettingState.Action.OnClickReport)
                            })
                        }

                    },
                )
            )

            Spacer(modifier = Modifier.height(10.dp))
            SettingGroup(
                items = listOf(
                    {
                        SettingItem(Icons.AutoMirrored.Filled.Help, "Hỏi đáp & trợ giúp")
                        SettingItem(Icons.AutoMirrored.Filled.Message, "Liên hệ")
                        SettingItem(Icons.Default.PrivacyTip, "Chính sách bảo mật")
                    }
                )
            )

            Spacer(modifier = Modifier.height(20.dp))
            AppButton(
                onClick = {
                    showDialogLogout = true
                },
               text = "Đăng xuất",
                modifier = Modifier.fillMaxWidth()

            )
        }

    if (showDialogLogout) {

        PassbookAppDialog(
            title = "Đăng xuất",
            titleColor = MaterialTheme.colorScheme.error,
            message = "Bạn có chắc chắn muốn đăng xuất khỏi tài khoản của mình không?",
            onDismiss = {

                showDialogLogout = false
            },
            onConfirm = {
                viewModel.onAction(SettingState.Action.OnLogout)
                showDialogLogout = false

            },
            confirmText = "Đăng xuất",
            dismissText = "Đóng",
            showConfirmButton = true
        )

    }
    if (showErrorSheet) {
        ErrorModalBottomSheet(
            onDismiss = {
                showErrorSheet = false
            },
            description = uiState.error.toString(),
        )
    }
}