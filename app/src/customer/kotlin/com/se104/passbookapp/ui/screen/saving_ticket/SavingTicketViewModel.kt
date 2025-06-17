package com.se104.passbookapp.ui.screen.saving_ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.filter.SavingTicketFilter
import com.se104.passbookapp.data.dto.request.WithdrawalTicketRequest
import com.se104.passbookapp.data.model.SavingTicket
import com.se104.passbookapp.domain.repository.WithdrawalRepository
import com.se104.passbookapp.domain.use_case.saving_ticket.GetSavingTicketsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SavingTicketViewModel @Inject constructor(
    private val getSavingTicketsUseCase: GetSavingTicketsUseCase,
    private val withdrawalRepository: WithdrawalRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        SavingTicketState.UiState(
            filter = SavingTicketFilter(
                startDate = LocalDate.now().minusDays(1),
                endDate = LocalDate.now(),
                userId = 3
            )
        )
    )
    val uiState: StateFlow<SavingTicketState.UiState> get() = _uiState.asStateFlow()

    private val _event = Channel<SavingTicketState.Event>()
    val event = _event.receiveAsFlow()


    fun getSavingTickets(filter: SavingTicketFilter): Flow<PagingData<SavingTicket>> =
        getSavingTicketsUseCase(filter)

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
                            _event.send(SavingTicketState.Event.NavigateToActionSuccess)

                        }

                        is ApiResponse.Failure -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = response.errorMessage
                                )
                            }
                            _event.send(SavingTicketState.Event.ShowError)
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

    fun onAction(action: SavingTicketState.Action) {
        when (action) {
            is SavingTicketState.Action.Search -> {
                _uiState.value = _uiState.value.copy(search = action.search)
            }


            is SavingTicketState.Action.OnSelectSavingTicket -> {
                _uiState.update {
                    it.copy(
                        savingTicketSelected = action.savingTicket,
                        request = it.request.copy(savingTicketId = action.savingTicket?.id)
                    )

                }
            }

            is SavingTicketState.Action.OnChangeAmountWithdrawal -> {
                _uiState.update {
                    it.copy(
                        request = it.request.copy(
                            withdrawalAmount = action.amount ?: BigDecimal.ZERO
                        )
                    )
                }
            }

            SavingTicketState.Action.OnCreateWithdrawalTicket -> {
                createWithdrawalTicket()
            }

            SavingTicketState.Action.Withdraw -> {
                viewModelScope.launch {
                    _event.send(SavingTicketState.Event.Withdraw)
                }
            }

            is SavingTicketState.Action.OnChangeOrder -> {
                _uiState.update {
                    it.copy(
                        filter = it.filter.copy(order = action.order)
                    )
                }
            }

            is SavingTicketState.Action.OnChangeSortBy -> {
                _uiState.update {
                    it.copy(
                        filter = it.filter.copy(sortBy = action.sortBy)
                    )
                }
            }

            is SavingTicketState.Action.OnChangeDateFilter -> {
                if (action.startDate == null || action.endDate == null) return
                _uiState.update {
                    it.copy(
                        filter = it.filter.copy(
                            startDate = action.startDate,
                            endDate = action.endDate
                        )
                    )
                }
            }
        }
    }


}

object SavingTicketState {
    data class UiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val search: String = "",
        val filter: SavingTicketFilter,
        val savingTicketSelected: SavingTicket? = null,
        val request: WithdrawalTicketRequest = WithdrawalTicketRequest(),
    )

    sealed interface Event {
        data object ShowError : Event
        data object NavigateToActionSuccess : Event
        data object Withdraw : Event
    }

    sealed interface Action {
        data class Search(val search: String) : Action
        data class OnChangeDateFilter(val startDate: LocalDate?, val endDate: LocalDate?) : Action
        data class OnChangeOrder(val order: String) : Action
        data class OnChangeSortBy(val sortBy: String) : Action
        data class OnSelectSavingTicket(val savingTicket: SavingTicket?) : Action
        data class OnChangeAmountWithdrawal(val amount: BigDecimal?) : Action
        data object OnCreateWithdrawalTicket : Action
        data object Withdraw : Action

    }
}