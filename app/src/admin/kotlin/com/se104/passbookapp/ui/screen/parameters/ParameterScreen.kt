package com.se104.passbookapp.ui.screen.parameters

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavController
import com.se104.passbookapp.ui.screen.components.AppButton
import com.se104.passbookapp.ui.screen.components.ErrorModalBottomSheet
import com.se104.passbookapp.ui.screen.components.HeaderDefaultView
import com.se104.passbookapp.ui.screen.components.Loading
import com.se104.passbookapp.ui.screen.components.LoadingAnimation
import com.se104.passbookapp.ui.screen.components.Retry
import com.se104.passbookapp.ui.screen.components.text_field.PassbookTextField
import com.se104.passbookapp.ui.screen.components.text_field.PasswordTextField
import com.se104.passbookapp.utils.hasPermission

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParametersScreen(
    navController: NavController,
    viewModel: ParametersViewModel = hiltViewModel(),
    permissions: List<String>,
) {
    val isModify = permissions.hasPermission("UPDATE_PARAMETERS")


    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showErrorSheet by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(Unit) {
        viewModel.event.flowWithLifecycle(lifecycleOwner.lifecycle).collect { event ->
            when (event) {
                is ParametersState.Event.OnBack -> {
                    navController.popBackStack()
                }

                is ParametersState.Event.ShowError -> {
                    showErrorSheet = true
                }

                is ParametersState.Event.ShowToastSuccess -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getParameters()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderDefaultView(
            text = "Tham số hệ thống",
            onBack = {
                viewModel.onAction(ParametersState.Action.OnBack)
            }
        )

        when (uiState.parametersState) {
            is ParametersState.ParametersReceivedState.Error -> {
                val message =
                    (uiState.parametersState as ParametersState.ParametersReceivedState.Error).message
                Retry(
                    message = message,
                    onClicked = {
                        viewModel.getParameters()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            ParametersState.ParametersReceivedState.Loading -> {
                LoadingAnimation(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            ParametersState.ParametersReceivedState.Success -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .background(
                            MaterialTheme.colorScheme.background, MaterialTheme.shapes.medium
                        )
                        .padding(12.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        PassbookTextField(
                            value = uiState.parameters!!.minAge.toString(),
                            onValueChange = {
                                viewModel.onAction(
                                    ParametersState.Action.OnChangeMinAge(
                                        it.toIntOrNull()
                                    )
                                )
                            },
                            readOnly = !isModify,
                            modifier = Modifier.fillMaxWidth(),
                            labelText = "Tuổi tối thiểu đăng kí",
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                        )
                        PassbookTextField(
                            value = uiState.parameters!!.minTransactionAmount.toPlainString(),
                            onValueChange = {
                                viewModel.onAction(
                                    ParametersState.Action.OnChangeMinTransactionAmount(
                                        it.toBigDecimalOrNull()
                                    )
                                )
                            },
                            readOnly = !isModify,
                            modifier = Modifier.fillMaxWidth(),
                            labelText = "Hạn mức giao dịch tối thiểu",
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                        )
                        PassbookTextField(
                            value = uiState.parameters!!.maxTransactionAmount.toPlainString(),
                            onValueChange = {
                                viewModel.onAction(
                                    ParametersState.Action.OnChangeMaxTransactionAmount(
                                        it.toBigDecimalOrNull()
                                    )
                                )
                            },
                            readOnly = !isModify,
                            modifier = Modifier.fillMaxWidth(),
                            labelText = "Hạn mức giao dịch tối đa",
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                        )
                        PassbookTextField(
                            value = uiState.parameters!!.minSavingAmount.toPlainString(),
                            onValueChange = {
                                viewModel.onAction(
                                    ParametersState.Action.OnChangeMinSavingAmount(
                                        it.toBigDecimalOrNull()
                                    )
                                )
                            },
                            readOnly = !isModify,
                            modifier = Modifier.fillMaxWidth(),
                            labelText = "Hạn mức gửi sổ tiết kiểm tối thiểu",
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                        )

                    }
                    AppButton(
                        text = "Cập nhật",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        onClick = {
                            viewModel.onAction(ParametersState.Action.OnUpdate)
                        },

                        enable = isModify && !uiState.isLoading
                    )
                }
            }
        }
        if (showErrorSheet) {
            ErrorModalBottomSheet(
                description = uiState.error.toString(),
                onDismiss = {
                    showErrorSheet = false
                },
            )
        }

    }
}