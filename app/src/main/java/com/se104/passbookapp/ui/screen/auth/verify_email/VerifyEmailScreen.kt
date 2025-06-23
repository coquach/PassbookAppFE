package com.se104.passbookapp.ui.screen.auth.verify_email

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.se104.passbookapp.R
import com.se104.passbookapp.navigation.Login
import com.se104.passbookapp.navigation.SignUp
import com.se104.passbookapp.navigation.VerifyEmail
import com.se104.passbookapp.ui.screen.components.AppButton
import com.se104.passbookapp.ui.screen.components.ErrorModalBottomSheet
import com.se104.passbookapp.ui.screen.components.HeaderDefaultView
import com.se104.passbookapp.ui.screen.components.OTPTextFields
import com.se104.passbookapp.ui.screen.components.text_field.ValidateTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyEmailScreen(
    navController: NavController,
    viewModel: VerifyEmailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    var showErrorSheet by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        viewModel.event.flowWithLifecycle(lifecycleOwner.lifecycle).collect { event ->
            when (event) {
                is VerifyEmailState.Event.NavigateToSignUp -> {
                    navController.navigate(SignUp(event.email))
                }

                is VerifyEmailState.Event.ShowError -> {
                    showErrorSheet = true
                }

                is VerifyEmailState.Event.OnBack -> {
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
            text = "Xác thực email",
            onBack = {
                viewModel.onAction(VerifyEmailState.Action.OnBack)
            }
        )
        Image(
            painter = painterResource(id = R.drawable.send_otp),
            contentDescription = "",
            modifier = Modifier.size(240.dp)
        )
        Card(
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Nhập OTP được gửi đến email",
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),

                    )


                OTPTextFields(
                    length = 6,
                    onFilled = {
                        viewModel.onAction(VerifyEmailState.Action.OnOtpChange(it))
                    }
                )


            }

        }
        Spacer(modifier = Modifier.weight(0.5f))
        AppButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                viewModel.onAction(VerifyEmailState.Action.VerifyEmail)
            },
            text = "Xác nhận",
            enable = uiState.isValid && !uiState.isLoading
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
}