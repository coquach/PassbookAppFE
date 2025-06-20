package com.se104.passbookapp.ui.screen.profile

import androidx.lifecycle.ViewModel
import com.se104.passbookapp.data.model.User
import com.se104.passbookapp.domain.use_case.user.GetMyInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getMyInfoUseCase: GetMyInfoUseCase
) : ViewModel() {
    private val _event = Channel<>()

}
object ProfileState{
    sealed interface GetMyInfoState{
        object Loading: GetMyInfoState
        data class Success(val user: User): GetMyInfoState
        data class Error(val message: String): GetMyInfoState
    }
    sealed interface Event{
        data object OnBack: Event
        data object NavigateToChangePassword: Event
    }
    sealed interface Action{
        data object OnBack: Action
        data object NavigateToChangePassword: Action
    }
}
