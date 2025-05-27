package com.se104.passbookapp.ui.screen.saving_ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.se104.passbookapp.data.dto.filter.SavingTicketFilter
import com.se104.passbookapp.data.model.SavingTicket
import com.se104.passbookapp.domain.use_case.saving_ticket.GetSavingTicketsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavingTicketViewModel @Inject constructor(
    private val getSavingTicketsUseCase: GetSavingTicketsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SavingTicketState.UiState())
    val uiState: StateFlow<SavingTicketState.UiState> get() = _uiState.asStateFlow()

    private val _event = Channel<SavingTicketState.Event>()
    val event = _event.receiveAsFlow()

    init {
        getSavingTickets(SavingTicketFilter())

    }

    private val _savingTickets = MutableStateFlow<PagingData<SavingTicket>>(PagingData.empty())
    val savingTickets: StateFlow<PagingData<SavingTicket>> get() = _savingTickets


    private fun getSavingTickets(filter: SavingTicketFilter) {
        viewModelScope.launch {
            getSavingTicketsUseCase(filter).cachedIn(viewModelScope).collect {
                _savingTickets.value = it
            }
        }

    }
    fun onAction(action: SavingTicketState.Action) {
        when (action) {
            is SavingTicketState.Action.Search -> {
                _uiState.value = _uiState.value.copy(search = action.search)
            }
            is SavingTicketState.Action.Filter -> {
                getSavingTickets(action.filter)
            }

            is SavingTicketState.Action.GoToDetail -> {
                viewModelScope.launch {
                    _event.send(SavingTicketState.Event.GoToDetail(action.savingTicket))
                }
            }
        }
        }
}

object SavingTicketState {
    data class UiState(
        val search: String = "",
    )
    sealed interface Event {
        data class GoToDetail(val savingTicket: SavingTicket) : Event
    }

    sealed interface Action {
        data class Search(val search: String) : Action
        data class Filter(val filter: SavingTicketFilter) : Action
        data class GoToDetail(val savingTicket: SavingTicket) : Action
    }
}