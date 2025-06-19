package com.se104.passbookapp.ui.screen.auth.verify_email

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.domain.use_case.auth.SendOtpUseCase
import com.se104.passbookapp.ui.screen.components.text_field.validateField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendEmailViewModel @Inject constructor(
    private val sendOtpUseCase: SendOtpUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SendEmailState.UiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<SendEmailState.Event>()
    val event = _event.receiveAsFlow()

    fun validate(type: String) {
        val current = _uiState.value
        var emailError: String? = current.emailError
        when (type) {
            "email" -> {
                val emailError = validateField(
                    current.email.trim(),
                    "Email không hợp lệ"
                ) { it.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) }


            }



        }
        val isValid = current.email.isNotBlank() && emailError == null
        _uiState.update {
            it.copy(
                emailError = emailError,
                isValid = isValid
            )
        }
    }

    private fun sendEmail() {
        viewModelScope.launch {
            sendOtpUseCase.invoke(_uiState.value.email).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _event.send(SendEmailState.Event.NavigateToVerifyEmail(_uiState.value.email))
                    }
                    is ApiResponse.Failure -> {
                        _uiState.update { it.copy(isLoading = false, errorMessage = response.errorMessage) }
                        _event.send(SendEmailState.Event.ShowError)
                    }
                    is ApiResponse.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                }
            }
        }
    }

    fun onAction(action: SendEmailState.Action) {
        when (action) {
            is SendEmailState.Action.SetEmail -> {
                _uiState.update { it.copy(email = action.email) }
            }
            is SendEmailState.Action.SendEmail -> {
                sendEmail()
            }
            is SendEmailState.Action.LoginClicked -> {
                viewModelScope.launch {
                    _event.send(SendEmailState.Event.NavigateToLogin)
                }
            }
        }
    }

}

object SendEmailState {
    data class UiState(
        val email: String = "",
        val emailError: String?=null,
        val isLoading: Boolean = false,
        val errorMessage: String = "",
        val isValid: Boolean = false,
    )
    sealed interface Event{
        data class NavigateToVerifyEmail(val email: String): Event
        data object NavigateToLogin: Event
        data object ShowError: Event
    }
    sealed interface Action{
        data class SetEmail(val email: String): Action
        data object SendEmail: Action
        data object LoginClicked: Action

    }
}