package com.se104.passbookapp.ui.screen.auth.login


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
import com.se104.passbookapp.R
import com.se104.passbookapp.navigation.Auth
import com.se104.passbookapp.navigation.ForgotPassword
import com.se104.passbookapp.navigation.Home
import com.se104.passbookapp.navigation.SendEmail
import com.se104.passbookapp.navigation.SignUp
import com.se104.passbookapp.ui.screen.components.AppButton
import com.se104.passbookapp.ui.screen.components.ErrorModalBottomSheet
import com.se104.passbookapp.ui.screen.components.IconCustomButton
import com.se104.passbookapp.ui.screen.components.text_field.PasswordTextField
import com.se104.passbookapp.ui.screen.components.text_field.ValidateTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    isCustomer: Boolean = true,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showErrorSheet by remember { mutableStateOf(false) }



    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        viewModel.event.flowWithLifecycle(lifecycleOwner.lifecycle).collect {
            when (it) {
                Login.Event.NavigateForgot -> {
                    navController.navigate(ForgotPassword)
                }

                Login.Event.NavigateSignUp -> {
                    navController.navigate(SendEmail) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }

                Login.Event.NavigateHome -> {
                    navController.navigate(Home){
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }

                Login.Event.ShowError -> {
                    showErrorSheet = true
                }


            }
        }
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)


    ) {


        Text(
            text = stringResource(id = R.string.log_in_desc),
            fontSize = 32.sp,
            lineHeight = 40.sp,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,

            )

        Image(
            painter = painterResource(id = R.drawable.login),
            contentDescription = "",
            modifier = Modifier.size(240.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ValidateTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.email,
                onValueChange = {
                    viewModel.onAction(Login.Action.OnEmailChanged(it))
                },
                labelText = stringResource(R.string.email),
                errorMessage = uiState.emailError,
                validate = {
                    viewModel.validate("email")
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
            )

            PasswordTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.password,
                onValueChange = {
                    viewModel.onAction(Login.Action.OnPasswordChanged(it))
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


            if (isCustomer) {

                TextButton(
                    onClick = {
                        viewModel.onAction(Login.Action.OnForgotClick)
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = stringResource(R.string.forgot_password),
                        color = MaterialTheme.colorScheme.outline,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }

                Text(
                    text = stringResource(id = R.string.dont_have_account),
                    modifier = Modifier
                        .clickable {
                            viewModel.onAction(Login.Action.OnSignUpClick)
                        }
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge

                )
            }

            Spacer(modifier = Modifier.weight(1f))
            AppButton(
                onClick = {
                    viewModel.onAction(Login.Action.OnLoginClick)
                },
                text = stringResource(R.string.log_in),
                modifier = Modifier.fillMaxWidth(),
                enable = uiState.isValid && !uiState.isLoading
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



