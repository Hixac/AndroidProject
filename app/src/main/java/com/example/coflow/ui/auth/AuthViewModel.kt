package com.example.coflow.ui.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AuthUiState(
    val mode: AuthMode = AuthMode.LOGIN, // по умолчанию первая авторизация
    val contact: String = "", // почта или телефон
    val password: String = "", // пароль
    val confirmPassword: String = "", // подтверждение пароля
    val contactError: String? = null,
    val passwordError: String? = null,
    val confirmError: String? = null,
    val isSubmitting: Boolean = false,
)

class AuthViewModel : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    // Сохранение всех изменений в состояние
    fun onContactChange(value: String) {
        _state.value = _state.value.copy(contact = value, contactError = null)
    }

    fun onPasswordChange(value: String) {
        _state.value = _state.value.copy(password = value, passwordError = null)
    }

    fun onConfirmChange(value: String) {
        _state.value = _state.value.copy(confirmPassword = value, confirmError = null)
    }

    fun onToggleMode() {
        _state.value = AuthUiState(
            mode = if (_state.value.mode == AuthMode.LOGIN) AuthMode.REGISTER else AuthMode.LOGIN
        )
    }

    fun onSubmit() {
        // TODO: Проверка заполненоности (поменять)
        val current = _state.value
        _state.value = current.copy(
            contactError = if (current.contact.isBlank()) "Введите почту или телефон" else null,
            passwordError = if (current.password.length < 8) "Минимум 8 символов" else null,
        )
    }
}