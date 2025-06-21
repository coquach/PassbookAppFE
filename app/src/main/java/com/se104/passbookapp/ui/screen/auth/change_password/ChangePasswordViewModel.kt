package com.se104.passbookapp.ui.screen.auth.change_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.domain.use_case.user.ChangePasswordUseCase
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
class ChangePasswordViewModel @Inject constructor(
    private val changePasswordUseCase: ChangePasswordUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ChangePasswordState.UiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<ChangePasswordState.Event>()
    val event = _event.receiveAsFlow()

    fun validate(type: String) {
        val current = _uiState.value

        var oldPasswordError: String? = current.oldPasswordError
        var newPasswordError: String? = current.newPasswordError


        when (type) {

            "oldPassword" -> {
                oldPasswordError = validateField(
                    current.oldPassword.trim(),
                    "Mật khẩu phải có ít nhất 6 ký tự"
                ) { it.length >= 6 }

            }

            "newPassword" -> {
                newPasswordError = validateField(
                    current.newPassword.trim(),
                    "Mật khẩu phải có ít nhất 6 ký tự và khác mật khẩu cũ"

                ) { it.length >= 6 && current.oldPassword != it }
            }


        }
        val isValid = current.oldPassword.isNotBlank() && current.newPassword.isNotBlank() && oldPasswordError == null && newPasswordError == null
        _uiState.update {
            it.copy(
                oldPasswordError = oldPasswordError,
                newPasswordError = newPasswordError,
                isValid = isValid
            )
        }
    }


    private fun changePassword() {
        viewModelScope.launch {
            changePasswordUseCase.invoke(oldPassword = _uiState.value.oldPassword, newPassword =  _uiState.value.newPassword)
                .collect { response ->
                    when (response) {
                        is ApiResponse.Loading -> {
                            _uiState.update { it.copy(isLoading = true) }
                        }

                        is ApiResponse.Success -> {
                            _uiState.update { it.copy(isLoading = false) }
                            _event.send(ChangePasswordState.Event.NavigateToSuccess)
                        }

                        is ApiResponse.Failure -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = response.errorMessage
                                )
                            }
                            _event.send(ChangePasswordState.Event.ShowError)
                        }
                    }
                }
        }

    }

    fun onAction(action: ChangePasswordState.Action) {
        when (action) {
            is ChangePasswordState.Action.OnChangeOldPassword -> {
                _uiState.update { it.copy(oldPassword = action.oldPassword) }
            }

            is ChangePasswordState.Action.OnChangeNewPassword -> {
                _uiState.update { it.copy(newPassword = action.newPassword) }
            }

            is ChangePasswordState.Action.OnBack -> {
                viewModelScope.launch {
                    _event.send(ChangePasswordState.Event.OnBack)
                }
            }

            is ChangePasswordState.Action.OnChangePassword -> {
                changePassword()
            }
        }
    }
}

object ChangePasswordState {
    data class UiState(
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val oldPassword: String = "",
        val newPassword: String = "",
        val oldPasswordError: String? = null,
        val newPasswordError: String? = null,
        val isValid: Boolean = false,
    )

    sealed interface Event {
        data object ShowError : Event
        data object OnBack : Event
        data object NavigateToSuccess : Event
    }

    sealed interface Action {
        data class OnChangeOldPassword(val oldPassword: String) : Action
        data class OnChangeNewPassword(val newPassword: String) : Action
        data object OnChangePassword : Action
        data object OnBack : Action

    }
}