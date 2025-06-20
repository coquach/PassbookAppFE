package com.se104.passbookapp.ui.screen.profile

import DetailsRow
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.se104.passbookapp.navigation.ChangePassword
import com.se104.passbookapp.ui.screen.components.HeaderDefaultView
import androidx.compose.runtime.getValue
import com.se104.passbookapp.ui.screen.components.AppButton
import com.se104.passbookapp.ui.screen.components.LoadingAnimation
import com.se104.passbookapp.ui.screen.components.Retry
import com.se104.passbookapp.utils.StringUtils

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        viewModel.event.flowWithLifecycle(lifecycleOwner.lifecycle).collect {
            when (it) {
                ProfileState.Event.OnBack -> {
                    navController.popBackStack()
                }

                ProfileState.Event.NavigateToChangePassword -> {
                    navController.navigate(ChangePassword)
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
            .padding(start = 16.dp, end = 16.dp, bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HeaderDefaultView(
            text = "Thông tin cá nhân",
            onBack = {
                viewModel.onAction(ProfileState.Action.OnBack)
            }
        )
        when(uiState.getMyInfoState){
            is ProfileState.GetMyInfoState.Error -> {
                val message = (uiState.getMyInfoState as ProfileState.GetMyInfoState.Error).message
                Retry(
                    message = message,
                    onClicked = {
                        viewModel.getMyInfo()
                    },
                    modifier = Modifier.fillMaxWidth().weight(1f)
                )
            }
            ProfileState.GetMyInfoState.Loading -> {
                LoadingAnimation(
                    modifier = Modifier.fillMaxWidth().weight(1f)
                )
            }
            is ProfileState.GetMyInfoState.Success -> {
                val user = (uiState.getMyInfoState as ProfileState.GetMyInfoState.Success).user
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
                        text = user.fullName,
                        icon = Icons.Default.Person,
                        titleColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.primary
                    )
                    DetailsRow(
                        title = "CCCD",
                        text = user.citizenID,
                        icon = Icons.Default.Tag,
                        titleColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.primary
                    )
                    DetailsRow(
                        title = "Vai trò",
                        text = user.groupName,
                        icon = Icons.Default.Groups,
                        titleColor = MaterialTheme.colorScheme.primary,
                        textColor = MaterialTheme.colorScheme.primary
                        )
                    DetailsRow(
                        title = "Email",
                        text = user.email,
                        icon = Icons.Default.Email,
                        titleColor = MaterialTheme.colorScheme.outline,
                        textColor = MaterialTheme.colorScheme.onBackground

                        )
                    DetailsRow(
                        title = "Số điện thoại",
                        text = user.phone,
                        icon = Icons.Default.Phone,
                        titleColor = MaterialTheme.colorScheme.outline,
                        textColor = MaterialTheme.colorScheme.onBackground
                    )

                    DetailsRow(
                        title = "Ngày sinh",
                        text = StringUtils.formatLocalDate(user.dateOfBirth)!!,
                        icon = Icons.Default.DateRange,
                        titleColor = MaterialTheme.colorScheme.outline,
                        textColor = MaterialTheme.colorScheme.onBackground
                    )
                    DetailsRow(
                        title = "Địa chỉ",
                        text = user.address,
                        icon = Icons.Default.Person,
                        titleColor = MaterialTheme.colorScheme.outline,
                        textColor = MaterialTheme.colorScheme.onBackground
                    )




                }
                Spacer(modifier = Modifier.weight(1f))
                AppButton(
                    text = "Thay đổi mật khẩu",
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.onAction(ProfileState.Action.NavigateToChangePassword)

                    },
                )
            }
        }

    }
}