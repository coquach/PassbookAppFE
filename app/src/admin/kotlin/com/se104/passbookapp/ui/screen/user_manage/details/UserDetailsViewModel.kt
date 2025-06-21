package com.se104.passbookapp.ui.screen.user_manage.details

import android.view.View
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.model.Group
import com.se104.passbookapp.data.model.User
import com.se104.passbookapp.domain.use_case.group.GetGroupsUseCase
import com.se104.passbookapp.domain.use_case.user.SetGroupForUserUseCase
import com.se104.passbookapp.navigation.UserDetail
import com.se104.passbookapp.navigation.userNavType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getGroupUseCase: GetGroupsUseCase,
    private val setGroupForUserUseCase: SetGroupForUserUseCase
) : ViewModel() {
    private val user = savedStateHandle.toRoute<UserDetail>(
        typeMap =mapOf(typeOf<User>() to userNavType)
    ).user
    private val _uiState = MutableStateFlow(UserDetailsState.UiState(user = user, groupName = user.groupName))
    val uiState: StateFlow<UserDetailsState.UiState> get() = _uiState.asStateFlow()

    private val _event = Channel<UserDetailsState.Event>()
    val event = _event.receiveAsFlow()

    fun getGroupForUser(){
        viewModelScope.launch {
            getGroupUseCase().collect{ response ->
                when(response){
                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = null,
                                groups = response.data)}}
                    is ApiResponse.Failure -> {
                        _uiState.update { it.copy(isLoading = false, errorMessage = response.errorMessage) }
                        _event.send(UserDetailsState.Event.ShowError)
                    }
                    is ApiResponse.Loading -> {
                        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    }

                }
            }
        }
    }

    private fun setGroupForUser(userId: Long, groupId: Int){
        viewModelScope.launch {
            setGroupForUserUseCase(userId, groupId).collect{ response ->
                when(response){
                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,)}
                    _event.send(UserDetailsState.Event.ShowSuccessToast("Cập nhật vai trò người dùng thành công"))
                    }
                    is ApiResponse.Failure -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = response.errorMessage
                            )
                        }
                        _event.send(UserDetailsState.Event.ShowError)
                    }
                    is ApiResponse.Loading -> {
                        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    }
                }

            }
        }
    }

    fun onAction(action: UserDetailsState.Action){
        when(action){
            is UserDetailsState.Action.OnBack -> {
                viewModelScope.launch {
                    _event.send(UserDetailsState.Event.OnBack)
                }
            }
            is UserDetailsState.Action.OnSelectGroup -> {
                _uiState.update {
                    it.copy(groupIdSelected = action.groupId, groupName = action.groupName)
                }
            }
            is UserDetailsState.Action.SetGroupForUser -> {
                setGroupForUser(_uiState.value.user.id, _uiState.value.groupIdSelected!!)
            }
        }
    }

}

object UserDetailsState{
    data class UiState(
        val groupName: String,
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val user: User,
        val groupIdSelected: Int? = null,
        val groups: List<Group> = emptyList(),

    )
    sealed interface Event{
        data object OnBack : Event
        data object ShowError : Event
        data class ShowSuccessToast(val message: String) : Event
    }
    sealed interface Action{
        data object OnBack : Action
        data class OnSelectGroup(val groupId: Int, val groupName: String ) : Action
        data object SetGroupForUser : Action


    }
}