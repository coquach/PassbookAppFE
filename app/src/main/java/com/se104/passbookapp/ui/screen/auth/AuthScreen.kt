package com.se104.passbookapp.ui.screen.auth

import android.Manifest
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.se104.passbookapp.R
import com.se104.passbookapp.navigation.Login
import com.se104.passbookapp.navigation.SendEmail
import com.se104.passbookapp.navigation.SignUp
import com.se104.passbookapp.ui.screen.components.AppButton

import com.se104.passbookapp.ui.theme.button


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AuthScreen(
    navController: NavController,
    isCustomer: Boolean = true,
) {
    


    Box(
        modifier = Modifier
            .fillMaxSize()

    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Bottom)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = "Logo",
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = stringResource(id = R.string.app_name),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
            )


            AppButton(
                text = stringResource(R.string.log_in),
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navController.navigate(Login)
                },
            )
            if (isCustomer) {
                AppButton(
                    text = stringResource(R.string.sign_up),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate(SendEmail)
                    },
                    backgroundColor = MaterialTheme.colorScheme.inversePrimary.copy(alpha = 0.6f),
                    textColor = MaterialTheme.colorScheme.primary,

                    )
            }



        }
    }
}


