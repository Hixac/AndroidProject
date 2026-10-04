package com.example.coflow.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

// Логин или регистрация
enum class AuthMode { LOGIN, REGISTER }

@Composable
fun AuthScreen(
    state: AuthUiState,
    onContactChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmChange: (String) -> Unit,
    onToggleMode: () -> Unit,
    onSubmit: () -> Unit,
    ){

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
    ) {

        // --- Заголовок: меняется в зависимости от режима
        Text(
            text = if (state.mode == AuthMode.LOGIN) "С возвращением!" else "Создать аккаунт",
            style = MaterialTheme.typography.headlineMedium,
        )

        // --- Поле почты/телефона
        OutlinedTextField(
            value = state.contact,
            onValueChange = { onContactChange(it) },
            label = { Text("Почта или телефон") },
            singleLine = true,
            isError = state.contactError != null,
            supportingText = {
                state.contactError?.let { Text(it) }
            },
            modifier = Modifier.fillMaxWidth(),
        )

        // --- Пароль
        OutlinedTextField(
            value = state.password,
            onValueChange = { onPasswordChange(it) },
            label = { Text("Пароль") },
            singleLine = true,
            isError = state.passwordError != null,
            supportingText = {
                state.passwordError?.let { Text(it) }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        )

        // --- Повтор пароля: только в режиме регистрации
        if (state.mode == AuthMode.REGISTER) {
            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = {  onConfirmChange(it) },
                label = { Text("Повторите пароль") },
                singleLine = true,
                isError = state.confirmError != null,
                supportingText = {
                    state.confirmError?.let { Text(it) }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
            )
        }

        // --- Кнопка
        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
        ) {
            Text(if (state.mode == AuthMode.LOGIN) "Войти" else "Зарегистрироваться")
        }

        // --- Переключатель режима
        TextButton(
            onClick = onToggleMode,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp),
        ){
            Text(
                if (state.mode == AuthMode.LOGIN) "Нет аккаунта?"
                else "Уже есть аккаунт?"
            )
        }
    }
}


@Composable
fun AuthRoute(
    onAuthSuccess: () -> Unit,                      // сюда потом придёт переход на главный экран
    viewModel: AuthViewModel = AuthViewModel(),         // ← добавь этот импорт
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    AuthScreen(
        state = state,
        onContactChange = viewModel::onContactChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmChange = viewModel::onConfirmChange,
        onToggleMode = viewModel::onToggleMode,
        onSubmit = viewModel::onSubmit,
    )
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun Auth_Login_Empty() {
    MaterialTheme {
        AuthScreen(
            state = AuthUiState(),
            onContactChange = {},
            onPasswordChange = {},
            onConfirmChange = {},
            onToggleMode = {},
            onSubmit = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun Auth_Login_Error() {
    MaterialTheme {
        AuthScreen(
            state = AuthUiState(
                contact = "liza@mail",
                password = "123",
                contactError = "Формат: liza@mail.ru или +7 999 123 45 67",
                passwordError = "Минимум 8 символов",
            ),
            onContactChange = {},
            onPasswordChange = {},
            onConfirmChange = {},
            onToggleMode = {},
            onSubmit = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun Auth_Register_Empty() {
    MaterialTheme {
        AuthScreen(
            state = AuthUiState(mode = AuthMode.REGISTER),
            onContactChange = {},
            onPasswordChange = {},
            onConfirmChange = {},
            onToggleMode = {},
            onSubmit = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun Auth_Register_Mismatch() {
    MaterialTheme {
        AuthScreen(
            state = AuthUiState(
                mode = AuthMode.REGISTER,
                contact = "liza@mail.ru",
                password = "parol123",
                confirmPassword = "parol124",
                confirmError = "Пароли не совпадают",
            ),
            onContactChange = {},
            onPasswordChange = {},
            onConfirmChange = {},
            onToggleMode = {},
            onSubmit = {},
        )
    }
}

