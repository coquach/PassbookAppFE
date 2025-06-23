package com.se104.passbookapp.ui.screen.saving_type


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.model.SavingType
import com.se104.passbookapp.domain.use_case.saving_type.CreateSavingTypeUseCase
import com.se104.passbookapp.domain.use_case.saving_type.DeleteSavingTypeUseCase
import com.se104.passbookapp.domain.use_case.saving_type.GetActiveSavingTypesUseCase
import com.se104.passbookapp.domain.use_case.saving_type.GetInActiveSavingTypesUseCase
import com.se104.passbookapp.domain.use_case.saving_type.SetActiveSavingTypeUseCase
import com.se104.passbookapp.domain.use_case.saving_type.UpdateSavingTypeUseCase

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class SavingTypeViewModel @Inject constructor(
    private val getActiveSavingTypesUseCase: GetActiveSavingTypesUseCase,
    private val getInActiveSavingTypesUseCase: GetInActiveSavingTypesUseCase,
    private val createSavingTypeUseCase: CreateSavingTypeUseCase,
    private val updateSavingTypeUseCase: UpdateSavingTypeUseCase,
    private val deleteSavingTypeUseCase: DeleteSavingTypeUseCase,
    private val setActiveSavingTypesUseCase: SetActiveSavingTypeUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SavingTypeState.UiState())
    val uiState: StateFlow<SavingTypeState.UiState> get() = _uiState.asStateFlow()

    private val _event = Channel<SavingTypeState.Event>()
    val event = _event.receiveAsFlow()


    fun getActiveSavingTypes() {
        viewModelScope.launch {
            getActiveSavingTypesUseCase().collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                activeSavingTypeListState = SavingTypeState.ActiveSavingTypeList.Success,
                                activeSavingTypes = response.data
                            )
                        }
                    }

                    is ApiResponse.Loading -> {
                        _uiState.update {
                            it.copy(
                                activeSavingTypeListState = SavingTypeState.ActiveSavingTypeList.Loading
                            )
                        }
                    }

                    is ApiResponse.Failure -> {
                        _uiState.update {
                            it.copy(
                                activeSavingTypeListState = SavingTypeState.ActiveSavingTypeList.Error(
                                    response.errorMessage
                                )
                            )
                        }
                    }
                }
            }
        }


    }

    fun getInActiveSavingTypes() {
        viewModelScope.launch {
            getInActiveSavingTypesUseCase().collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                inactiveSavingTypeListState = SavingTypeState.InactiveSavingTypeList.Success,
                                inactiveSavingTypes = response.data
                            )
                        }
                    }

                    is ApiResponse.Loading -> {
                        _uiState.update {
                            it.copy(
                                inactiveSavingTypeListState = SavingTypeState.InactiveSavingTypeList.Loading
                            )
                        }
                    }

                    is ApiResponse.Failure -> {
                        _uiState.update {
                            it.copy(
                                inactiveSavingTypeListState = SavingTypeState.InactiveSavingTypeList.Error(
                                    response.errorMessage
                                )
                            )
                        }
                    }
                }
            }
        }


    }

    private fun createSavingType() {
        viewModelScope.launch {

            createSavingTypeUseCase(_uiState.value.savingTypeSelected).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                savingTypeSelected = response.data,
                                activeSavingTypes = it.activeSavingTypes + response.data
                            )
                        }
                        _event.send(SavingTypeState.Event.ShowToastSuccess("Tạo loại tiết kiệm thành công"))

                    }

                    is ApiResponse.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is ApiResponse.Failure -> {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = response.errorMessage)
                        }
                        _event.send(SavingTypeState.Event.ShowError)

                    }
                }

            }
        }

    }

    private fun updateSavingType(savingType: SavingType) {

        viewModelScope.launch {

            updateSavingTypeUseCase(_uiState.value.savingTypeSelected).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                activeSavingTypes = it.activeSavingTypes.map { savingType ->
                                    if (savingType.id == response.data.id) {
                                        response.data
                                    } else {
                                        savingType
                                    }
                                }
                            )
                        }
                        _event.send(SavingTypeState.Event.ShowToastSuccess("Cập nhật loại tiết kiệm thành công"))
                    }

                    is ApiResponse.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is ApiResponse.Failure -> {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = response.errorMessage)
                        }
                        _event.send(SavingTypeState.Event.ShowError)
                    }

                }
            }
        }
    }

    private fun setActiveSavingType(id: Long, isActive: Boolean) {
        viewModelScope.launch {
            setActiveSavingTypesUseCase(id, isActive).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                            )
                        }
                        _event.send(SavingTypeState.Event.ShowToastSuccess("Cập nhật trạng thái loại tiết kiệm thành công"))
                        onAction(SavingTypeState.Action.Refresh)
                    }

                    is ApiResponse.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is ApiResponse.Failure -> {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = response.errorMessage)
                        }
                        _event.send(SavingTypeState.Event.ShowError)

                    }

                }
            }
        }
    }

    private fun deleteSavingType(id: Long) {
        viewModelScope.launch {
            deleteSavingTypeUseCase(id).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                            )
                        }
                        _event.send(SavingTypeState.Event.ShowToastSuccess("Xóa loại tiết kiệm thành công"))
                        onAction(SavingTypeState.Action.Refresh)
                    }

                    is ApiResponse.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }

                    is ApiResponse.Failure -> {
                        _uiState.update {
                            it.copy(isLoading = false, errorMessage = response.errorMessage)
                        }
                        _event.send(SavingTypeState.Event.ShowError)
                    }
                }
            }
        }
    }

    fun onAction(action: SavingTypeState.Action) {
        when (action) {
            is SavingTypeState.Action.OnCreateSavingType -> {
                createSavingType()
            }

            is SavingTypeState.Action.OnUpdateSavingType -> {
                updateSavingType(uiState.value.savingTypeSelected)
            }

            is SavingTypeState.Action.OnSetActiveSavingType -> {
                setActiveSavingType(uiState.value.savingTypeSelected.id!!, action.isActive)
            }
            is SavingTypeState.Action.OnDeleteSavingType -> {
                deleteSavingType(uiState.value.savingTypeSelected.id!!)
            }

            is SavingTypeState.Action.OnTypeNameChange -> {
                _uiState.update {
                    it.copy(savingTypeSelected = it.savingTypeSelected.copy(typeName = action.name))
                }
            }

            is SavingTypeState.Action.OnDurationChange -> {
                _uiState.update {
                    it.copy(
                        savingTypeSelected = it.savingTypeSelected.copy(
                            duration = action.duration ?: 0
                        )
                    )
                }
            }

            is SavingTypeState.Action.OnInterestRateChange -> {
                val parsed = action.interestRate.toBigDecimalOrNull()
                _uiState.update {
                    it.copy(
                        interestRateInput = action.interestRate,
                        savingTypeSelected = it.savingTypeSelected.copy(
                            interestRate = parsed ?: BigDecimal.ZERO
                        )
                    )
                }
            }

            is SavingTypeState.Action.OnSelectedSavingType -> {
                _uiState.update {
                    it.copy(savingTypeSelected = action.savingType)
                }
            }

            is SavingTypeState.Action.Refresh -> {
                getActiveSavingTypes()
                getInActiveSavingTypes()
            }

            is SavingTypeState.Action.OnUpdateStatus -> {
                _uiState.update {
                    it.copy(isUpdate = action.isUpdate)
                }
            }

            is SavingTypeState.Action.OnHideStatus -> {
                _uiState.update {
                    it.copy(isHide = action.isHide)
                }
            }

            is SavingTypeState.Action.ShowToast -> {
                viewModelScope.launch {
                    _event.send(SavingTypeState.Event.ShowToastUnEnableAction)
                }
            }
            is SavingTypeState.Action.OnBack -> {
                viewModelScope.launch {
                    _event.send(SavingTypeState.Event.OnBack)
                }
            }
        }
    }
}


