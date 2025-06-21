package com.se104.passbookapp.ui.screen.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.domain.use_case.auth.LogOutUseCase
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
class SettingViewModel @Inject constructor(
    private val logOutUseCase: LogOutUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingState.UiState())
    val uiState: StateFlow<SettingState.UiState> get() = _uiState.asStateFlow()

    private val _event = Channel<SettingState.Event>()
    val event = _event.receiveAsFlow()

    private fun logout() {
        viewModelScope.launch {
            logOutUseCase().collect { result ->
                when (result) {
                    is ApiResponse.Loading ->{
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
                                error = result.errorMessage
                            )
                        }
                        _event.send(SettingState.Event.ShowError)
                    }
                }
            }
        }
    }

    fun onAction(action: SettingState.Action) {
        when (action) {
            is SettingState.Action.OnLogout -> {
                logout()
            }

            is SettingState.Action.OnClickReport -> {
                viewModelScope.launch {
                    _event.send(SettingState.Event.NavigateToReport)
                }
            }

            is SettingState.Action.OnClickParameters -> {
                viewModelScope.launch {
                    _event.send(SettingState.Event.NavigateToParameters)
                }
            }

            is SettingState.Action.OnClickProfile -> {
                viewModelScope.launch {
                    _event.send(SettingState.Event.NavigateToProfile)
                }
            }

            SettingState.Action.OnClickSavingType -> {
                viewModelScope.launch {
                    _event.send(SettingState.Event.NavigateToSavingType)
                }
            }
        }
    }
}

object SettingState {
    data class UiState(
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    sealed interface Event {
        data object ShowError : Event
        data object NavigateToReport : Event
        data object NavigateToParameters : Event
        data object NavigateToProfile : Event
        data object NavigateToSavingType : Event

    }

    sealed class Action {

        object OnLogout : Action()
        data object OnClickReport : Action()
        data object OnClickParameters : Action()
        data object OnClickProfile : Action()
        data object OnClickSavingType: Action()
    }

}