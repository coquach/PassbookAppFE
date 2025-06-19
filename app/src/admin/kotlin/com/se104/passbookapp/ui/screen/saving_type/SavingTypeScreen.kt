package com.se104.passbookapp.ui.screen.saving_type

import CardSample
import DetailsRow
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.se104.passbookapp.data.model.SavingType
import com.se104.passbookapp.ui.screen.components.AppButton
import com.se104.passbookapp.ui.screen.components.ErrorModalBottomSheet
import com.se104.passbookapp.ui.screen.components.HeaderDefaultView
import com.se104.passbookapp.ui.screen.components.MyFloatingActionButton
import com.se104.passbookapp.ui.screen.components.Nothing
import com.se104.passbookapp.ui.screen.components.PassbookAppDialog
import com.se104.passbookapp.ui.screen.components.TabWithPager
import com.se104.passbookapp.ui.screen.components.text_field.PassbookTextField
import com.se104.passbookapp.ui.theme.confirm
import com.se104.passbookapp.utils.hasAllPermissions
import com.se104.passbookapp.utils.hasPermission
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavingTypeScreen(
    viewModel: SavingTypeViewModel = hiltViewModel(),
    permissions: List<String>,
) {
    val isView = permissions.hasAllPermissions("VIEW_ACTIVE_SAVINGTYPES", "VIEW_INACTIVE_SAVINGTYPES")
    val isModify = permissions.hasAllPermissions("CREATE_SAVINGTYPE", "UPDATE_SAVINGTYPE")
    val isDelete = permissions.hasPermission("DELETE_SAVINGTYPE")
    val isSetActive = permissions.hasPermission("SET_ACTIVE_SAVINGTYPE")


    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showErrorSheet by rememberSaveable { mutableStateOf(false) }
    var showDialogUpdate by rememberSaveable { mutableStateOf(false) }
    var showDialogStatus by rememberSaveable { mutableStateOf(false) }
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }


    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        viewModel.event.flowWithLifecycle(lifecycleOwner.lifecycle).collect {
            when (it) {
                SavingTypeState.Event.ShowError -> {
                    showErrorSheet = true
                }
                SavingTypeState.Event.ShowToastUnEnableAction -> {
                    Toast.makeText(
                        context,
                        "Bạn không có quyền thực hiện thao tác này",
                        Toast.LENGTH_SHORT

                    ).show()
                }
                is SavingTypeState.Event.ShowToastSuccess -> {
                    Toast.makeText(
                        context,
                        it.message,
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    Scaffold(
        floatingActionButton = {
            MyFloatingActionButton(
                onClick = {
                    if(isModify){
                        viewModel.onAction(SavingTypeState.Action.OnSelectedSavingType(SavingType()))
                        viewModel.onAction(SavingTypeState.Action.OnUpdateStatus(false))
                        showDialogUpdate = true
                    }else{
                        viewModel.onAction(SavingTypeState.Action.ShowToast)
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
                )
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)

        ) {
            HeaderDefaultView(
                text = "Loại tiết kiệm"
            )
            if (isView){
                LaunchedEffect(Unit) {
                    viewModel.getActiveSavingTypes()
                    viewModel.getInActiveSavingTypes()
                }
                TabWithPager(
                    tabs = listOf("Đang hiển thị", "Đã ẩn"),
                    pages = listOf(
                        {
                            SavingTypeListSection(
                                savingTypes = uiState.activeSavingTypes,
                                onClick = {
                                    viewModel.onAction(SavingTypeState.Action.OnSelectedSavingType(it))
                                    viewModel.onAction(SavingTypeState.Action.OnUpdateStatus(true))
                                    showDialogUpdate = true
                                },
                                startAction = { it ->
                                    SwipeAction(
                                        icon = rememberVectorPainter(Icons.Default.VisibilityOff),
                                        background = MaterialTheme.colorScheme.outline,
                                        onSwipe = {
                                            if(isSetActive){
                                                viewModel.onAction(
                                                    SavingTypeState.Action.OnSelectedSavingType(
                                                        it
                                                    )
                                                )
                                                viewModel.onAction(SavingTypeState.Action.OnHideStatus(true))
                                                showDialogStatus = true
                                            } else viewModel.onAction(SavingTypeState.Action.ShowToast)

                                        }
                                    )
                                },
                                endAction = { it ->
                                    SwipeAction(
                                        icon = rememberVectorPainter(Icons.Default.Delete),
                                        background = MaterialTheme.colorScheme.error,
                                        onSwipe = {
                                            if(isDelete){
                                                viewModel.onAction(
                                                    SavingTypeState.Action.OnSelectedSavingType(
                                                        it
                                                    )
                                                )
                                                showDeleteDialog = true
                                            } else viewModel.onAction(SavingTypeState.Action.ShowToast)

                                        }
                                    )
                                }
                            )
                        },
                        {
                            SavingTypeListSection(
                                savingTypes = uiState.inactiveSavingTypes,
                                onClick = {
                                    viewModel.onAction(SavingTypeState.Action.OnSelectedSavingType(it))
                                    viewModel.onAction(SavingTypeState.Action.OnUpdateStatus(true))
                                    showDialogUpdate = true
                                },
                                startAction = { it ->
                                    SwipeAction(
                                        icon = rememberVectorPainter(Icons.Default.Visibility),
                                        background = MaterialTheme.colorScheme.confirm,
                                        onSwipe = {
                                            if(isSetActive){
                                                viewModel.onAction(
                                                    SavingTypeState.Action.OnSelectedSavingType(
                                                        it
                                                    )
                                                )
                                                viewModel.onAction(SavingTypeState.Action.OnHideStatus(false))
                                                showDialogStatus = true
                                            } else viewModel.onAction(SavingTypeState.Action.ShowToast)

                                        }
                                    )
                                },
                                endAction = { it ->
                                    SwipeAction(
                                        icon = rememberVectorPainter(Icons.Default.Delete),
                                        background = MaterialTheme.colorScheme.error,
                                        onSwipe = {
                                            if(isDelete){
                                                viewModel.onAction(
                                                    SavingTypeState.Action.OnSelectedSavingType(
                                                        it
                                                    )
                                                )
                                                showDeleteDialog = true
                                            } else viewModel.onAction(SavingTypeState.Action.ShowToast)

                                        }
                                    )
                                }
                            )
                        }
                    ),
                    modifier = Modifier.fillMaxWidth().weight(1f),
                    scrollable = false,
                    onTabSelected = {

                    }
                )
            }
            else {
                Nothing(
                    text = "Bạn không có quyền xem danh sách loại tiết kiệm",
                    icon = Icons.Default.Groups,
                    modifier = Modifier.fillMaxWidth().weight(1f)
                )
            }



        }
        if (showErrorSheet) {
            ErrorModalBottomSheet(
                description = uiState.errorMessage.toString(),
                onDismiss = { showErrorSheet = false }
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
                        .background(
                            MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(30.dp)

                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {


                        Text(
                            text = "Thông tin",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                        PassbookTextField(
                            labelText = "Tên loại tiết kiệm",
                            value = uiState.savingTypeSelected.typeName,
                            onValueChange = {
                                viewModel.onAction(SavingTypeState.Action.OnTypeNameChange(it))
                            },
                            singleLine = true
                        )
                        PassbookTextField(
                            labelText = "Thời hạn",
                            value = uiState.savingTypeSelected.duration.toString(),
                            onValueChange = {
                                viewModel.onAction(SavingTypeState.Action.OnDurationChange(it.toIntOrNull()))
                            },
                            singleLine = true
                        )
                        PassbookTextField(
                            labelText = "Lãi suaat",
                            value = uiState.savingTypeSelected.interestRate.toString(),
                            onValueChange = {
                                viewModel.onAction(SavingTypeState.Action.OnInterestRateChange(it.toBigDecimalOrNull()))
                            },
                            singleLine = true
                        )
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
                        ) {
//                            AButton(
//                                onClick = {
//                                    showDialogUpdate = false
//                                },
//                                colors = ButtonDefaults.buttonColors(
//                                    containerColor = MaterialTheme.colorScheme.outline
//                                ),
//                                modifier = Modifier.heightIn(48.dp),
//                                shape = RoundedCornerShape(12.dp)
//
//
//                            ) {
//                                Text(text = "Đóng", modifier = Modifier.padding(horizontal = 16.dp))
//                            }
                            AppButton(
                                text = "Đóng",
                                onClick = {
                                    showDialogUpdate = false
                                },
                                backgroundColor = MaterialTheme.colorScheme.outline,
                                textColor = MaterialTheme.colorScheme.onPrimary,
                                enable = isModify && !uiState.isLoading
                            )
                            AppButton(
                                text = if (uiState.isUpdate) "Cập nhật" else "Tạo",
                                onClick = {
                                    if (uiState.isUpdate) {
                                        viewModel.onAction(SavingTypeState.Action.OnUpdateSavingType)

                                    } else viewModel.onAction(SavingTypeState.Action.OnCreateSavingType)

                                    showDialogUpdate = false
                                }
                                ,
                                enable = isModify && !uiState.isLoading
                            )

                            Button(
                                onClick = {

                                    if (uiState.isUpdate) {
                                        viewModel.onAction(SavingTypeState.Action.OnUpdateSavingType)

                                    } else viewModel.onAction(SavingTypeState.Action.OnCreateSavingType)

                                    showDialogUpdate = false
                                },
                                modifier = Modifier.heightIn(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                enabled =  !uiState.isLoading
                            ) {
                                Text(
                                    text = if (uiState.isUpdate) "Cập nhật" else "Tạo",
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
        if (showDialogStatus) {
            PassbookAppDialog(
                title = if (uiState.isHide) "Ẩn loại tiết kiệm" else "Hiện loại tiết kiệm",
                message = if (uiState.isHide) "Bạn có chắc chắn muốn ẩn loại tiết kiệm này không?" else "Bạn có chắc chắn muốn hiện loại tiết kiệm này không?",
                onDismiss = {
                    showDialogStatus = false
                },
                onConfirm = {

                    if (uiState.isHide) {
                        viewModel.onAction(SavingTypeState.Action.OnSetActiveSavingType(false))
                    } else viewModel.onAction(SavingTypeState.Action.OnSetActiveSavingType(true))
                    showDialogStatus = false

                },
                confirmText = if (uiState.isHide) "Ẩn" else "Hiện",
                dismissText = "Đóng",
                showConfirmButton = true
            )
        }
        if (showDeleteDialog) {
            PassbookAppDialog(
                title = "Xóa loại tiết kiệm",
                message = "Bạn có chắc chắn muốn loại tiết kiệm này không?",
                onDismiss = {
                    showDeleteDialog = false
                },
                onConfirm = {

                    viewModel.onAction(SavingTypeState.Action.OnDeleteSavingType)
                    showDeleteDialog = false


                },
                confirmText = "Xóa",
                dismissText = "Đóng",
                showConfirmButton = true
            )
        }
    }
}


@Composable
fun SavingTypeListSection(
    savingTypes: List<SavingType>,
    onClick: (SavingType) -> Unit,
    startAction: @Composable (SavingType) -> SwipeAction,
    endAction: @Composable (SavingType) -> SwipeAction,
) {
    if (savingTypes.isEmpty()) {

        Nothing(
            text = "Không có nhà cung cấp nào",
            icon = Icons.Default.Groups,
            modifier = Modifier.fillMaxSize()
        )
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            items(
                savingTypes, key = { it -> it.id!! },
            ) { savingType ->
                SwipeableActionsBox(
                    modifier = Modifier
                        .padding(
                            8.dp,
                        )
                        .clip(RoundedCornerShape(12.dp)),
                    startActions = listOf(startAction(savingType)),
                    endActions = listOf(endAction(savingType))
                ) {
                    SavingTypeCard(
                        savingType = savingType,
                        onClick = onClick
                    )
                }

            }

        }


    }
}


@Composable
fun SavingTypeCard(savingType: SavingType, onClick: (SavingType) -> Unit) {
    CardSample(item = savingType, onClick = onClick) {
        SavingDetails(savingType = savingType)
    }
}


@Composable
fun SavingDetails(savingType: SavingType) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Savings,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(80.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DetailsRow(
                title = "Loại tiết kiệm",
                icon = Icons.Default.Category,
                text = savingType.typeName,

            )
            DetailsRow(
                title = "Kỳ hạn",
                icon = Icons.Default.DateRange,
                text = "${savingType.duration} tháng"
            )
            DetailsRow(
                title = "Lãi suất",
                icon = Icons.Default.MonetizationOn,
                text = "${savingType.interestRate}%"
            )
        }
    }
}




