package com.se104.passbookapp.ui.screen.auth.signup

import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.viewModelScope
import com.se104.passbookapp.BaseViewModel
import com.se104.passbookapp.CoroutinesErrorHandler
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.request.RegisterRequest
import com.se104.passbookapp.data.dto.response.TokenResponse
import com.se104.passbookapp.data.repository.AuthRepository
import com.se104.passbookapp.ui.screen.auth.login.LoginViewModel.LoginNavigationEvent
import com.se104.passbookapp.utils.ValidateField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _signUpResponse = MutableStateFlow<ApiResponse<TokenResponse>>(ApiResponse.Loading)
    val signUpResponse = _signUpResponse.asStateFlow()

    private val _event = Channel<SignUpEvent>()
    val event = _event.receiveAsFlow()


    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword = _confirmPassword.asStateFlow()


    fun onEmailChanged(email: String) {
        _email.value = email
    }

    fun onPasswordChanged(password: String) {
        _password.value = password
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
    }


    var emailError = mutableStateOf<String?>(null)
    var passwordError = mutableStateOf<String?>(null)
    var confirmPasswordError = mutableStateOf<String?>(null)


    fun validate(): Boolean {
        var isValid = true

        isValid = ValidateField(
            email.value,
            emailError,
            "Email không hợp lệ"
        ) { it.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$")) } && isValid

        isValid = ValidateField(
            password.value,
            passwordError,
            "Mật khẩu phải có ít nhất 6 ký tự"
        ) { it.length >= 6 } && isValid

        isValid = ValidateField(
            confirmPassword.value,
            confirmPasswordError,
            "Mật khẩu không trùng khớp"
        ) { it == password.value } && isValid

        return isValid
    }


    private fun signUp(request: RegisterRequest, coroutinesErrorHandler: CoroutinesErrorHandler) {

           baseRequest(
               _signUpResponse,
               coroutinesErrorHandler,
               onSuccess = {
                   viewModelScope.launch {
                       _event.send(SignUpEvent.ShowSuccessDialog)

                   }
               },
               onFailure = {
                   viewModelScope.launch {
                       _event.send(SignUpEvent.ShowError(it))
                   }
               },
               request = {
                   authRepository.register(request)
               }
           )


    }


    fun onSignUpClick(){

    }

    fun onLoginClick() {
        viewModelScope.launch {
            _event.send(SignUpEvent.NavigateLogin)
        }
    }

    private val errorHandler = object : CoroutinesErrorHandler {
        override fun onError(message: String) {
            viewModelScope.launch {
                _event.send(SignUpEvent.ShowError(message))
            }

        }
    }

    sealed class SignUpEvent {
        data class ShowError(val error: String) : SignUpEvent()
        data object NavigateLogin : SignUpEvent()
        data object ShowSuccessDialog : SignUpEvent()
    }


}