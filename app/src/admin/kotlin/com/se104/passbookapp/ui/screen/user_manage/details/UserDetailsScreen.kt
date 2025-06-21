package com.se104.passbookapp.ui.screen.user_manage.details

import DetailsRow
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.se104.passbookapp.ui.screen.components.AppButton
import com.se104.passbookapp.ui.screen.components.ErrorModalBottomSheet
import com.se104.passbookapp.ui.screen.components.HeaderDefaultView
import com.se104.passbookapp.ui.screen.components.RadioGroupWrap
import com.se104.passbookapp.utils.StringUtils
import com.se104.passbookapp.utils.hasPermission

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailsScreen(
    navController: NavController,
    viewModel: UserDetailsViewModel = hiltViewModel(),
    permission: List<String>,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isSetGroupForUser by rememberSaveable { mutableStateOf(permission.hasPermission("ADMIN_PREVILAGE")) }
    var showErrorSheet by rememberSaveable { mutableStateOf(false) }
    var showDialog by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        viewModel.event.flowWithLifecycle(lifecycleOwner.lifecycle).collect {
            when (it) {
                UserDetailsState.Event.OnBack -> {
                    navController.popBackStack()
                }

                UserDetailsState.Event.ShowError -> {
                    showErrorSheet = true

                }

                is UserDetailsState.Event.ShowSuccessToast -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getGroupForUser()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderDefaultView(
            text = "Thông tin tài khoản",
            onBack = {
                viewModel.onAction(UserDetailsState.Action.OnBack)
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
                title = "Họ và tên",
                text = uiState.user.fullName,
                icon = Icons.Default.Person,
                titleColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.primary
            )
            DetailsRow(
                title = "CCCD",
                text = uiState.user.citizenID,
                icon = Icons.Default.Tag,
                titleColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.primary
            )
            DetailsRow(
                title = "Vai trò",
                text = uiState.user.groupName,
                icon = Icons.Default.Groups,
                titleColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.primary
            )
            DetailsRow(
                title = "Email",
                text = uiState.user.email,
                icon = Icons.Default.Email,
                titleColor = MaterialTheme.colorScheme.outline,
                textColor = MaterialTheme.colorScheme.onBackground

            )
            DetailsRow(
                title = "Số điện thoại",
                text = uiState.user.phone!!,
                icon = Icons.Default.Phone,
                titleColor = MaterialTheme.colorScheme.outline,
                textColor = MaterialTheme.colorScheme.onBackground
            )

            DetailsRow(
                title = "Ngày sinh",
                text = StringUtils.formatLocalDate(uiState.user.dateOfBirth)!!,
                icon = Icons.Default.DateRange,
                titleColor = MaterialTheme.colorScheme.outline,
                textColor = MaterialTheme.colorScheme.onBackground
            )
            DetailsRow(
                title = "Địa chỉ",
                text = uiState.user.address!!,
                icon = Icons.Default.Person,
                titleColor = MaterialTheme.colorScheme.outline,
                textColor = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        AppButton(
            text = "Thay đổi vai trò",
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                showDialog = true

            },
            enable = isSetGroupForUser
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

    if (showDialog) {
        Dialog(
            onDismissRequest = {
                showDialog = false
            },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.extraLarge
                    )
                    .padding(20.dp)

            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {


                    Text(
                        text = "Vai trò",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(MaterialTheme.shapes.extraLarge)
                            .background(
                                MaterialTheme.colorScheme.background,
                                MaterialTheme.shapes.extraLarge
                            )
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)

                    ) {
                        RadioGroupWrap(
                            options = uiState.groups.map { it.description },
                            selectedOption = uiState.groupName,
                            onOptionSelected = { groupName ->
                                val group = uiState.groups.find { it.description == groupName }
                                if (group != null) {
                                    viewModel.onAction(
                                        UserDetailsState.Action.OnSelectGroup(
                                            groupId = group.id!!, groupName = group.description
                                        )
                                    )
                                }
                            },
                            useFlowLayout = false
                        )
                    }
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                    ) {
                        AppButton(
                            text = "Đóng",
                            onClick = {
                                showDialog = false
                            },
                            backgroundColor = MaterialTheme.colorScheme.outline,
                            textColor = MaterialTheme.colorScheme.onPrimary,
                            enable = !uiState.isLoading
                        )
                        AppButton(
                            text = "Cập nhật",
                            onClick = {
                                viewModel.onAction(UserDetailsState.Action.SetGroupForUser)
                            },
                            enable  = uiState.groupIdSelected != null && !uiState.isLoading
                        )
                    }


                }
            }

        }
    }
}