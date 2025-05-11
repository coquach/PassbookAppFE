package com.se104.passbookapp.ui.screen.auth.signup


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

import com.se104.passbookapp.R
import com.se104.passbookapp.ui.navigation.Auth
import com.se104.passbookapp.ui.navigation.Login
import com.se104.passbookapp.ui.screen.components.AppButton
import com.se104.passbookapp.ui.screen.components.DialogSample
import com.se104.passbookapp.ui.screen.components.ErrorModalBottomSheet
import com.se104.passbookapp.ui.screen.components.IconBackButton
import com.se104.passbookapp.ui.screen.components.PassbookTextField
import com.se104.passbookapp.ui.theme.confirm
import com.se104.passbookapp.utils.ValidateField
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel(),
) {

    val email = viewModel.email.collectAsStateWithLifecycle()
    val password = viewModel.password.collectAsStateWithLifecycle()
    val confirmPassword = viewModel.confirmPassword.collectAsStateWithLifecycle()


    val emailError = viewModel.emailError
    val passwordError = viewModel.passwordError
    val conFirmPasswordError = viewModel.confirmPasswordError

    var isTouched by remember { mutableStateOf(false) }


    var showPassword by remember { mutableStateOf(false) }
    val loading = remember { mutableStateOf(false) }
    var showErrorSheet by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showSuccessDialog by remember { mutableStateOf(false) }




    LaunchedEffect(true) {
        viewModel.event.collectLatest { event ->
            when (event) {
                is SignUpViewModel.SignUpEvent.NavigateLogin -> {
                    navController.navigate(Login) {
                        popUpTo(Auth) {
                            inclusive = true
                        }
                    }
                }


                is SignUpViewModel.SignUpEvent.ShowError -> {
                    errorMessage = event.error
                    showErrorSheet = true
                }

                SignUpViewModel.SignUpEvent.ShowSuccessDialog -> {
                    showSuccessDialog = true
                }
            }

        }
    }

    val uiState = viewModel.signUpResponse.collectAsStateWithLifecycle()


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
            text = stringResource(id = R.string.sign_up_desc),
            fontSize = 32.sp,
            lineHeight = 40.sp,
            fontWeight = FontWeight.Bold,

            color = MaterialTheme.colorScheme.primary

        )

        Spacer(modifier = Modifier.weight(0.5f))
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PassbookTextField(
                value = email.value,
                onValueChange = {
                    viewModel.onEmailChanged(it)
                    if (!isTouched) isTouched = true
                },
                labelText = stringResource(R.string.email),
                isError = emailError.value != null,
                errorText = emailError.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (isTouched && !focusState.isFocused) {
                            ValidateField(
                                email.value,
                                emailError,
                                "Email không hợp lệ"
                            ) { it.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")) }
                        }
                    },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                maxLines = 1
            )
            PassbookTextField(
                value = password.value,
                onValueChange = {
                    viewModel.onPasswordChanged(it)
                    if (!isTouched) isTouched = true
                },
                labelText = stringResource(R.string.password),
                isError = passwordError.value != null,
                errorText = passwordError.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (isTouched && !focusState.isFocused) {
                            ValidateField(
                                password.value,
                                passwordError,
                                "Mật khẩu phải có ít nhất 6 ký tự"
                            ) { it.length >= 6 }
                        }
                    },
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
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                maxLines = 1

            )
            PassbookTextField(
                value = confirmPassword.value,
                onValueChange = {
                    viewModel.onConfirmPasswordChanged(it)
                    if (!isTouched) isTouched = true
                },
                labelText = stringResource(R.string.confirm_password),
                isError = conFirmPasswordError.value != null,
                errorText = conFirmPasswordError.value,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (isTouched && !focusState.isFocused) {
                            ValidateField(
                                confirmPassword.value,
                                conFirmPasswordError,
                                "Mật khẩu không trùng khớp"
                            ) { it == password.value }
                        }
                    },
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
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                maxLines = 1

            )
            AppButton(
                text = stringResource(R.string.sign_up),
                modifier = Modifier.fillMaxWidth(),
                onClick = viewModel::onSignUpClick,
            )
        }






        Text(
            text = stringResource(id = R.string.already_have_account),
            modifier = Modifier
                .padding(8.dp)
                .clickable {
                    viewModel.onLoginClick()
                }
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyLarge

        )
        if (showSuccessDialog) {

            DialogSample(
                title = "Đăng kí thành công",
                titleColor = MaterialTheme.colorScheme.confirm,
                message = "Bây giờ bạn có thể đăng nhập được rồi nha",
                onDismiss = {
                    viewModel.onLoginClick()
                    showSuccessDialog = false
                },
                onConfirm = {
                    viewModel.onLoginClick()
                    showSuccessDialog = false

                },
                containerConfirmButtonColor = MaterialTheme.colorScheme.confirm,
                labelConfirmButtonColor = MaterialTheme.colorScheme.onBackground,
                confirmText = "Đăng nhập",
                dismissText = "Trở về",
                showConfirmButton = true
            )


        }

        if (showErrorSheet) {
            ErrorModalBottomSheet(
                title = "Đã có lỗi xảy ra",
                description = errorMessage ?: "",
                onDismiss = { showErrorSheet = false },

                )
        }
    }


}

