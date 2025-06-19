package com.se104.passbookapp.ui.screen.auth.verify_email

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.domain.use_case.auth.VerifyEmailUseCase
import com.se104.passbookapp.navigation.VerifyEmail
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
class VerifyEmailViewModel @Inject constructor(
    private val verifyEmailUseCase: VerifyEmailUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val email = savedStateHandle.toRoute<VerifyEmail>().email


    private val _uiState = MutableStateFlow(VerifyEmailState.UiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<VerifyEmailState.Event>()
    val event = _event.receiveAsFlow()



    private fun verifyEmail() {
        viewModelScope.launch {
            verifyEmailUseCase.invoke(email, _uiState.value.otp).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _event.send(VerifyEmailState.Event.NavigateToSignUp(email))
                    }
                    is ApiResponse.Failure -> {
                        _uiState.update { it.copy(isLoading = false, errorMessage = response.errorMessage) }
                        _event.send(VerifyEmailState.Event.ShowError)
                    }
                    is ApiResponse.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                }
            }
        }
    }

    fun onAction(action: VerifyEmailState.Action) {
        when (action) {
            is VerifyEmailState.Action.OnBack -> {
                viewModelScope.launch {
                    _event.send(VerifyEmailState.Event.OnBack)
                }
            }
            is VerifyEmailState.Action.OnOtpChange -> {
                if (action.otp.length != 6)
                _uiState.update { it.copy(otp = action.otp, isValid = false) }
                else
                    _uiState.update { it.copy(otp = action.otp, isValid = true) }
            }
            is VerifyEmailState.Action.VerifyEmail -> {
                verifyEmail()
            }
        }
    }


}

object VerifyEmailState{
    data class UiState(
        val otp: String = "",
        val isLoading: Boolean = false,
        val errorMessage: String = "",
        val isValid: Boolean = false,
    )

    sealed interface Event{
        data object OnBack: Event
        data class NavigateToSignUp(val email: String): Event
        data object ShowError: Event
    }

    sealed interface Action{
        data object OnBack: Action
        data class OnOtpChange(val otp: String): Action
        data object VerifyEmail: Action
    }
}