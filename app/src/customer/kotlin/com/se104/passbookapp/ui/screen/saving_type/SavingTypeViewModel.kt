package com.se104.passbookapp.ui.screen.saving_type


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.request.SavingTypeRequest
import com.se104.passbookapp.data.model.SavingType
import com.se104.passbookapp.domain.repository.SavingTypeRepository

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
    private val savingTypeRepository: SavingTypeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SavingTypeState.UiState())
    val uiState: StateFlow<SavingTypeState.UiState> get() = _uiState.asStateFlow()

    private val _event = Channel<SavingTypeState.Event>()
    val event = _event.receiveAsFlow()

    init {
        getActiveSavingTypes()
        getInActiveSavingTypes()
    }

    private fun getActiveSavingTypes() {
        viewModelScope.launch {
            savingTypeRepository.getActiveSavingTypes().collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                activeSavingTypeList = SavingTypeState.ActiveSavingTypeList.Success,
                                activeSavingTypes = response.data
                            )
                        }
                    }

                    is ApiResponse.Loading -> {
                        _uiState.update {
                            it.copy(
                                activeSavingTypeList = SavingTypeState.ActiveSavingTypeList.Loading
                            )
                        }
                    }

                    is ApiResponse.Failure -> {
                        _uiState.update {
                            it.copy(
                                activeSavingTypeList = SavingTypeState.ActiveSavingTypeList.Error(
                                    response.errorMessage
                                )
                            )
                        }
                    }
                }
            }
        }


    }

    private fun getInActiveSavingTypes() {
        viewModelScope.launch {
            savingTypeRepository.getInactiveSavingTypes().collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                inactiveSavingTypeList = SavingTypeState.InactiveSavingTypeList.Success,
                                inactiveSavingTypes = response.data
                            )
                        }
                    }

                    is ApiResponse.Loading -> {
                        _uiState.update {
                            it.copy(
                                inactiveSavingTypeList = SavingTypeState.InactiveSavingTypeList.Loading
                            )
                        }
                    }

                    is ApiResponse.Failure -> {
                        _uiState.update {
                            it.copy(
                                inactiveSavingTypeList = SavingTypeState.InactiveSavingTypeList.Error(
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
            val request = SavingTypeRequest(
                typeName = _uiState.value.savingTypeSelected.typeName,
                duration = _uiState.value.savingTypeSelected.duration,
                interestRate = _uiState.value.savingTypeSelected.interestRate
            )
            savingTypeRepository.createSavingType(request).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                savingTypeSelected = response.data,
                                activeSavingTypes = it.activeSavingTypes + response.data
                            )
                        }
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
            val request = SavingTypeRequest(
                typeName = savingType.typeName,
                duration = savingType.duration,
                interestRate = savingType.interestRate
            )
            val id = savingType.id!!

            savingTypeRepository.updateSavingType(id, request).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                activeSavingTypes = it.activeSavingTypes.map { savingType ->
                                    if (savingType.id == id) {
                                        response.data
                                    } else {
                                        savingType
                                    }
                                }
                            )
                        }
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
            savingTypeRepository.setActiveSavingType(id, isActive).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,

                                )
                        }
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

            is SavingTypeState.Action.OnTypeNameChange -> {
                _uiState.update {
                    it.copy(savingTypeSelected = it.savingTypeSelected.copy(typeName = action.name))
                }
            }

            is SavingTypeState.Action.OnDurationChange -> {
                _uiState.update {
                    it.copy(savingTypeSelected = it.savingTypeSelected.copy(duration = action.duration?: 0))
                }
            }

            is SavingTypeState.Action.OnInterestRateChange -> {
                _uiState.update {
                    it.copy(savingTypeSelected = it.savingTypeSelected.copy(interestRate = action.interestRate?: BigDecimal.ZERO))
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
        }
    }
}


object SavingTypeState {
    data class UiState(
        val activeSavingTypes: List<SavingType> = emptyList(),
        val inactiveSavingTypes: List<SavingType> = emptyList(),
        val savingTypeSelected: SavingType = SavingType(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val activeSavingTypeList: ActiveSavingTypeList = ActiveSavingTypeList.Loading,
        val inactiveSavingTypeList: InactiveSavingTypeList = InactiveSavingTypeList.Loading,
        val isUpdate: Boolean = false,
        val isHide: Boolean = false,
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
        data object ShowError : Event

    }

    sealed interface Action {
        data object OnCreateSavingType : Action
        data object OnUpdateSavingType : Action
        data class OnSetActiveSavingType(val isActive: Boolean) : Action
        data class OnTypeNameChange(val name: String) : Action
        data class OnDurationChange(val duration: Int?) : Action
        data class OnInterestRateChange(val interestRate: BigDecimal?) : Action
        data class OnSelectedSavingType(val savingType: SavingType) : Action
        data object Refresh : Action
        data class OnUpdateStatus(val isUpdate: Boolean) : Action
        data class OnHideStatus(val isHide: Boolean) : Action

    }
}