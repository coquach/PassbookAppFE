package com.se104.passbookapp.ui.screen.saving_type

import android.os.Message
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.se104.passbookapp.BaseViewModel
import com.se104.passbookapp.CoroutinesErrorHandler
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.request.RegisterRequest
import com.se104.passbookapp.data.model.SavingType
import com.se104.passbookapp.data.repository.SavingTypeRepository
import com.se104.passbookapp.ui.screen.auth.signup.SignUpViewModel.SignUpEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavingTypeViewModel @Inject constructor(
    private val savingTypeRepository: SavingTypeRepository,
) : BaseViewModel() {

    private val _event = Channel<SavingTypeEvents>()
    val event = _event.receiveAsFlow()

    private val _savingTypesResponse =
        MutableStateFlow<ApiResponse<List<SavingType>>>(ApiResponse.Loading)


    private val _savingTypeList = MutableStateFlow<List<SavingType>>(emptyList())
    val savingTypeList = _savingTypeList.asStateFlow()

    fun getActiveSavingTypes(
        request: RegisterRequest,
        coroutinesErrorHandler: CoroutinesErrorHandler,
    ) = baseRequest(
        stateFlow = _savingTypesResponse,
        errorHandler = errorHandler,
        request = {
            savingTypeRepository.getActiveSavingTypes()
        },
        onSuccess = {
            _savingTypeList.value = it

        },
        onFailure = {
            viewModelScope.launch {
                _event.send(SavingTypeEvents.ShowError(it))
            }
        }
    )

    fun onAddSavingTypeClicked(savingType: SavingType) {
        viewModelScope.launch {
            _event.send(SavingTypeEvents.AddSavingType(savingType))
        }
    }

    fun onUpdateSavingTypeClicked(savingType: SavingType) {
        viewModelScope.launch {
            _event.send(SavingTypeEvents.UpdateSavingType(savingType))
        }
    }

    private val errorHandler = object : CoroutinesErrorHandler {
        override fun onError(message: String) {
            viewModelScope.launch {
                _event.send(SavingTypeEvents.ShowError(message))
            }

        }
    }

    sealed class SavingTypeEvents {
        data class AddSavingType(val savingType: SavingType) : SavingTypeEvents()
        data class UpdateSavingType(val savingType: SavingType) : SavingTypeEvents()
        data class ShowError(val message: String) : SavingTypeEvents()
    }
}