package com.se104.passbookapp.ui.screen.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(): ViewModel() {
    private val _uiState = MutableStateFlow(SettingState.UiState())
    val uiState: StateFlow<SettingState.UiState> get() = _uiState.asStateFlow()

    private val _event = Channel<SettingState.Event>()
    val event = _event.receiveAsFlow()

    private fun logout(){

    }

    fun onAction(action: SettingState.Action){
        when(action){
            is SettingState.Action.OnLogout -> {
                logout()
            }
            is SettingState.Action.OnClickReport -> {
                viewModelScope.launch {
                    _event.send(SettingState.Event.NavigateToReport)
                }
            }
        }
    }
}

object SettingState{
    data class UiState(
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    sealed interface Event{
        data object ShowError : Event
        data object NavigateToReport : Event
    }
    sealed class Action{

        object OnLogout : Action()
        data object OnClickReport : Action()
    }

}