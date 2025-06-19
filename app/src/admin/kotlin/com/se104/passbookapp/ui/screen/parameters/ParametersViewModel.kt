package com.se104.passbookapp.ui.screen.parameters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.request.ParameterRequest
import com.se104.passbookapp.data.model.Parameter
import com.se104.passbookapp.domain.repository.ParameterRepository
import com.se104.passbookapp.ui.screen.parameters.ParametersState.ParametersReceivedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject


@HiltViewModel
class ParametersViewModel @Inject constructor(
    private val parameterRepository: ParameterRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ParametersState.UiState())
    val uiState get() = _uiState.asStateFlow()
    private val _event = Channel<ParametersState.Event>()
    val event get() = _event.receiveAsFlow()

    fun getParameters() {
        viewModelScope.launch {
            parameterRepository.getParameters().collect { response ->
                when (response) {
                    is ApiResponse.Loading -> {
                        _uiState.update {
                            it.copy(
                                parametersState = ParametersReceivedState.Loading,
                            )
                        }
                    }

                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                parametersState = ParametersReceivedState.Success,
                                parameters = response.data
                            )
                        }
                    }

                    is ApiResponse.Failure -> {
                        _uiState.update {
                            it.copy(
                                parametersState = ParametersReceivedState.Error(response.errorMessage),
                            )
                        }
                    }
                }
            }
        }

    }

    private fun updateParameters() {
        viewModelScope.launch {
            parameterRepository.updateParameters(
                ParameterRequest(
                    minAge = _uiState.value.parameters!!.minAge,
                    minTransactionAmount = _uiState.value.parameters!!.minTransactionAmount,
                    maxTransactionAmount = _uiState.value.parameters!!.maxTransactionAmount,
                    minSavingAmount = _uiState.value.parameters!!.minSavingAmount
                )
            ).collect { response ->
                when (response) {
                    is ApiResponse.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                            )
                        }
                        _event.send(ParametersState.Event.ShowToastSuccess("Cập nhật tham số thành công"))
                        getParameters()
                    }

                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = null
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
                        _event.send(ParametersState.Event.ShowError)
                    }
                }
            }
        }
    }

    fun onAction(action: ParametersState.Action) {
        when (action) {
            is ParametersState.Action.OnBack -> {
                viewModelScope.launch {
                    _event.send(ParametersState.Event.OnBack)
                }
            }

            is ParametersState.Action.OnChangeMinAge -> {
                _uiState.update {
                    it.copy(
                        parameters = it.parameters!!.copy(
                            minAge = action.minAge ?: 0
                        )
                    )
                }
            }

            is ParametersState.Action.OnChangeMinTransactionAmount -> {
                _uiState.update {
                    it.copy(
                        parameters = it.parameters!!.copy(
                            minTransactionAmount = action.minTransactionAmount ?: BigDecimal.ZERO
                        )
                    )
                }
            }

            is ParametersState.Action.OnChangeMaxTransactionAmount -> {
                _uiState.update {
                    it.copy(
                        parameters = it.parameters!!.copy(
                            maxTransactionAmount = action.maxTransactionAmount ?: BigDecimal.ZERO
                        )
                    )
                }
            }

            is ParametersState.Action.OnChangeMinSavingAmount -> {
                _uiState.update {
                    it.copy(
                        parameters = it.parameters!!.copy(
                            minSavingAmount = action.minSavingAmount ?: BigDecimal.ZERO
                        )
                    )
                }
            }

            is ParametersState.Action.OnUpdate -> {
                updateParameters()
            }
        }

    }
}

object ParametersState {
    data class UiState(
        val parametersState: ParametersReceivedState = ParametersReceivedState.Loading,
        val parameters: Parameter? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
    )

    sealed interface ParametersReceivedState {
        object Loading : ParametersReceivedState
        object Success : ParametersReceivedState
        data class Error(val message: String) : ParametersReceivedState
    }

    sealed interface Event {
        data object OnBack : Event
        data object ShowError : Event
        data class ShowToastSuccess(val message: String) : Event
    }

    sealed interface Action {
        data object OnBack : Action
        data class OnChangeMinAge(val minAge: Int?) : Action
        data class OnChangeMinTransactionAmount(val minTransactionAmount: BigDecimal?) : Action
        data class OnChangeMaxTransactionAmount(val maxTransactionAmount: BigDecimal?) : Action
        data class OnChangeMinSavingAmount(val minSavingAmount: BigDecimal?) : Action
        data object OnUpdate : Action

    }

}