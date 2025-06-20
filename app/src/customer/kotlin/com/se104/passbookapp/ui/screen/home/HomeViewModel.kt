package com.se104.passbookapp.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.filter.UserFilter
import com.se104.passbookapp.data.model.User
import com.se104.passbookapp.domain.use_case.transaction.DepositUseCase
import com.se104.passbookapp.domain.use_case.transaction.WithDrawUseCase
import com.se104.passbookapp.domain.use_case.user.GetMyInfoUseCase
import com.se104.passbookapp.domain.use_case.user.GetUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val depositUseCase: DepositUseCase,
    private val withDrawUseCase: WithDrawUseCase,
    private val getMyInfoUseCase: GetMyInfoUseCase,
    private val getUsersUseCase: GetUsersUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState.UiState())
    val uiState: StateFlow<HomeState.UiState> get() = _uiState.asStateFlow()

    private val _event = Channel<HomeState.Event>()
    val event = _event.receiveAsFlow()

    fun getGreetingTitle(): String {
        val hour = LocalTime.now().hour

        val greeting = when (hour) {
            in 6..11 -> "buổi sáng"
            in 12..17 -> "buổi chiều"
            in 18..23 -> "buổi tối"
            else -> "buổi khuya"
        }

        return "Chào $greeting"
    }

    fun getUsers(filter: UserFilter) = getUsersUseCase(filter)
    fun getMyInfo() {
        viewModelScope.launch {
            getMyInfoUseCase().collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                getInfoState = HomeState.GetInfoState.Success(response.data)
                            )
                        }
                    }

                    is ApiResponse.Failure -> {
                        _uiState.update {
                            it.copy(
                                getInfoState = HomeState.GetInfoState.Error(response.errorMessage)
                            )
                        }
                    }

                    is ApiResponse.Loading -> {
                        _uiState.update {
                            it.copy(
                                getInfoState = HomeState.GetInfoState.Loading
                            )
                        }
                    }
                }
            }
        }
    }

    private fun deposit(userId: Long, amount: BigDecimal) {
        viewModelScope.launch {
            depositUseCase.invoke(userId, amount).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update {
                            it.copy(
                                loading = false,
                                amount = BigDecimal.ZERO,
                                error = null
                            )
                        }
                        _event.send(HomeState.Event.NavigateToActionSuccess)

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
                        _uiState.update {
                            it.copy(
                                loading = false,
                                amount = BigDecimal.ZERO,
                                error = null
                            )
                        }
                        _event.send(HomeState.Event.NavigateToActionSuccess)
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
                deposit(uiState.value.userSelected!!, uiState.value.amount)
            }

            is HomeState.Action.Withdraw -> {
                withDraw(uiState.value.userSelected!!, uiState.value.amount)
            }

            is HomeState.Action.OnAmountChange -> {
                _uiState.update { it.copy(amount = action.amount ?: BigDecimal.ZERO) }
            }

            is HomeState.Action.SetAmountDefault -> {
                _uiState.update { it.copy(amount = BigDecimal.ZERO) }
            }

            is HomeState.Action.OnChangeOrder -> {
                _uiState.update {
                    it.copy(
                        filter = it.filter.copy(order = action.order)
                    )
                }
            }

            is HomeState.Action.OnChangeSortBy -> {
                _uiState.update {
                    it.copy(
                        filter = it.filter.copy(sortBy = action.sortBy)
                    )
                }
            }

            is HomeState.Action.OnSearch -> {
                _uiState.update {
                    it.copy(
                        search = action.search,
                        filter = it.filter.copy(citizenID = action.search)
                    )
                }
            }

            is HomeState.Action.OnSelectUser -> {
                _uiState.update {
                    it.copy(userSelected = action.user)
                }
            }
            HomeState.Action.ShowTransactionSheet -> {
                viewModelScope.launch {
                    _event.send(HomeState.Event.ShowTransactionSheet)
                }
            }
        }
    }
}

object HomeState {
    data class UiState(
        val loading: Boolean = false,
        val error: String? = null,
        val amount: BigDecimal = BigDecimal.ZERO,
        val search: String = "",
        val filter: UserFilter = UserFilter(groupName = "CUSTOMER"),
        val getInfoState: GetInfoState = GetInfoState.Loading,
        val userSelected: Long? = null,

    )

    sealed interface GetInfoState {
        data class Success(val data: User) : GetInfoState
        data class Error(val message: String) : GetInfoState
        object Loading : GetInfoState
    }

    sealed interface Event {
        object ShowError : Event
        object ShowTransactionSheet: Event
        data object NavigateToActionSuccess : Event
    }

    sealed interface Action {
        data class OnSearch(val search: String) : Action
        data class OnChangeOrder(val order: String) : Action
        data class OnChangeSortBy(val sortBy: String) : Action
        object Deposit : Action
        object Withdraw : Action
        data class OnAmountChange(val amount: BigDecimal?) : Action
        data object SetAmountDefault : Action
        data class OnSelectUser(val user: Long) : Action
        data object ShowTransactionSheet : Action
    }


}