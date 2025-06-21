package com.se104.passbookapp.ui.screen.user_manage

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.se104.passbookapp.R
import com.se104.passbookapp.data.model.User
import com.se104.passbookapp.navigation.UserDetail
import com.se104.passbookapp.ui.screen.components.ChipsGroupWrap
import com.se104.passbookapp.ui.screen.components.ErrorModalBottomSheet
import com.se104.passbookapp.ui.screen.components.HeaderDefaultView
import com.se104.passbookapp.ui.screen.components.LazyPagingSample
import com.se104.passbookapp.ui.screen.components.PassbookAppDialog
import com.se104.passbookapp.ui.screen.components.SearchField
import com.se104.passbookapp.ui.screen.components.TabWithPager
import com.se104.passbookapp.ui.theme.confirm
import com.se104.passbookapp.utils.hasPermission
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManageScreen(
    navController: NavController,
    viewModel: UserManageViewModel = hiltViewModel(),
    permission: List<String>,
) {

    val isSetActiveUser by rememberSaveable { mutableStateOf(permission.hasPermission("SET_ACTIVE_USER")) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showErrorSheet by rememberSaveable { mutableStateOf(false) }
    var showDialogUpdateStatus by rememberSaveable { mutableStateOf(false) }



    val users = remember(uiState.filter) {
        viewModel.getUsers(uiState.filter)
    }.collectAsLazyPagingItems()

    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.event.flowWithLifecycle(lifecycleOwner.lifecycle).collect { event ->
            when (event) {
                UserManageState.Event.ShowError -> {
                    showErrorSheet = true
                }

                is UserManageState.Event.NavigateToDetail -> {
                    navController.navigate(UserDetail(event.user))
                }

                is UserManageState.Event.ShowSuccessToast -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                UserManageState.Event.ShowToastUnEnableAction -> {
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
        viewModel.getGroups()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderDefaultView(
            text = "Quản lý tài khoản"
        )

            SearchField(
                searchInput = uiState.search,
                searchChange = {
                  viewModel.onAction(UserManageState.Action.OnSearch(it))

                },
                switchChange = {
                    when (it) {
                        true -> viewModel.onAction(UserManageState.Action.OnChangeOrder("desc"))
                        false -> viewModel.onAction(UserManageState.Action.OnChangeOrder("asc"))
                    }
                },
                filterChange = {
                    when (it) {
                        "Ngày tạo" -> viewModel.onAction(UserManageState.Action.OnChangeSortBy("createdAt"))
                        "Tên" -> viewModel.onAction(UserManageState.Action.OnChangeSortBy("fullName"))
                        else -> viewModel.onAction(UserManageState.Action.OnChangeSortBy("createdAt"))

                    }
                    Log.d("SavingTicketScreen", "filterChange: ${uiState.filter}")
                },
                filters = listOf("Ngày tạo", "Tên"),
                filterSelected = when (uiState.filter.sortBy) {
                   "createdAt" -> "Ngày tạo"
                    "fullName" -> "Tên"
                    else -> "Ngày tạo"
                },
                switchState = uiState.filter.order == "desc",
                placeHolder = "Tìm kiếm theo CCCD"
            )
            ChipsGroupWrap(
                modifier = Modifier.padding(8.dp),
                options = uiState.groups.map { it.description },
                selectedOption = uiState.groupName,
                onOptionSelected = { groupName ->
                    val group = uiState.groups.find { it.description == groupName }
                    group?.let {
                        viewModel.onAction(UserManageState.Action.OnSelectGroup(groupName = it.description, groupId = it.id!!))
                    }
                },
                containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
                isFlowLayout = false,
            )

            TabWithPager(
                modifier = Modifier.fillMaxWidth().weight(1f),
                tabs = listOf("Đang hoạt động", "Bị khóa"),
                pages = listOf(
                    {
                      LazyPagingSample(
                          modifier = Modifier.fillMaxSize(),
                          items = users,
                          textNothing = "Không có tài khoản nào",
                          iconNothing = Icons.Default.Person,
                          columns = 2,
                          key = {
                              it.id
                          },
                          itemContent = {
                              SwipeableActionsBox(
                                  modifier = Modifier.padding(8.dp).clip(MaterialTheme.shapes.extraLarge),
                                  endActions = listOf(
                                      SwipeAction(
                                          icon = rememberVectorPainter(Icons.Default.Lock),
                                          background = MaterialTheme.colorScheme.outline,
                                          onSwipe = {
                                              if(isSetActiveUser){
                                                  viewModel.onAction(UserManageState.Action.OnSelectUser(it.id))
                                                  showDialogUpdateStatus = true
                                              } else viewModel.onAction(UserManageState.Action.ShowToastUnEnable)


                                          }
                                      )
                                  )
                              ) {
                                  UserSection(
                                      user = it,
                                      onClick = {
                                          viewModel.onAction(UserManageState.Action.OnSelectUserToGoDetail(it))
                                      }
                                  )
                              }

                          }
                      )
                    },
                    {
                        LazyPagingSample(
                            modifier = Modifier.fillMaxSize(),
                            items = users,
                            textNothing = "Không có tài khoản nào",
                            iconNothing = Icons.Default.Person,
                            columns = 2,
                            key = {
                                it.id
                            },
                            itemContent = {
                                SwipeableActionsBox(
                                    modifier = Modifier.padding(8.dp).clip(MaterialTheme.shapes.extraLarge),
                                    endActions = listOf(
                                        SwipeAction(
                                            icon = rememberVectorPainter(Icons.Default.LockOpen),
                                            background = MaterialTheme.colorScheme.confirm,
                                            onSwipe = {
                                                if(isSetActiveUser){
                                                    viewModel.onAction(UserManageState.Action.OnSelectUser(it.id))
                                                    showDialogUpdateStatus = true
                                                } else viewModel.onAction(UserManageState.Action.ShowToastUnEnable)

                                            }
                                        )
                                    )
                                ) {
                                    UserSection(
                                        user = it,
                                        onClick = {
                                            viewModel.onAction(UserManageState.Action.OnSelectUserToGoDetail(it))
                                        }
                                    )
                                }

                            }
                        )
                    }
                ),

                onTabSelected = {
                    when(it){
                        0 -> viewModel.onAction(UserManageState.Action.OnChangeIsActive(true))
                        1 -> viewModel.onAction(UserManageState.Action.OnChangeIsActive(false))


                    }
                    Log.d("SavingTicketScreen", "onTabSelected: $it")
                    Log.d("SavingTicketScreenFilter", "onTabSelected: ${uiState.filter}")
                }
            )

    }

    if (showDialogUpdateStatus) {
        PassbookAppDialog(
            titleColor = if (uiState.filter.isActive) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.confirm,
            title = if (uiState.filter.isActive) "Khóa tài khoản" else "Mở khóa tài khoản",
            message = if (uiState.filter.isActive) "Bạn có chắc chắn muốn ẩn khóa tài khoản này không?" else "Bạn có chắc chắn muốn mở khóa tài khoản này không?",
            onDismiss = {
                showDialogUpdateStatus = false
            },
            onConfirm = {

                if (uiState.filter.isActive) {
                    viewModel.onAction(UserManageState.Action.SetInactiveUser)
                } else viewModel.onAction(UserManageState.Action.SetActiveUser)
                showDialogUpdateStatus = false


            },
            containerConfirmButtonColor = if (uiState.filter.isActive) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.confirm,
            labelConfirmButtonColor = MaterialTheme.colorScheme.onPrimary,
            confirmText = if (uiState.filter.isActive) "Khóa" else "Mở khóa",
            dismissText = "Đóng",
            showConfirmButton = true
        )
    }
    if (showErrorSheet) {
        ErrorModalBottomSheet(
            description = uiState.errorMessage.toString(),
            onDismiss = { showErrorSheet = false }
        )
    }

}

@Composable
fun UserSection(
    user: User,
    onClick: (User) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .width(162.dp)
            .height(216.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(color = MaterialTheme.colorScheme.background, shape = MaterialTheme.shapes.extraLarge)
            .clickable(
                onClick = { onClick.invoke(user) },
            ).padding(8.dp)

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            contentAlignment = Alignment.Center
        ){
            Image(
                painter = painterResource(id = R.drawable.avatar_placeholder),
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop

            )
        }
        Column(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = user.fullName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = user.citizenID,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}