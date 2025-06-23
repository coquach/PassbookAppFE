package com.se104.passbookapp.ui.screen.permission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.request.GroupRequest
import com.se104.passbookapp.data.model.Group
import com.se104.passbookapp.data.model.Permission
import com.se104.passbookapp.domain.use_case.group.CreateGroupUseCase
import com.se104.passbookapp.domain.use_case.group.DeleteGroupUseCase
import com.se104.passbookapp.domain.use_case.group.GetGroupsUseCase
import com.se104.passbookapp.domain.use_case.group.UpdateGroupUseCase
import com.se104.passbookapp.domain.use_case.permission.GetPermissionsNotAdminUseCase
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
class PermissionsSettingViewModel @Inject constructor(
    private val getPermissionsNotAdminUseCase: GetPermissionsNotAdminUseCase,
    private val getGroupsUseCase: GetGroupsUseCase,
    private val createGroupUseCase: CreateGroupUseCase,
    private val updateGroupUseCase: UpdateGroupUseCase,
    private val deleteGroupUseCase: DeleteGroupUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PermissionsSettingState.UiState())
    val uiState: StateFlow<PermissionsSettingState.UiState> get() = _uiState.asStateFlow()

    private val _event = Channel<PermissionsSettingState.Event>()
    val event = _event.receiveAsFlow()

    fun getPermissionsNotAdmin() {
        viewModelScope.launch {
            getPermissionsNotAdminUseCase().collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = null,
                                permissions = response.data,
                            )
                        }
                    }

                    is ApiResponse.Failure -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = response.errorMessage
                            )
                        }
                        _event.send(PermissionsSettingState.Event.ShowError)
                    }

                    is ApiResponse.Loading -> {

                    }
                }

            }
        }
    }

    fun getGroups() {
        viewModelScope.launch {
            getGroupsUseCase().collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                getGroupsState = PermissionsSettingState.GetGroupsState.Success,
                                groups = response.data,
                            )
                        }
                    }

                    is ApiResponse.Failure -> {
                        _uiState.update {
                            it.copy(
                                getGroupsState = PermissionsSettingState.GetGroupsState.Error(
                                    response.errorMessage
                                ),
                                errorMessage = response.errorMessage
                            )
                        }
                    }

                    is ApiResponse.Loading -> {
                        _uiState.update {
                            it.copy(
                                getGroupsState = PermissionsSettingState.GetGroupsState.Loading
                            )
                        }
                    }
                }
            }
        }
    }


    private fun createGroup() {
        viewModelScope.launch {
            createGroupUseCase(
                GroupRequest(
                    name = _uiState.value.groupSelected.name,
                    description = _uiState.value.groupSelected.description,
                    permissionIds = _uiState.value.groupSelected.permissions.map { it.id },
                )
            ).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                        _event.send(PermissionsSettingState.Event.ShowSuccessToast("Tạo nhóm thành công"))
                        getGroups()
                    }

                    is ApiResponse.Failure -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = response.errorMessage,
                            )
                        }
                        _event.send(PermissionsSettingState.Event.ShowError)
                    }

                    is ApiResponse.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun updateGroup() {
        viewModelScope.launch {
            updateGroupUseCase(
                request = GroupRequest(
                    name = _uiState.value.groupSelected.name,
                    description = _uiState.value.groupSelected.description,
                    permissionIds = _uiState.value.groupSelected.permissions.map { it.id },
                ), id = _uiState.value.groupSelected.id!!
            ).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = null
                            )
                        }

                        _event.send(PermissionsSettingState.Event.ShowSuccessToast("Cập nhật nhóm thành công"))
                        getGroups()
                    }

                    is ApiResponse.Failure -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = response.errorMessage,
                            )
                        }
                        _event.send(PermissionsSettingState.Event.ShowError)
                        getGroups()

                    }

                    is ApiResponse.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                            )
                        }
                    }
                }
            }
        }
    }

    private fun deleteGroup() {
        viewModelScope.launch {
            deleteGroupUseCase(_uiState.value.groupSelected.id!!).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = null
                            )

                        }
                        _event.send(PermissionsSettingState.Event.ShowSuccessToast("Xóa nhóm thành công"))
                        getGroups()
                    }

                    is ApiResponse.Failure -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = response.errorMessage,
                            )
                        }
                        _event.send(PermissionsSettingState.Event.ShowError)
                    }

                    is ApiResponse.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                            )
                        }
                    }
                }

            }
        }

    }

    fun onAction(action: PermissionsSettingState.Action) {
        when (action) {
            is PermissionsSettingState.Action.OnGroupSelected -> {
                _uiState.update {
                    it.copy(
                        groupSelected = action.group
                    )
                }
            }

            is PermissionsSettingState.Action.OnChangeNameGroup -> {
                _uiState.update {
                    it.copy(
                        groupSelected = it.groupSelected.copy(name = action.name)
                    )
                }
            }

            is PermissionsSettingState.Action.OnChangeDescriptionGroup -> {
                _uiState.update {
                    it.copy(
                        groupSelected = it.groupSelected.copy(description = action.description)
                    )
                }
            }

            is PermissionsSettingState.Action.OnChangePermissionGroup -> {
                _uiState.update {
                    it.copy(
                        groupSelected = it.groupSelected.copy(permissions = action.permissions)
                    )
                }
            }

            PermissionsSettingState.Action.OnCreateGroup -> {
                createGroup()
            }

            PermissionsSettingState.Action.OnUpdateGroup -> {
                updateGroup()
            }

            PermissionsSettingState.Action.OnDeleteGroup -> {
                deleteGroup()
            }

            is PermissionsSettingState.Action.OnSetStatus -> {
                _uiState.update {
                    it.copy(
                        isUpdate = action.status
                    )
                }
            }
            PermissionsSettingState.Action.ShowToast -> {
                viewModelScope.launch {
                    _event.send(PermissionsSettingState.Event.ShowToastUnEnableAction)
                }
            }
        }
    }
}

object PermissionsSettingState {
    data class UiState(
        val groups: List<Group> = emptyList(),
        val getGroupsState: GetGroupsState = GetGroupsState.Loading,
        val permissions: List<Permission> = emptyList(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val groupSelected: Group = Group(),
        val isUpdate: Boolean = false,
    )

    sealed interface GetGroupsState {
        data object Success : GetGroupsState
        data object Loading : GetGroupsState
        data class Error(val message: String) : GetGroupsState
    }

    sealed interface Event {
        data object ShowError : Event
        data class ShowSuccessToast(val message: String) : Event
        data object ShowToastUnEnableAction : Event
    }

    sealed interface Action {
        data class OnGroupSelected(val group: Group) : Action
        data class OnChangeNameGroup(val name: String) : Action
        data class OnChangeDescriptionGroup(val description: String) : Action
        data class OnChangePermissionGroup(val permissions: List<Permission>) : Action
        data object OnCreateGroup : Action
        data object OnUpdateGroup : Action
        data object OnDeleteGroup : Action
        data class OnSetStatus(val status: Boolean) : Action
        data object ShowToast : Action


    }
}