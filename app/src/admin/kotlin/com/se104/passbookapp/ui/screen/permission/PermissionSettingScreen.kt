package com.se104.passbookapp.ui.screen.permission

import DetailsRow
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DriveFileRenameOutline
import androidx.compose.material.icons.filled.NotInterested
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.se104.passbookapp.data.model.Group
import com.se104.passbookapp.ui.screen.components.AppButton
import com.se104.passbookapp.ui.screen.components.CheckboxGroupColumn
import com.se104.passbookapp.ui.screen.components.ErrorModalBottomSheet
import com.se104.passbookapp.ui.screen.components.HeaderDefaultView
import com.se104.passbookapp.ui.screen.components.LoadingAnimation
import com.se104.passbookapp.ui.screen.components.MyFloatingActionButton
import com.se104.passbookapp.ui.screen.components.Nothing
import com.se104.passbookapp.ui.screen.components.PassbookAppDialog
import com.se104.passbookapp.ui.screen.components.Retry
import com.se104.passbookapp.ui.screen.components.text_field.PassbookTextField
import com.se104.passbookapp.utils.hasPermission
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionsSettingScreen(
    viewModel: PermissionsSettingViewModel = hiltViewModel(),
    permissions: List<String>,
) {
    val isAdmin by rememberSaveable { mutableStateOf(permissions.hasPermission("ADMIN_PREVILAGE")) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showErrorSheet by rememberSaveable { mutableStateOf(false) }
    var showDialogDelete by rememberSaveable { mutableStateOf(false) }
    var showDialogUpdate by rememberSaveable { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.flowWithLifecycle(lifecycleOwner.lifecycle).collect { event ->
            when (event) {
                PermissionsSettingState.Event.ShowError -> {
                    showErrorSheet = true
                }

                is PermissionsSettingState.Event.ShowSuccessToast -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                PermissionsSettingState.Event.ShowToastUnEnableAction -> {
                    Toast.makeText(
                        context,
                        "Bạn không có quyền thực hiện thao tác này",
                        Toast.LENGTH_SHORT

                    ).show()
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getPermissionsNotAdmin()
        viewModel.getGroups()
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        floatingActionButton = {
            MyFloatingActionButton(
                onClick = {
                    if (isAdmin) {
                        viewModel.onAction(PermissionsSettingState.Action.OnGroupSelected(Group()))
                        viewModel.onAction(PermissionsSettingState.Action.OnSetStatus(false))
                        showDialogUpdate = true
                    } else {
                        viewModel.onAction(PermissionsSettingState.Action.ShowToast)
                    }

                },
                bgColor = MaterialTheme.colorScheme.primary,
            ) {
                Box(modifier = Modifier.size(56.dp)) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Center)
                            .size(35.dp)
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    PaddingValues(
                        start = padding.calculateStartPadding(LayoutDirection.Ltr),
                        end = padding.calculateEndPadding(LayoutDirection.Ltr)
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HeaderDefaultView(
                text = "Phân quyền",
            )
            if (isAdmin) {
                when (uiState.getGroupsState) {
                    is PermissionsSettingState.GetGroupsState.Error -> {
                        val message =
                            (uiState.getGroupsState as PermissionsSettingState.GetGroupsState.Error).message
                        Retry(
                            message = message,
                            onClicked = {
                                viewModel.getGroups()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                    }

                    PermissionsSettingState.GetGroupsState.Loading -> {
                        LoadingAnimation(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        )
                    }

                    PermissionsSettingState.GetGroupsState.Success -> {
                        GroupList(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            groups = uiState.groups,
                            onClick = {
                                viewModel.onAction(PermissionsSettingState.Action.OnGroupSelected(it))
                                viewModel.onAction(PermissionsSettingState.Action.OnSetStatus(true))
                                showDialogUpdate = true
                            },
                            endAction = { it ->
                                SwipeAction(
                                    icon = rememberVectorPainter(Icons.Default.Delete),
                                    background = MaterialTheme.colorScheme.error,
                                    onSwipe = {
                                        viewModel.onAction(
                                            PermissionsSettingState.Action.OnGroupSelected(
                                                it
                                            )
                                        )
                                        showDialogDelete = true


                                    }
                                )
                            }
                        )
                    }
                }
            } else {
                Nothing(
                    text = "Bạn không có quyền truy cập chức năng này",
                    icon = Icons.Default.NotInterested,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }

    if (showErrorSheet) {
        ErrorModalBottomSheet(
            description = uiState.errorMessage.toString(),
            onDismiss = {
                showErrorSheet = false
            },

            )
    }
    if (showDialogDelete) {
        PassbookAppDialog(
            titleColor = MaterialTheme.colorScheme.error,
            title = "Xóa nhóm người dùng",
            message = "Bạn có chắc chắn muốn xóa nhóm người dùng này không?",
            onDismiss = {
                showDialogDelete = false
            },
            onConfirm = {
                viewModel.onAction(PermissionsSettingState.Action.OnDeleteGroup)
                showDialogDelete = false
                viewModel.getGroups()
            },
            confirmText = "Xóa",
            dismissText = "Đóng",

            )
    }
    if (showDialogUpdate) {
        Dialog(
            onDismissRequest = {
                showDialogUpdate = false
            },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(700.dp)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.extraLarge
                    )
                    .padding(20.dp)

            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {


                    Text(
                        text = "Thông tin chi tiết",
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
                            .padding(12.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)

                    ) {
                        PassbookTextField(
                            value = uiState.groupSelected.name,
                            onValueChange = {
                                viewModel.onAction(
                                    PermissionsSettingState.Action.OnChangeNameGroup(
                                        it
                                    )
                                )
                            },
                            labelText = "Tên nhóm",
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardOptions.Default.keyboardType,
                                imeAction = ImeAction.Next
                            )
                        )
                        PassbookTextField(
                            labelText = "Mô tả",
                            value = uiState.groupSelected.description,
                            onValueChange = {
                                viewModel.onAction(
                                    PermissionsSettingState.Action.OnChangeDescriptionGroup(
                                        it
                                    )
                                )
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardOptions.Default.keyboardType,
                                imeAction = ImeAction.Next
                            )
                        )

                        CheckboxGroupColumn(
                            title = "Quyền hạn",
                            options = uiState.permissions.map { it.description },
                            selectedOptions = uiState.groupSelected.permissions.map { it.description },
                            onOptionToggled = { option, isChecked ->
                                val permission =
                                    uiState.permissions.find { it.description == option }
                                permission?.let {
                                    if (isChecked) {
                                        viewModel.onAction(
                                            PermissionsSettingState.Action.OnChangePermissionGroup(
                                                uiState.groupSelected.permissions.plus(permission)
                                            )
                                        )
                                    } else {
                                        viewModel.onAction(
                                            PermissionsSettingState.Action.OnChangePermissionGroup(
                                                uiState.groupSelected.permissions.minus(permission)
                                            )
                                        )
                                    }
                                }

                            },

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
                                showDialogUpdate = false
                            },
                            backgroundColor = MaterialTheme.colorScheme.outline,
                            textColor = MaterialTheme.colorScheme.onPrimary,
                            enable = !uiState.isLoading
                        )
                        AppButton(
                            text = if (uiState.isUpdate) "Cập nhật" else "Tạo",
                            onClick = {
                                if (uiState.isUpdate) viewModel.onAction(PermissionsSettingState.Action.OnUpdateGroup) else
                                    viewModel.onAction(PermissionsSettingState.Action.OnCreateGroup)
                            },
                            enable = uiState.groupSelected.name.isNotBlank() && uiState.groupSelected.description.isNotBlank() && uiState.groupSelected.permissions.isNotEmpty() && !uiState.isLoading
                        )
                    }


                }
            }

        }
    }
}

@Composable
fun GroupList(
    modifier: Modifier = Modifier,
    groups: List<Group>,
    onClick: (Group) -> Unit,
    endAction: @Composable (Group) -> SwipeAction,
) {
    if (groups.isEmpty()) {

        Nothing(
            text = "Không có nhóm người dùng nào",
            icon = Icons.Default.People,
            modifier = modifier
        )
    } else {
        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            items(
                groups, key = { it -> it.id!! },
            ) { group ->
                SwipeableActionsBox(
                    modifier = Modifier
                        .padding(
                            8.dp,
                        )
                        .clip(MaterialTheme.shapes.extraLarge),
                    endActions = listOf(endAction(group))
                ) {
                    GroupSection(
                        group = group,
                        onItemClick = onClick
                    )
                }

            }

        }


    }
}


@Composable
fun GroupSection(
    group: Group,
    onItemClick: (Group) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraLarge)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.medium
            )
            .padding(18.dp)
            .clickable {
                onItemClick(group)
            }
    ) {


        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            DetailsRow(
                title = "Tên nhóm",
                text = group.name,
                icon = Icons.Default.DriveFileRenameOutline,
                titleColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.primary

            )

            DetailsRow(
                text = group.description,
                icon = Icons.Default.Description,
                title = "Mô tả",
                titleColor = MaterialTheme.colorScheme.outline,
                textColor = MaterialTheme.colorScheme.onBackground

            )

        }

    }
}
