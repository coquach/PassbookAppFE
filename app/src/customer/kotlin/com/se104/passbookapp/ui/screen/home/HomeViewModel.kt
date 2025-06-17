package com.se104.passbookapp.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.domain.use_case.transaction.DepositUseCase
import com.se104.passbookapp.domain.use_case.transaction.WithDrawUseCase
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
class HomeViewModel @Inject constructor(
    private val depositUseCase: DepositUseCase,
    private val withDrawUseCase: WithDrawUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState.UiState())
    val uiState: StateFlow<HomeState.UiState> get() = _uiState.asStateFlow()

    private val _event = Channel<HomeState.Event>()
    val event = _event.receiveAsFlow()

    private fun deposit(userId: Long, amount: BigDecimal) {
        viewModelScope.launch {
            depositUseCase.invoke(userId, amount).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update { it.copy(loading = false, amount = BigDecimal.ZERO, error = null) }

                    }

                    is ApiResponse.Failure -> {
                        _uiState.update { it.copy(loading = false, error = response.errorMessage) }
                        _event.send(HomeState.Event.ShowError)

                    }

                    is ApiResponse.Loading -> {
                        _uiState.update { it.copy(loading = true, error = null) }
                    }
                }

            }
        }
    }

    private fun withDraw(userId: Long, amount: BigDecimal) {
        viewModelScope.launch {
            withDrawUseCase.invoke(userId, amount).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update { it.copy(loading = false, amount = BigDecimal.ZERO, error = null) }
                    }

                    is ApiResponse.Failure -> {
                        _uiState.update { it.copy(loading = false, error = response.errorMessage) }
                        _event.send(HomeState.Event.ShowError)
                    }

                    is ApiResponse.Loading -> {
                        _uiState.update { it.copy(loading = true, error = null) }
                    }
                }
            }

        }
    }

    fun onAction(action: HomeState.Action) {
        when (action) {
            is HomeState.Action.Deposit -> {

            }
            is HomeState.Action.Withdraw -> {
                
            }
            is HomeState.Action.OnAmountChange -> {
                _uiState.update { it.copy(amount = action.amount ?: BigDecimal.ZERO) }
            }
            is HomeState.Action.SetAmountDefault -> {
                _uiState.update { it.copy(amount = BigDecimal.ZERO) }
            }
        }
    }
}

object HomeState {
    data class UiState(
        val loading: Boolean = false,
        val error: String? = null,
        val amount: BigDecimal = BigDecimal.ZERO,
    )

    sealed interface Event {
        object ShowError : Event
    }

    sealed interface Action {
        object Deposit : Action
        object Withdraw : Action
        data class OnAmountChange(val amount: BigDecimal?) : Action
        data object SetAmountDefault : Action
    }


}