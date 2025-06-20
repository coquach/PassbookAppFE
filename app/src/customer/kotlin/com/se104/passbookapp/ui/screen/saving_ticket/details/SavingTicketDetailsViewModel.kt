package com.se104.passbookapp.ui.screen.saving_ticket.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.request.WithdrawalTicketRequest
import com.se104.passbookapp.data.model.SavingTicket
import com.se104.passbookapp.domain.repository.WithdrawalRepository
import com.se104.passbookapp.navigation.SavingTicketDetails
import com.se104.passbookapp.navigation.savingTicketNavType
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
import kotlin.reflect.typeOf

@HiltViewModel
class SavingTicketDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val withdrawalRepository: WithdrawalRepository,
) : ViewModel() {
    private val argument = savedStateHandle.toRoute<SavingTicketDetails>(
        typeMap =mapOf(typeOf<SavingTicket>() to savingTicketNavType)
    ).savingTicket

    private val _uiState = MutableStateFlow(SavingTicketDetailsState.UiState(
        savingTicket = argument,
        request = WithdrawalTicketRequest(savingTicketId = argument.id)
    ))
    val uiState: StateFlow<SavingTicketDetailsState.UiState> get() = _uiState.asStateFlow()
    
    private val _event = Channel<SavingTicketDetailsState.Event>()
    val event = _event.receiveAsFlow()


    private fun createWithdrawalTicket() {
        viewModelScope.launch {

            withdrawalRepository.createWithdrawalTicket(_uiState.value.request)
                .collect { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = null
                                )
                            }
                            _event.send(SavingTicketDetailsState.Event.NavigateToActionSuccess)

                        }

                        is ApiResponse.Failure -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = response.errorMessage
                                )
                            }
                            _event.send(SavingTicketDetailsState.Event.ShowError)
                        }

                        ApiResponse.Loading -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = true,
                                    error = null
                                )
                            }
                        }
                    }
                }
        }
    }
    fun onAction(action: SavingTicketDetailsState.Action){
        when(action){
            is SavingTicketDetailsState.Action.OnChangeAmountWithdrawal -> {
                _uiState.update {
                    it.copy(
                        request = it.request.copy(
                            withdrawalAmount = action.amount ?: BigDecimal.ZERO))}

            }
            SavingTicketDetailsState.Action.OnCreateWithdrawalTicket -> {
                createWithdrawalTicket()
            }
            SavingTicketDetailsState.Action.Withdraw -> {
                viewModelScope.launch {
                    _event.send(SavingTicketDetailsState.Event.Withdraw)
                }
            }
            SavingTicketDetailsState.Action.OnBack -> {
                viewModelScope.launch {
                    _event.send(SavingTicketDetailsState.Event.OnBack)
                }
            }

        }
    }

}

object SavingTicketDetailsState {
    data class UiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val savingTicket: SavingTicket,
        val request: WithdrawalTicketRequest = WithdrawalTicketRequest(),
    )

    sealed interface Event{
        data object NavigateToActionSuccess : Event
        data object Withdraw : Event
        data object ShowError: Event
        data object OnBack : Event
    }
    sealed interface Action{
        data class OnChangeAmountWithdrawal(val amount: BigDecimal?) : Action
        data object OnCreateWithdrawalTicket : Action
        data object Withdraw : Action
        data object OnBack: Action
    }
}