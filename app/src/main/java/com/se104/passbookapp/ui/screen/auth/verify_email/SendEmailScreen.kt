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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.se104.passbookapp.R
import com.se104.passbookapp.navigation.Login
import com.se104.passbookapp.navigation.VerifyEmail
import com.se104.passbookapp.ui.screen.auth.signup.SignUp
import com.se104.passbookapp.ui.screen.components.AppButton
import com.se104.passbookapp.ui.screen.components.ErrorModalBottomSheet
import com.se104.passbookapp.ui.screen.components.text_field.ValidateTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendEmailScreen(
    navController: NavController,
    viewModel: SendEmailViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsState()
    var showErrorSheet by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        viewModel.event.flowWithLifecycle(lifecycleOwner.lifecycle).collect { event ->
            when (event) {
                is SendEmailState.Event.NavigateToVerifyEmail -> {
                    navController.navigate(VerifyEmail(event.email))
                }
                is SendEmailState.Event.ShowError -> {
                    showErrorSheet = true
                }
                is SendEmailState.Event.NavigateToLogin -> {
                    navController.navigate(Login) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                }
            }
        }

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)


    ){
        Text(
            text = stringResource(id = R.string.sign_up_desc),
            fontSize = 32.sp,
            lineHeight = 40.sp,
            fontWeight = FontWeight.Bold,

            color = MaterialTheme.colorScheme.primary

        )
        Image(
            painter = painterResource(id = R.drawable.send_email),
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
                    text = "Nhập email đăng kí tài khoản",
                    color = MaterialTheme.colorScheme.outline,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp,),

                    )
                ValidateTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.email,
                    onValueChange = {
                        viewModel.onAction(SendEmailState.Action.SetEmail(it))
                    },
                    labelText = stringResource(R.string.email),
                    errorMessage = uiState.emailError,
                    validate = {
                        viewModel.validate("email")
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    ),
                )
                Text(
                    text = stringResource(id = R.string.already_have_account),
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            viewModel.onAction(SendEmailState.Action.LoginClicked)
                        }
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyLarge

                )




            }

        }
        Spacer(modifier = Modifier.weight(0.5f))
        AppButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                viewModel.onAction(SendEmailState.Action.SendEmail)
            },
            text = "Gửi",
            enable = uiState.isValid && !uiState.isLoading
        )
    }

    if(showErrorSheet){
        ErrorModalBottomSheet(
            description = uiState.errorMessage.toString(),
            onDismiss = {
                showErrorSheet = false
            },

        )
    }
}