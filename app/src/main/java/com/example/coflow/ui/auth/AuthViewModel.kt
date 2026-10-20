package com.example.coflow.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coflow.data.firebase.AuthRepository
import com.example.coflow.data.firebase.FirebaseAuthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class AuthMode {
    LOGIN,
    REGISTER
}

data class AuthUiState(
    val mode: AuthMode = AuthMode.LOGIN,
    val username: String = "",          // Имя пользователя
    val contact: String = "",           // Почта или телефон
    val password: String = "",          // Пароль
    val confirmPassword: String = "",   // Подтверждение пароля
    val usernameError: String? = null,  // Ошибка имени
    val contactError: String? = null,
    val passwordError: String? = null,
    val confirmError: String? = null,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
)

class AuthViewModel(
    private val repository: AuthRepository = FirebaseAuthRepositoryImpl()
) : ViewModel() {
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

    fun onSubmit(onAuthSuccess: () -> Unit) {
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

        if (usernameError != null || contactError != null || passwordError != null || confirmError != null) {
            _state.value = current.copy(
                usernameError = usernameError,
                contactError = contactError,
                passwordError = passwordError,
                confirmError = confirmError,
                isSubmitting = false
            )
            return
        }

        _state.value = current.copy(
            isSubmitting = true,
            usernameError = null,
            contactError = null,
            passwordError = null,
            confirmError = null
        )

        // Вызов бэкенда
        viewModelScope.launch {
            val result = if (current.mode == AuthMode.REGISTER) {
                repository.register(current.contact, current.password, current.username)
            } else {
                repository.login(current.contact, current.password)
            }

            result.fold(
                onSuccess = {
                    _state.value = _state.value.copy(isSubmitting = false)
                    onAuthSuccess()
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(
                        isSubmitting = false,
                        errorMessage = error.message
                    )
                }
            )
        }
    }

}