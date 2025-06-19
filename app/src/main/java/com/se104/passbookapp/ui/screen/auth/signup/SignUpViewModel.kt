package com.se104.passbookapp.ui.screen.auth.signup


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.request.RegisterRequest
import com.se104.passbookapp.domain.use_case.auth.RegisterUseCase
import com.se104.passbookapp.ui.screen.components.text_field.validateField
import com.se104.passbookapp.utils.StringUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
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
class SignUpViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val email = savedStateHandle.toRoute<com.se104.passbookapp.navigation.SignUp>().email


    private val _uiState = MutableStateFlow(SignUp.UiState(
        email = email
    ))
    val uiState: StateFlow<SignUp.UiState> get() = _uiState.asStateFlow()

    private val _event = Channel<SignUp.Event>()
    val event = _event.receiveAsFlow()

    private fun signUp() {
        viewModelScope.launch {
            val request = RegisterRequest(
                _uiState.value.email,
                _uiState.value.password,
                _uiState.value.confirmPassword,
                _uiState.value.phoneNumber,
                _uiState.value.fullName,
                _uiState.value.address,
                _uiState.value.citizenId,
                StringUtils.formatLocalDate(_uiState.value.dateOfBirth)!!,


                )
            registerUseCase.invoke(request).collect { response ->
                when (response) {
                    is ApiResponse.Success -> {
                        _uiState.update { it.copy(loading = false) }
                        _event.send(SignUp.Event.ShowSuccessDialog)
                    }

                    is ApiResponse.Failure -> {
                        _uiState.update { it.copy(loading = false, error = response.errorMessage) }
                        _event.send(SignUp.Event.ShowError)
                    }

                    is ApiResponse.Loading -> {
                        _uiState.update { it.copy(loading = true) }
                    }
                }
            }
        }
    }

    fun validate(type: String) {
        val current = _uiState.value

        var passwordError: String? = current.passwordError
        var confirmPasswordError: String? = current.confirmPasswordError
        var number: String? = current.phoneNumberError
        var fullName: String? = current.fullNameError
        var address: String? = current.addressError
        var citizenId: String? = current.citizenIdError

        when (type) {

            "password" -> {
                passwordError = validateField(
                    current.password.trim(),
                    "Mật khẩu phải có ít nhất 6 ký tự"
                ) { it.length >= 6 }

            }

            "confirmPassword" -> {
                confirmPasswordError = validateField(
                    current.confirmPassword.trim(),
                    "Mật khẩu không khớp"

                ) { it == current.password }
            }

            "phoneNumber" -> {
                number = validateField(
                    current.phoneNumber.trim(),
                    "Số điện thoại không hợp lệ"
                ) { it.matches(Regex("^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-9]))(\\s|\\.)?[0-9]{3}(\\s|\\.)?[0-9]{3}$")) }
            }

            "fullName" -> {
                fullName = validateField(
                    current.fullName.trim(),
                    "Họ tên không hợp lệ"
                ) { it.matches(Regex("^[a-zA-Z\\s]+$")) }
            }

            "address" -> {
                address = validateField(
                    current.address.trim(),
                    "Địa chỉ không hợp lệ"
                ) { it.matches(Regex("^[a-zA-Z\\s]+$")) }
            }

            "citizenId" -> {
                citizenId = validateField(
                    current.citizenId.trim(),
                    "CMND không hợp lệ"
                ) { it.matches(Regex("^[0-9]{9,12}$")) }
            }

        }
        val isValid =
            passwordError == null && confirmPasswordError == null && number == null && fullName == null && address == null && citizenId == null
        _uiState.update {
            it.copy(
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError,
                isValid = isValid
            )
        }
    }


    fun onAction(action: SignUp.Action) {
        when (action) {
            is SignUp.Action.EmailChanged -> {
                _uiState.update { it.copy(email = action.email) }
            }

            is SignUp.Action.PasswordChanged -> {
                _uiState.update { it.copy(password = action.password) }
            }

            is SignUp.Action.ConfirmPasswordChanged -> {
                _uiState.update { it.copy(confirmPassword = action.confirmPassword) }
            }

            is SignUp.Action.PhoneNumberChanged -> {
                _uiState.update { it.copy(phoneNumber = action.phoneNumber) }
            }

            is SignUp.Action.FullNameChanged -> {
                _uiState.update { it.copy(fullName = action.fullName) }
            }

            is SignUp.Action.AddressChanged -> {
                _uiState.update { it.copy(address = action.address) }
            }

            is SignUp.Action.CitizenIdChanged -> {
                _uiState.update { it.copy(citizenId = action.citizenId) }
            }

            SignUp.Action.LoginClicked -> {
                viewModelScope.launch {
                    _event.send(SignUp.Event.NavigateToLogin)
                }
            }

            SignUp.Action.SignUpClicked -> {
                signUp()
            }

            is SignUp.Action.DateOfBirthChanged -> {
                _uiState.update { it.copy(dateOfBirth = action.dateOfBirth) }
            }

            SignUp.Action.AuthClicked -> {
                viewModelScope.launch {
                    _event.send(SignUp.Event.NavigateToAuth)
                }
            }
            SignUp.Action.OnBack -> {
                viewModelScope.launch {
                    _event.send(SignUp.Event.OnBack)
                }
            }


        }
    }
}

object SignUp {
    data class UiState(
        val email: String,
        val password: String = "",
        val passwordError: String? = null,
        val confirmPassword: String = "",
        val confirmPasswordError: String? = null,
        val phoneNumber: String = "",
        val phoneNumberError: String? = null,
        val fullName: String = "",
        val fullNameError: String? = null,
        val address: String = "",
        val addressError: String? = null,
        val citizenId: String = "",
        val citizenIdError: String? = null,
        val dateOfBirth: LocalDate? = LocalDate.now(),
        val loading: Boolean = false,
        val error: String? = null,
        val isValid: Boolean = false,
    )

    sealed interface Event {
        data object NavigateToLogin : Event
        data object NavigateToAuth : Event
        data object ShowError : Event
        data object ShowSuccessDialog : Event
        data object OnBack: Event

    }

    sealed interface Action {
        data class EmailChanged(val email: String) : Action
        data class PasswordChanged(val password: String) : Action
        data class ConfirmPasswordChanged(val confirmPassword: String) : Action
        data class PhoneNumberChanged(val phoneNumber: String) : Action
        data class FullNameChanged(val fullName: String) : Action
        data class AddressChanged(val address: String) : Action
        data class CitizenIdChanged(val citizenId: String) : Action
        data class DateOfBirthChanged(val dateOfBirth: LocalDate) : Action
        data object LoginClicked : Action
        data object SignUpClicked : Action
        data object AuthClicked : Action
        data object OnBack: Action


    }
}

