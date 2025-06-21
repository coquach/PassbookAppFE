package com.se104.passbookapp.ui.screen.auth.change_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.domain.use_case.auth.LogOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordSuccessViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ResetPasswordSuccessState.UiState())
    val uiState = _uiState.asStateFlow()
    private val _event = Channel<ResetPasswordSuccessState.Event>()
    val event = _event.receiveAsFlow()

    private fun onLogout() {
        viewModelScope.launch {
            logOutUseCase.invoke().collect { response ->
                when (response) {
                    is ApiResponse.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                    }

                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                    }

                    is ApiResponse.Failure -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = response.errorMessage
                            )
                        }
                        _event.send(ResetPasswordSuccessState.Event.ShowError)
                    }
                }
            }
        }
    }

    fun onAction(action: ResetPasswordSuccessState.Action) {
        when (action) {
            ResetPasswordSuccessState.Action.NavigateToAuth -> {
                onLogout()
            }
        }
    }
}

object ResetPasswordSuccessState {
    data class UiState(
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    sealed interface Event {
        data object ShowError : Event
    }

    sealed interface Action {
        object NavigateToAuth : Action
    }
}