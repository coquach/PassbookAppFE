package com.se104.passbookapp.ui.screen.auth.login


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.request.LoginRequest
import com.se104.passbookapp.domain.use_case.auth.LoginUseCase
import com.se104.passbookapp.ui.screen.components.text_field.validateField
import com.se104.passbookapp.utils.hasPermission
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
   private val loginUseCase: LoginUseCase
) : ViewModel() {
private val _uiState = MutableStateFlow(Login.UiState())
    val uiState : StateFlow<Login.UiState> get() = _uiState.asStateFlow()

    private val _event = Channel<Login.Event>()
    val event = _event.receiveAsFlow()

    private fun login(request: LoginRequest) {
        viewModelScope.launch {
            loginUseCase.invoke(request).collect {response->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _event.send(Login.Event.NavigateHome)
                    }
                    is ApiResponse.Failure -> {
                        _uiState.update { it.copy(isLoading = false, error = response.errorMessage) }
                        _event.send(Login.Event.ShowError)
                    }
                    is ApiResponse.Loading -> {
                        _uiState.update { it.copy(isLoading = false) }
                    }

                }
            }
        }
    }

    fun validate(type: String) {
        val current = _uiState.value
        var emailError: String? = current.emailError
        var passwordError: String? = current.passwordError
        when (type) {
            "email" -> {
                val emailError = validateField(
                    current.email.trim(),
                    "Email không hợp lệ"
                ) { it.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) }


            }

            "password" -> {
                val passwordError = validateField(
                    current.password.trim(),
                    "Mật khẩu phải có ít nhất 6 ký tự"
                ) { it.length >= 6 }


            }

        }
        val isValid = emailError == null && passwordError == null
        _uiState.update {
            it.copy(
                emailError = emailError,
                passwordError = passwordError,
                isValid = isValid
            )
        }
    }

    fun onAction(action: Login.Action){
        when(action){
            is Login.Action.OnEmailChanged -> {
                _uiState.value = _uiState.value.copy(email = action.email)
            }
            is Login.Action.OnPasswordChanged -> {
                _uiState.value = _uiState.value.copy(password = action.password)
            }
            is Login.Action.OnLoginClick -> {
                login(LoginRequest(_uiState.value.email, _uiState.value.password))
            }
            is Login.Action.OnSignUpClick -> {
                viewModelScope.launch {
                    _event.send(Login.Event.NavigateSignUp)
                }
            }
            is Login.Action.OnForgotClick -> {
                viewModelScope.launch {
                    _event.send(Login.Event.NavigateForgot)
                }
            }


        }
    }



}

object Login{
    data class UiState(
        val isLoading: Boolean = false,
        val email: String = "",
        val password: String = "",
        val emailError: String?= null,
        val passwordError: String?= null,
        val error: String?= null,
        val isCustomer: Boolean = false,
        val isValid: Boolean = false
    )
    sealed interface Event{
        data object ShowError : Event
        data object NavigateSignUp : Event
        data object NavigateHome : Event
        data object NavigateForgot : Event


    }
    sealed interface Action {
        data object OnLoginClick : Action
        data object OnSignUpClick : Action
        data object OnForgotClick : Action
        data class OnEmailChanged(val email: String) : Action
        data class OnPasswordChanged(val password: String) : Action


    }
}
