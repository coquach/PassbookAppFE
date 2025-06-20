package com.se104.passbookapp.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.model.User
import com.se104.passbookapp.domain.use_case.user.GetMyInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getMyInfoUseCase: GetMyInfoUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileState.UiState())
    val uiState = _uiState.asStateFlow()

    private val _event = Channel<ProfileState.Event>()
    val event = _event.receiveAsFlow()

    fun getMyInfo() {
        viewModelScope.launch {
            getMyInfoUseCase().collect { response ->
                when (response) {
                   is ApiResponse.Success -> {
                       _uiState.update {
                           it.copy(getMyInfoState = ProfileState.GetMyInfoState.Success(response.data))
                       }
                   }
                    is ApiResponse.Failure -> {
                        _uiState.update {
                            it.copy(getMyInfoState = ProfileState.GetMyInfoState.Error(response.errorMessage))
                        }
                    }
                    is ApiResponse.Loading -> {
                        _uiState.update {
                            it.copy(getMyInfoState = ProfileState.GetMyInfoState.Loading)
                        }
                    }
                }
            }
        }
    }

    fun onAction(action: ProfileState.Action) {
        when (action) {
            ProfileState.Action.OnBack -> {
                viewModelScope.launch {
                    _event.send(ProfileState.Event.OnBack)
                }
            }
            ProfileState.Action.NavigateToChangePassword -> {
                viewModelScope.launch {
                    _event.send(ProfileState.Event.NavigateToChangePassword)
                }
            }
        }
    }

}

object ProfileState {
    data class UiState(
        val getMyInfoState: GetMyInfoState = GetMyInfoState.Loading,
    )
    sealed interface GetMyInfoState {
        object Loading : GetMyInfoState
        data class Success(val user: User) : GetMyInfoState
        data class Error(val message: String) : GetMyInfoState
    }

    sealed interface Event {
        data object OnBack : Event
        data object NavigateToChangePassword : Event
    }

    sealed interface Action {
        data object OnBack : Action
        data object NavigateToChangePassword : Action
    }
}
