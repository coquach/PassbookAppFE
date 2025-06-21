package com.se104.passbookapp.ui.screen.user_manage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.filter.UserFilter
import com.se104.passbookapp.data.model.Group
import com.se104.passbookapp.data.model.User
import com.se104.passbookapp.domain.use_case.group.GetGroupsUseCase
import com.se104.passbookapp.domain.use_case.user.GetUsersUseCase
import com.se104.passbookapp.domain.use_case.user.SetActiveUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import kotlin.uuid.Uuid

@HiltViewModel
class UserManageViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val getGroupsUseCase: GetGroupsUseCase,
    private val setActiveUserUseCase: SetActiveUserUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserManageState.UiState())
    val uiState: StateFlow<UserManageState.UiState> get() = _uiState.asStateFlow()

    private val _event = Channel<UserManageState.Event>()
    val event = _event.receiveAsFlow()

    fun getUsers(filter: UserFilter) = getUsersUseCase(filter)
    fun getGroups() {
        viewModelScope.launch {
            getGroupsUseCase().collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                getGroupsState = UserManageState.GetGroupsState.Success,
                                groups = response.data
                            )
                        }
                    }

                    is ApiResponse.Failure -> {
                        _uiState.update {
                            it.copy(
                                getGroupsState = UserManageState.GetGroupsState.Error(response.errorMessage)
                            )
                        }
                    }

                    is ApiResponse.Loading -> {
                        _uiState.update {
                            it.copy(
                                getGroupsState = UserManageState.GetGroupsState.Loading
                            )
                        }
                    }
                }

            }
        }
    }

    private fun setActiveUser(userId: Long, isActive: Boolean){
        viewModelScope.launch {
            setActiveUserUseCase.invoke(userId, isActive).collect{ response ->
                when(response){
                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = null)}
                        _event.send(UserManageState.Event.ShowSuccessToast("Cập nhật trạng thái thành công"))
                        onAction(UserManageState.Action.OnRefresh)
                    }
                    is ApiResponse.Failure -> {
                        _uiState.update { it.copy(isLoading = false, errorMessage = response.errorMessage) }
                        _event.send(UserManageState.Event.ShowError)
                    }
                    is ApiResponse.Loading -> {
                        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                    }


                }
            }
        }
    }

    fun onAction(action: UserManageState.Action) {
        when (action) {
            is UserManageState.Action.OnChangeIsActive -> {
                _uiState.update {
                    it.copy(
                        filter = it.filter.copy(isActive = action.isActive)
                    )
                }
            }
            is UserManageState.Action.OnChangeOrder -> {
                _uiState.update {
                    it.copy(
                        filter = it.filter.copy(order = action.order)
                    )
                }
            }
            is UserManageState.Action.OnChangeSortBy -> {
                _uiState.update {
                    it.copy(
                        filter = it.filter.copy(sortBy = action.sortBy)
                    )
                }
            }
            is UserManageState.Action.OnSearch -> {
                _uiState.update {
                    it.copy(search = action.search, filter = it.filter.copy(citizenID = action.search))
                }

            }
            is UserManageState.Action.OnSelectGroup -> {
                _uiState.update {
                    it.copy(
                        groupName = action.groupName,
                        filter = it.filter.copy(groupId = action.groupId)
                    )
                }
            }
            is UserManageState.Action.OnSelectUser -> {
                _uiState.update {
                    it.copy(userSelected = action.user)
                }
            }
            is UserManageState.Action.OnSelectUserToGoDetail -> {
                viewModelScope.launch {
                    _event.send(UserManageState.Event.NavigateToDetail(action.user))
                }
            }
            is UserManageState.Action.SetActiveUser -> {
                setActiveUser(_uiState.value.userSelected!!, true)
            }
            is UserManageState.Action.SetInactiveUser -> {
                setActiveUser(_uiState.value.userSelected!!, false)
            }
            is UserManageState.Action.ShowToastUnEnable -> {
                viewModelScope.launch {
                    _event.send(UserManageState.Event.ShowToastUnEnableAction)
                }
            }
            UserManageState.Action.OnRefresh->{
                _uiState.update {
                    it.copy(
                        filter = it.filter.copy(isRefresh = UUID.randomUUID().toString())
                    )
                }
            }
        }
    }

}

object UserManageState {
    data class UiState(
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val groups: List<Group> = emptyList(),
        val groupName: String?=null,
        val search: String = "",
        val filter: UserFilter = UserFilter(sortBy = "createdAt"),
        val getGroupsState: GetGroupsState = GetGroupsState.Loading,
        val userSelected: Long? = null,

        )



    sealed interface GetGroupsState {
        data object Success : GetGroupsState
        data class Error(val message: String) : GetGroupsState
        object Loading : GetGroupsState
    }

    sealed interface Event {
        data object ShowError : Event
        data object ShowToastUnEnableAction : Event
        data class ShowSuccessToast(val message: String) : Event
        data class NavigateToDetail(val user: User) : Event
    }

    sealed interface Action {
        data class OnSearch(val search: String) : Action
        data class OnChangeOrder(val order: String) : Action
        data class OnChangeSortBy(val sortBy: String) : Action
        data class OnSelectUser(val user: Long) : Action
        data class OnChangeIsActive(val isActive: Boolean) : Action
        data object SetActiveUser : Action
        data object SetInactiveUser : Action
        data class OnSelectGroup(val groupId: Int, val groupName: String) : Action
        data class OnSelectUserToGoDetail(val user: User) : Action
        data object ShowToastUnEnable : Action
        data object OnRefresh: Action

    }


}