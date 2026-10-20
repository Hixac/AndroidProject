package com.example.coflow.ui.auth

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AuthMode {
    LOGIN,
    REGISTER
}

data class AuthUiState(
    val mode: AuthMode = AuthMode.LOGIN,
    val username: String = "",          // <-- НОВОЕ: Имя пользователя
    val contact: String = "",           // Почта или телефон
    val password: String = "",          // Пароль
    val confirmPassword: String = "",   // Подтверждение пароля
    val usernameError: String? = null,  // <-- НОВОЕ: Ошибка имени
    val contactError: String? = null,
    val passwordError: String? = null,
    val confirmError: String? = null,
    val isSubmitting: Boolean = false,
)

class AuthViewModel : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun onUsernameChange(value: String) {
        _state.value = _state.value.copy(username = value, usernameError = null)
    }

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
        // При переключении режима очищаем поля и ошибки для чистого старта
        val newMode = if (_state.value.mode == AuthMode.LOGIN) AuthMode.REGISTER else AuthMode.LOGIN
        _state.value = AuthUiState(mode = newMode)
    }

    fun onSubmit() {
        val current = _state.value

        // Валидация имени пользователя (только для регистрации)
        val usernameError = if (current.mode == AuthMode.REGISTER && current.username.isBlank()) {
            "Введите имя пользователя"
        } else null

        // Валидация почты/телефона
        val contactError = if (current.contact.isBlank()) {
            "Введите почту или телефон"
        } else null

        // Валидация пароля
        val passwordError = if (current.password.length < 8) {
            "Минимум 8 символов"
        } else null

        // Валидация подтверждения пароля (только для регистрации)
        val confirmError = if (current.mode == AuthMode.REGISTER && current.password != current.confirmPassword) {
            "Пароли не совпадают"
        } else null

        _state.value = current.copy(
            usernameError = usernameError,
            contactError = contactError,
            passwordError = passwordError,
            confirmError = confirmError,
            isSubmitting = false // Здесь можно запустить реальную загрузку
        )
    }
}