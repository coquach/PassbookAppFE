package com.se104.passbookapp.ui.screen.auth.login


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.se104.passbookapp.R
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.ui.navigation.Auth
import com.se104.passbookapp.ui.navigation.Home
import com.se104.passbookapp.ui.navigation.Login
import com.se104.passbookapp.ui.navigation.SignUp


import com.se104.passbookapp.ui.screen.auth.login.LoginViewModel
import com.se104.passbookapp.ui.screen.components.AppButton
import com.se104.passbookapp.ui.screen.components.ErrorModalBottomSheet
import com.se104.passbookapp.ui.screen.components.IconBackButton
import com.se104.passbookapp.ui.screen.components.PassbookTextField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    isCustomer: Boolean = false,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val email = viewModel.email.collectAsStateWithLifecycle()
    val password = viewModel.password.collectAsStateWithLifecycle()
    var showPassword by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val loading = remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }

    var rememberMe by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collectLatest {
            when (it) {
                LoginViewModel.LoginNavigationEvent.NavigateForgot -> {}
                LoginViewModel.LoginNavigationEvent.NavigateSignUp -> {
                    navController.navigate(SignUp) {
                        popUpTo(Auth) {
                            inclusive = true
                        }
                    }
                }

                LoginViewModel.LoginNavigationEvent.NavigateToHome -> {
                    navController.navigate(Home)
                }

                is LoginViewModel.LoginNavigationEvent.ShowError -> {
                    errorMessage = it.error
                    showDialog = true


                }
            }
        }
    }


    val uiState = viewModel.loginResponse.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)


    ) {
        IconBackButton(
            modifier = Modifier.align(Alignment.Start),
            onClick = {
                navController.navigateUp()
            })

        Text(
            text = stringResource(id = R.string.log_in_desc),
            fontSize = 32.sp,
            lineHeight = 40.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,

            )

        Spacer(modifier = Modifier.weight(0.5f))
        Column(
            modifier = Modifier
                .fillMaxWidth()
            ,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PassbookTextField(
                value = email.value,
                onValueChange = { viewModel.onEmailChanged(it) },
                labelText = stringResource(R.string.email),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                maxLines = 1
            )

            PassbookTextField(
                value = password.value,
                onValueChange = { viewModel.onPasswordChanged(it) },
                labelText = stringResource(R.string.password),
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {

                    IconButton(
                        onClick = {
                            showPassword = !showPassword
                        },
                    ) {
                        if (!showPassword) {
                            Icon(
                                imageVector = Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                maxLines = 1

            )

            Spacer(modifier = Modifier.size(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { }
                ) {
                    Checkbox(
                        checked = rememberMe,
                        onCheckedChange = { rememberMe = !it }
                    )
                    Text(
                        text = "Nhớ mật khẩu",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }


                TextButton(onClick = {
                    viewModel.onForgotPasswordClick()
                }) {
                    Text(
                        text = stringResource(R.string.forgot_password),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
            AppButton(
                onClick = viewModel::onLoginClick,
                text = stringResource(R.string.log_in),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.weight(0.5f))
        Text(
            text = stringResource(id = R.string.dont_have_account),
            modifier = Modifier
                .padding(8.dp)
                .clickable {
                    viewModel.onSignUpClick()
                }
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge

        )


    }
    if (showDialog) {
        ErrorModalBottomSheet(
            title = "Đã có lỗi xảy ra",
            description = errorMessage ?: "",
            onDismiss = { showDialog = false },

            )
    }
}



