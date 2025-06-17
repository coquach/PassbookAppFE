package com.se104.passbookapp.ui.screen.saving_ticket.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.model.SavingType
import com.se104.passbookapp.domain.use_case.saving_type.GetActiveSavingTypesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectSavingTypeViewModel @Inject constructor(
        private val getActiveSavingTypesUseCase: GetActiveSavingTypesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SelectSavingTypeState.UiState())
    val uiState get()= _uiState.asStateFlow()

    private val _event = Channel<SelectSavingTypeState.Event>()
    val event get() = _event.receiveAsFlow()


    init{
        getSavingTypes()
    }
    fun getSavingTypes(){
        viewModelScope.launch {
            getActiveSavingTypesUseCase().collect{ response ->
                when(response){
                    is ApiResponse.Success -> {
                        _uiState.value = _uiState.value.copy(
                            savingTypes = response.data,
                            savingTypesState = SelectSavingTypeState.SavingTypes.Success(response.data)
                        )
                    }
                    is ApiResponse.Failure -> {
                        _uiState.value = _uiState.value.copy(
                            savingTypesState = SelectSavingTypeState.SavingTypes.Error(response.errorMessage)
                        )
                    }
                    is ApiResponse.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            savingTypesState = SelectSavingTypeState.SavingTypes.Loading
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: SelectSavingTypeState.Action){
        when(action){
            is SelectSavingTypeState.Action.OnSelectSavingType -> {
                viewModelScope.launch {
                _event.send(SelectSavingTypeState.Event.GoToDetail(action.savingType))
                }
            }
            is SelectSavingTypeState.Action.OnBack -> {
                viewModelScope.launch {
                    _event.send(SelectSavingTypeState.Event.OnBack)
                }
            }
        }
    }
}
object SelectSavingTypeState {
    data class UiState(
        val savingTypes: List<SavingType> = emptyList(),
        val savingTypesState: SavingTypes = SavingTypes.Loading
    )
    sealed interface SavingTypes{
        object Loading: SavingTypes
        data class Success(val savingTypes: List<SavingType>): SavingTypes
        data class Error(val message: String): SavingTypes
    }
    sealed interface Event{
        data class GoToDetail(val savingType: SavingType): Event
        data object OnBack: Event
    }
    sealed interface Action{
        data class OnSelectSavingType(val savingType: SavingType): Action
        data object OnBack: Action
    }
}