object SavingTypeState {
    data class UiState(
        val activeSavingTypeListState: ActiveSavingTypeList = ActiveSavingTypeList.Loading,
        val inactiveSavingTypeListState: InactiveSavingTypeList = InactiveSavingTypeList.Loading,
        val activeSavingTypes: List<SavingType> = emptyList(),
        val inactiveSavingTypes: List<SavingType> = emptyList(),
        val savingTypeSelected: SavingType = SavingType(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val isUpdate: Boolean = false,
        val isHide: Boolean = false,
        val interestRateInput: String ="",
    )

    sealed interface ActiveSavingTypeList {
        data object Success : ActiveSavingTypeList
        data object Loading : ActiveSavingTypeList
        data class Error(val message: String) : ActiveSavingTypeList

    }

    sealed interface InactiveSavingTypeList {
        data object Success : InactiveSavingTypeList
        data object Loading : InactiveSavingTypeList
        data class Error(val message: String) : InactiveSavingTypeList
    }

    sealed interface Event {
        data object OnBack : Event
        data object ShowError : Event
        data object ShowToastUnEnableAction : Event
        data class ShowToastSuccess(val message: String) : Event

    }

    sealed interface Action {
        data object OnCreateSavingType : Action
        data object OnUpdateSavingType : Action
        data class OnSetActiveSavingType(val isActive: Boolean) : Action
        data class OnTypeNameChange(val name: String) : Action
        data class OnDurationChange(val duration: Int?) : Action
        data class OnInterestRateChange(val interestRate: String) : Action
        data class OnSelectedSavingType(val savingType: SavingType) : Action
        data object Refresh : Action
        data class OnUpdateStatus(val isUpdate: Boolean) : Action
        data class OnHideStatus(val isHide: Boolean) : Action
        data object OnDeleteSavingType : Action
        data object ShowToast : Action
        data object OnBack: Action

    }
}