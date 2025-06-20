package com.se104.passbookapp.ui.screen.auth.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.domain.use_case.auth.ForgotPasswordUseCase
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
class ForgotPasswordViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ForgotPasswordState.UiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<ForgotPasswordState.Event>()
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

    private fun forgotPassword() {
        viewModelScope.launch {
            forgotPasswordUseCase.invoke(_uiState.value.email).collect { response ->
                when (response) {
                    is ApiResponse.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }

                    is ApiResponse.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _event.send(ForgotPasswordState.Event.NavigateToSuccess)
                    }

                    is ApiResponse.Failure -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = response.errorMessage
                            )
                        }
                        _event.send(ForgotPasswordState.Event.ShowError)
                    }


                }
            }
        }
    }

    fun onAction(action: ForgotPasswordState.Action) {
        when (action) {
            is ForgotPasswordState.Action.OnBack -> {
                viewModelScope.launch {
                    _event.send(ForgotPasswordState.Event.OnBack)
                }
            }

            is ForgotPasswordState.Action.OnChangeEmail -> {
                _uiState.update { it.copy(email = action.email) }
            }

            is ForgotPasswordState.Action.OnForgot -> {
                forgotPassword()
            }
        }
    }
}

object ForgotPasswordState {
    data class UiState(
        val isLoading: Boolean = false,
        val email: String = "",
        val emailError: String? = null,
        val errorMessage: String? = null,
        val isValid: Boolean = false,
    )

    sealed interface Event {
        data object OnBack : Event
        data object NavigateToSuccess : Event
        data object ShowError : Event
    }

    sealed interface Action {
        data class OnChangeEmail(val email: String) : Action
        data object OnForgot : Action
        data object OnBack : Action
    }

}