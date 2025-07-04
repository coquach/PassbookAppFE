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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.se104.passbookapp.ui.screen.components.DatePickerSample
import com.se104.passbookapp.R
import com.se104.passbookapp.navigation.Auth
import com.se104.passbookapp.navigation.Login
import com.se104.passbookapp.ui.screen.components.AppButton
import com.se104.passbookapp.ui.screen.components.DialogSample
import com.se104.passbookapp.ui.screen.components.ErrorModalBottomSheet
import com.se104.passbookapp.ui.screen.components.HeaderDefaultView
import com.se104.passbookapp.ui.screen.components.text_field.PasswordTextField
import com.se104.passbookapp.ui.screen.components.text_field.ValidateTextField
import com.se104.passbookapp.ui.theme.confirm
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    var showErrorSheet by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }





    LaunchedEffect(Unit) {
        viewModel.event.flowWithLifecycle(lifecycleOwner.lifecycle).collect { event ->
            when (event) {
                is SignUp.Event.NavigateToLogin -> {
                    navController.navigate(Login) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }


                is SignUp.Event.ShowError -> {
                    showErrorSheet = true
                }

                SignUp.Event.ShowSuccessDialog -> {
                    showSuccessDialog = true
                }

                SignUp.Event.NavigateToAuth -> {
                    navController.navigate(Auth) {
                        popUpTo(Auth) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }

                SignUp.Event.OnBack -> {
                    navController.popBackStack()
                }

            }

        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)


    ) {

        HeaderDefaultView(
            text = stringResource(R.string.sign_up),
            onBack = {
                viewModel.onAction(SignUp.Action.OnBack)
            }
        )


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            PasswordTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.password,
                onValueChange = {
                    viewModel.onAction(SignUp.Action.PasswordChanged(it))
                },
                errorMessage = uiState.passwordError,
                validate = {
                    viewModel.validate("password")
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                label = stringResource(R.string.password)
            )
            PasswordTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.confirmPassword,
                onValueChange = {
                    viewModel.onAction(SignUp.Action.ConfirmPasswordChanged(it))
                },
                errorMessage = uiState.confirmPasswordError,
                validate = {
                    viewModel.validate("confirmPassword")
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                label = stringResource(R.string.confirm_password)
            )
            ValidateTextField(
                value = uiState.fullName,
                onValueChange = {
                    viewModel.onAction(SignUp.Action.FullNameChanged(it))
                },
                labelText = stringResource(R.string.full_name),
                errorMessage = uiState.fullNameError,
                modifier = Modifier
                    .fillMaxWidth(),
                validate = { viewModel.validate("fullName") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            ValidateTextField(
                value = uiState.citizenId,
                onValueChange = {
                    viewModel.onAction(SignUp.Action.CitizenIdChanged(it))
                },
                labelText = stringResource(R.string.citizen_id),
                errorMessage = uiState.citizenIdError,
                modifier = Modifier
                    .fillMaxWidth(),
                validate = { viewModel.validate("citizenId") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                )
            )
            DatePickerSample(
                text = "Ngày sinh",
                selectedDate = uiState.dateOfBirth,
                onDateSelected = {
                    viewModel.onAction(SignUp.Action.DateOfBirthChanged(it))
                },
                maxDate = LocalDate.now()
            )
            ValidateTextField(
                value = uiState.phoneNumber,
                onValueChange = {
                    viewModel.onAction(SignUp.Action.PhoneNumberChanged(it))
                },
                labelText = stringResource(R.string.phone_number),
                errorMessage = uiState.phoneNumberError,
                modifier = Modifier
                    .fillMaxWidth(),
                validate = { viewModel.validate("phoneNumber") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )

            )
            ValidateTextField(
                value = uiState.address,
                onValueChange = {
                    viewModel.onAction(SignUp.Action.AddressChanged(it))
                },
                labelText = stringResource(R.string.address),
                errorMessage = uiState.addressError,
                modifier = Modifier
                    .fillMaxWidth(),
                validate = { viewModel.validate("address") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )


        }

        AppButton(
            text = stringResource(R.string.sign_up),
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                viewModel.onAction(SignUp.Action.SignUpClicked)
            },
            enable = uiState.isValid && !uiState.loading
        )

        if (showSuccessDialog) {

            DialogSample(
                title = "Đăng kí thành công",
                titleColor = MaterialTheme.colorScheme.confirm,
                message = "Bây giờ bạn có thể đăng nhập được rồi nha",
                onDismiss = {
                    viewModel.onAction(SignUp.Action.AuthClicked)
                    showSuccessDialog = false
                },
                onConfirm = {
                    viewModel.onAction(SignUp.Action.LoginClicked)
                    showSuccessDialog = false

                },
                containerConfirmButtonColor = MaterialTheme.colorScheme.confirm,
                labelConfirmButtonColor = MaterialTheme.colorScheme.onBackground,
                confirmText = "Đăng nhập",
                dismissText = "Trở về",
                showConfirmButton = true
            )


        }


    }



    if (showErrorSheet) {
        ErrorModalBottomSheet(
            description = uiState.error ?: "",
            onDismiss = { showErrorSheet = false },

            )
    }


}

