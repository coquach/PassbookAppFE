package com.se104.passbookapp.ui.screen.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.se104.passbookapp.data.dto.filter.TransactionFilter
import com.se104.passbookapp.data.model.Transaction
import com.se104.passbookapp.domain.use_case.transaction.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel()  {
    private val _uiState = MutableStateFlow(TransactionState.UiState())
    val uiState: StateFlow<TransactionState.UiState> get() = _uiState.asStateFlow()

    private val _event = Channel<TransactionState.Event>()
    val event = _event.receiveAsFlow()

    private val _transactions = MutableStateFlow<PagingData<Transaction>>(PagingData.empty())
    val transactions: StateFlow<PagingData<Transaction>> get() = _transactions

    private fun getTransactions(filter: TransactionFilter) {
        viewModelScope.launch {
            getTransactionsUseCase(filter).collect {
                _transactions.value = it
            }
        }
    }
    fun onAction(action: TransactionState.Action) {
        when (action) {
            is TransactionState.Action.Search -> {
                _uiState.value = _uiState.value.copy(search = action.search)
            }
            is TransactionState.Action.Filter -> {
                getTransactions(action.filter)
            }

        }
    }
}

object TransactionState{
    data class UiState(
        val search: String = "",
    )
    sealed interface Event {
       
    }

    sealed interface Action {
        data class Search(val search: String) : Action
        data class Filter(val filter: TransactionFilter) : Action

    }
}