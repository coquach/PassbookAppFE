package com.se104.passbookapp.ui.screen.transaction

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.se104.passbookapp.data.dto.filter.TransactionFilter
import com.se104.passbookapp.data.model.Transaction
import com.se104.passbookapp.domain.use_case.transaction.GetTransactionsForCustomerUseCase
import com.se104.passbookapp.domain.use_case.transaction.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getTransactionsForCustomerUseCase: GetTransactionsForCustomerUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        TransactionState.UiState(
            filter = TransactionFilter(
                startDate = LocalDate.now().minusDays(1),
                endDate = LocalDate.now(),
            )
        )
    )
    val uiState: StateFlow<TransactionState.UiState> get() = _uiState.asStateFlow()

    private val _event = Channel<TransactionState.Event>()
    val event = _event.receiveAsFlow()

    fun getTransactions(filter: TransactionFilter): Flow<PagingData<Transaction>> =
        getTransactionsUseCase(filter)

    fun getTransactionsForCustomer(filter: TransactionFilter): Flow<PagingData<Transaction>> =
        getTransactionsForCustomerUseCase(filter)

    fun onAction(action: TransactionState.Action) {
        when (action) {
            is TransactionState.Action.SearchCustomer -> {
                _uiState.update {
                    it.copy(
                        search = action.search, filter = it.filter.copy(
                            amount = if (action.search.isBlank()) BigDecimal.ZERO else BigDecimal(
                                action.search
                            )
                        )
                    )
                }
            }

            is TransactionState.Action.SearchStaff -> {
                _uiState.update {
                    it.copy(
                        search = action.search,
                        filter = it.filter.copy(citizenID = action.search)
                    )
                }
            }

            is TransactionState.Action.OnChangeOrder -> {
                _uiState.update {
                    it.copy(
                        filter = it.filter.copy(order = action.order)
                    )
                }
            }

            is TransactionState.Action.OnChangeSortBy -> {
                _uiState.update {
                    it.copy(
                        filter = it.filter.copy(sortBy = action.sortBy)
                    )
                }
            }

            is TransactionState.Action.OnChangeDateFilter -> {
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

object TransactionState {
    data class UiState(
        val search: String = "",
        val filter: TransactionFilter,
    )

    sealed interface Event {

    }

    sealed interface Action {
        data class SearchCustomer(val search: String) : Action
        data class SearchStaff(val search: String) : Action
        data class OnChangeDateFilter(val startDate: LocalDate?, val endDate: LocalDate?) : Action
        data class OnChangeOrder(val order: String) : Action
        data class OnChangeSortBy(val sortBy: String) : Action

    }
}