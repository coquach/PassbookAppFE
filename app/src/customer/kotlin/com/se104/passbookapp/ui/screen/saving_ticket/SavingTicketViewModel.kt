package com.se104.passbookapp.ui.screen.saving_ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.filter.SavingTicketFilter
import com.se104.passbookapp.data.model.SavingTicket
import com.se104.passbookapp.data.model.SavingType
import com.se104.passbookapp.domain.use_case.saving_ticket.GetSavingTicketsForCustomerUseCase
import com.se104.passbookapp.domain.use_case.saving_ticket.GetSavingTicketsUseCase
import com.se104.passbookapp.domain.use_case.saving_type.GetActiveSavingTypesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class SavingTicketViewModel @Inject constructor(
    private val getSavingTicketsUseCase: GetSavingTicketsUseCase,
    private val getSavingTicketsForCustomerUseCase: GetSavingTicketsForCustomerUseCase,
    private val getActiveSavingTypesUseCase: GetActiveSavingTypesUseCase,
) : ViewModel() {


    private val _uiState = MutableStateFlow(
        SavingTicketState.UiState(
            filter = SavingTicketFilter(
                startDate = LocalDate.now().minusDays(1),
                endDate = LocalDate.now(),
            ),

            )
    )
    val uiState: StateFlow<SavingTicketState.UiState> get() = _uiState.asStateFlow()

    private val _event = Channel<SavingTicketState.Event>()
    val event = _event.receiveAsFlow()


    init {
        getActiveSavingTypes()
    }
    fun getSavingTickets(filter: SavingTicketFilter): Flow<PagingData<SavingTicket>> =
        getSavingTicketsUseCase(filter)

    fun getSavingTicketsForCustomer(filter: SavingTicketFilter): Flow<PagingData<SavingTicket>> =
        getSavingTicketsForCustomerUseCase(filter)

    private fun getActiveSavingTypes() {
        viewModelScope.launch {
            getActiveSavingTypesUseCase().collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                savingTypeState = SavingTicketState.SavingTypeState.Success,
                                savingTypes = response.data
                            )
                        }
                    }

                    is ApiResponse.Failure -> {
                        _uiState.update {
                            it.copy(
                                savingTypeState = SavingTicketState.SavingTypeState.Error(response.errorMessage)
                            )
                        }
                    }

                    is ApiResponse.Loading -> {
                        _uiState.update {
                            it.copy(
                                savingTypeState = SavingTicketState.SavingTypeState.Loading
                            )
                        }
                    }
                }
            }
        }
    }

    fun onAction(action: SavingTicketState.Action) {
        when (action) {
            is SavingTicketState.Action.SearchCustomer -> {
                _uiState.update {
                    it.copy(search = action.search, filter = it.filter.copy(amount = if (action.search.isBlank()) BigDecimal.ZERO else BigDecimal(action.search)))
                }
            }

            is SavingTicketState.Action.SearchStaff -> {
                _uiState.update {
                    it.copy(
                        search = action.search,
                        filter = it.filter.copy(citizenID = action.search)
                    )
                }
            }


            is SavingTicketState.Action.OnSelectSavingTicket -> {
                viewModelScope.launch {
                    _event.send(SavingTicketState.Event.NavigateToDetail(action.savingTicket))
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
            is SavingTicketState.Action.OnChangeSavingType -> {
                _uiState.update {
                    it.copy(
                        savingTypeName = action.name,
                        filter = it.filter.copy(savingTypeId = action.id)
                    )}}
        }
    }


}

object SavingTicketState {
    data class UiState(

        val search: String = "",
        val savingTypeName: String?=null,
        val filter: SavingTicketFilter,
        val savingTypeState: SavingTypeState = SavingTypeState.Loading,
        val savingTypes: List<SavingType> = emptyList(),
    )

    sealed interface SavingTypeState {
        object Loading : SavingTypeState
        data object Success : SavingTypeState
        data class Error(val message: String) : SavingTypeState
    }

    sealed interface Event {
        data class NavigateToDetail(val savingTicket: SavingTicket) : Event
    }

    sealed interface Action {
        data class SearchCustomer(val search: String) : Action
        data class SearchStaff(val search: String) : Action
        data class OnChangeDateFilter(val startDate: LocalDate?, val endDate: LocalDate?) : Action
        data class OnChangeOrder(val order: String) : Action
        data class OnChangeSortBy(val sortBy: String) : Action
        data class OnSelectSavingTicket(val savingTicket: SavingTicket) : Action
        data class OnChangeSavingType(val id: Long, val name: String) : Action

    }
}