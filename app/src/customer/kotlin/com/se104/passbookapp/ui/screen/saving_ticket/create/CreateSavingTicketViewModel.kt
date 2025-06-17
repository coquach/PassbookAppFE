package com.se104.passbookapp.ui.screen.saving_ticket.create

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.model.SavingType
import com.se104.passbookapp.domain.use_case.saving_ticket.CreateSavingTicketUseCase
import com.se104.passbookapp.navigation.CreateSavingTicket
import com.se104.passbookapp.navigation.savingTypeNavType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class CreateSavingTicketViewModel @Inject constructor(
        savedStateHandle: SavedStateHandle,
    private val createSavingTicketUseCase: CreateSavingTicketUseCase
) : ViewModel(){
    private val argument = savedStateHandle.toRoute<CreateSavingTicket>(
        typeMap =mapOf(typeOf<SavingType>() to savingTypeNavType)
    ).savingType
    private val _uiState = MutableStateFlow(CreateSavingTicketState.UiState(savingType = argument))
    val uiState get() = _uiState.asStateFlow()

    private val _event = Channel<CreateSavingTicketState.Event>()
    val event get() = _event.receiveAsFlow()

    private fun createSavingTicket(){
        viewModelScope.launch {
            createSavingTicketUseCase(
                userId = 1,
                savingTypeId = _uiState.value.savingType.id!!,
                amount = _uiState.value.amount
            ).collect{ response ->
                when(response){
                    is ApiResponse.Success -> {
                       _uiState.update {
                           it.copy(
                               isLoading = false,
                               errorMessage = null
                           )
                       }
                        _event.send(CreateSavingTicketState.Event.NavigateToActionSuccess)
                    }

                    is ApiResponse.Failure -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = response.errorMessage
                            )
                        }
                        _event.send(CreateSavingTicketState.Event.ShowError)

                    }
                    ApiResponse.Loading -> {
                        _uiState.update {
                            it.copy(
                                isLoading = true,
                                errorMessage = null
                            )
                        }
                    }
                }
            }
        }
    }

    fun onAction(action: CreateSavingTicketState.Action){
        when(action){
            is CreateSavingTicketState.Action.OnAmountChange -> {
                _uiState.update {
                    it.copy(
                        amount = action.amount?: BigDecimal.ZERO)}}
            CreateSavingTicketState.Action.OnCreate -> {
                createSavingTicket()
            }
            CreateSavingTicketState.Action.OnBack -> {
                viewModelScope.launch {
                    _event.send(CreateSavingTicketState.Event.OnBack)
                }
            }
        }
    }
}

object CreateSavingTicketState{
    data class UiState(
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val savingType: SavingType,
        val amount: BigDecimal= BigDecimal.ZERO,
    )

    sealed interface Event{
        data object ShowError: Event
        data object OnBack: Event
        data object NavigateToActionSuccess: Event
    }
    sealed interface Action{
        data class OnAmountChange(val amount: BigDecimal?): Action
        data object OnCreate: Action
        data object OnBack: Action

    }
}