package com.se104.passbookapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.se104.passbookapp.data.dto.ApiResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class BaseViewModel : ViewModel() {


    private var mJob: Job? = null

    protected fun <T> baseRequest(
        stateFlow: MutableStateFlow<ApiResponse<T>>,
        errorHandler: CoroutinesErrorHandler,
        request: () -> Flow<ApiResponse<T>>,
        onSuccess: ((T) -> Unit)? = null,
        onFailure: ((String) -> Unit)? = null
    ) {

        mJob = viewModelScope.launch(Dispatchers.IO + CoroutineExceptionHandler { _, error ->
            viewModelScope.launch(Dispatchers.Main) {
                errorHandler.onError(error.localizedMessage ?: "Error occured! Please try again.")
            }
        }) {
            request().collect {
                withContext(Dispatchers.Main) {
                    stateFlow.value = it
                    when(it){
                        is ApiResponse.Failure -> {
                            val msg = it.errorMessage
                            onFailure?.invoke(msg)
                        }
                        ApiResponse.Loading -> {

                        }
                        is ApiResponse.Success -> {
                            onSuccess?.invoke(it.data)
                        }
                    }
                }

            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        mJob?.let {
            if (it.isActive) {
                it.cancel()
            }
        }
    }

    sealed class UiState<out T> {
        data class Success<out T>(val data: T?) : UiState<T>()
        data class Failure(val error: String) : UiState<Nothing>()
        data object Loading : UiState<Nothing>()
    }
}

interface CoroutinesErrorHandler {
    fun onError(message: String)
}

