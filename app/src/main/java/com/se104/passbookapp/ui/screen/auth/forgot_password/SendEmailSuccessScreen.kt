package com.se104.passbookapp.ui.screen.auth.forgot_password

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.se104.passbookapp.R
import com.se104.passbookapp.navigation.Auth
import com.se104.passbookapp.ui.screen.components.AppButton

@Composable
fun SendEmailSuccessScreen(
    navController: NavController,
){
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, androidx.compose.ui.Alignment.CenterVertically)
    ) {
        Image(
            painter = painterResource(id = R.drawable.send_email),
            contentDescription = null,
            modifier = Modifier.size(400.dp)
        )
        Text(
            text = "Gửi email đặt lại mật khẩu thành công",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.ExtraBold,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Hãy kiểm tra email của bạn để đăng nhập với mật khẩu mới",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center
        )
        
        AppButton(
            onClick = {
                navController.navigate(Auth) {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            },
            text = "Trờ về",
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colorScheme.outline,
            textColor = MaterialTheme.colorScheme.onPrimary
        )
    }
}