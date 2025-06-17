package com.se104.passbookapp

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.se104.passbookapp.data.datastore.WelcomeRepository
import com.se104.passbookapp.di.TokenManager
import com.se104.passbookapp.navigation.Home
import com.se104.passbookapp.navigation.NavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val welcomeRepository: WelcomeRepository,
    private val tokenManager: TokenManager,
) : ViewModel() {

    private val _startDestination: MutableState<NavRoute?> = mutableStateOf(null)
    val startDestination: State<NavRoute?> = _startDestination

    private val _navigateEventChannel = Channel<UiEvent>()
    val navigateEventFlow = _navigateEventChannel.receiveAsFlow()


    private val currentTokenFlow = tokenManager.getAccessToken()

    init {
        viewModelScope.launch() {
            val initialToken = currentTokenFlow.firstOrNull()
            updateStartDestination(initialToken)
            currentTokenFlow.drop(1).collect { token ->
                if (token == null) {
                    Log.d("collect: ", "done")
                    _navigateEventChannel.send(UiEvent.NavigateToAuth)
                }
            }
        }

    }

    private suspend fun updateStartDestination(token: String?) {
        tokenManager.getAccessToken().collect { accessToken ->
            if (accessToken.isNullOrEmpty()) {
                _startDestination.value = Home
            } else {

                _startDestination.value = Home
            }

        }


    }

    sealed class UiEvent {
        data object NavigateToAuth : UiEvent()
    }


}