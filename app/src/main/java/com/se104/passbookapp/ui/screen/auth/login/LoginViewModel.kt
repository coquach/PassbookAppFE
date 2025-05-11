package com.se104.passbookapp.ui.screen.auth.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel

import com.se104.passbookapp.BaseViewModel
import com.se104.passbookapp.CoroutinesErrorHandler
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.request.LoginRequest
import com.se104.passbookapp.data.dto.response.TokenResponse
import com.se104.passbookapp.data.repository.AuthRepository
import com.se104.passbookapp.di.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
) : BaseViewModel() {
    private val _loginResponse = MutableStateFlow<ApiResponse<TokenResponse>>(ApiResponse.Loading)
    val loginResponse = _loginResponse.asStateFlow()

    private val _navigationEvent = Channel<LoginNavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()


    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    fun onEmailChanged(username: String) {
        _email.value = username
    }


    fun onPasswordChanged(password: String) {
        _password.value = password
    }

    private fun login(auth: LoginRequest, coroutinesErrorHandler: CoroutinesErrorHandler) =
        baseRequest(
            _loginResponse,
            coroutinesErrorHandler,
            onSuccess = {
                viewModelScope.launch {
                    tokenManager.saveToken(it.accessToken, it.refreshToken)
                    _navigationEvent.send(LoginNavigationEvent.NavigateToHome)

                }
            },
            onFailure = {
                viewModelScope.launch {
                    _navigationEvent.send(LoginNavigationEvent.ShowError(it))
                }
            },
            request = {
                authRepository.login(auth)
            }
        )

    fun onLoginClick() {
            login(
                LoginRequest(_email.value, _password.value, ""),
                coroutinesErrorHandler = errorHandler,
            )
    }

    fun onSignUpClick() {
        viewModelScope.launch {
            _navigationEvent.send(LoginNavigationEvent.NavigateSignUp)
        }
    }

    fun onForgotPasswordClick() {
        viewModelScope.launch {
            _navigationEvent.send(LoginNavigationEvent.NavigateForgot)
        }
    }


    private val errorHandler = object : CoroutinesErrorHandler {
        override fun onError(message: String) {
            viewModelScope.launch {
                _navigationEvent.send(LoginNavigationEvent.ShowError(message))
            }

        }
    }

    sealed class LoginNavigationEvent {
        data class ShowError(val error: String) : LoginNavigationEvent()
        data object NavigateSignUp : LoginNavigationEvent()
        data object NavigateToHome : LoginNavigationEvent()
        data object NavigateForgot : LoginNavigationEvent()
    }


}
