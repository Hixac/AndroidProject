
package com.example.coflow.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.coflow.ui.theme.CoFlowColors
import com.example.coflow.ui.theme.CoFlowShapes
import com.example.coflow.ui.theme.CoFlowSpacing
import com.example.coflow.ui.theme.coFlowAuthFieldColors
import com.example.coflow.ui.theme.coFlowBackgroundBrush
import androidx.compose.ui.text.style.TextAlign

// Режим экрана: авторизация или регистрация.
enum class AuthMode {
    LOGIN,
    REGISTER
}

@Composable
fun AuthScreen(
    state: AuthUiState,
    onContactChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmChange: (String) -> Unit,
    onToggleMode: () -> Unit,
    onSubmit: () -> Unit,
) {
    // Фон с общим градиентом CoFlow.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(coFlowBackgroundBrush())
            .imePadding(),
        contentAlignment = Alignment.Center
    ) {
        // Прокрутка нужна для небольших экранов и открытой клавиатуры.
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = CoFlowSpacing.ScreenHorizontal),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Стеклянная карточка формы.
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 12.dp,
                        shape = CoFlowShapes.Card,
                        ambientColor = CoFlowColors.Blue.copy(alpha = 0.10f),
                        spotColor = CoFlowColors.Blue.copy(alpha = 0.14f)
                    )
                    .border(
                        width = 1.dp,
                        color = CoFlowColors.BlueBorder.copy(alpha = 0.75f),
                        shape = CoFlowShapes.Card
                    ),
                shape = CoFlowShapes.Card,
                color = CoFlowColors.CardBackground
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 20.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Заголовок формы.
                    Text(
                        text = if (state.mode == AuthMode.LOGIN) {
                            "Добро пожаловать\nв CoFlow"
                        } else {
                            "Добро пожаловать\nв CoFlow"
                        },
                        style = MaterialTheme.typography.headlineMedium,
                        color = CoFlowColors.TextPrimary,
                        textAlign = TextAlign.Center

                    )

                    // Подпись под заголовком.
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (state.mode == AuthMode.LOGIN) {
                            "Авторизация"
                        } else {
                            "Регистрация"
                        },
                        style = MaterialTheme.typography.titleMedium,
                        color = CoFlowColors.TextPrimary
                    )

                    // Уменьшенный отступ от подписи до первого поля.
                    Spacer(modifier = Modifier.height(8.dp))

                    // Поле почты или телефона.
                    OutlinedTextField(
                        value = state.contact,
                        onValueChange = onContactChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Почта")
                        },
                        placeholder = {
                            Text("name@example.com")
                        },
                        singleLine = true,
                        isError = state.contactError != null,
                        supportingText = state.contactError?.let { error ->
                            @Composable {
                                Text(text = error)
                            }
                        },
                        shape = CoFlowShapes.Input,
                        colors = coFlowAuthFieldColors()
                    )

                    // Ошибка отображается только при её наличии.
                    // Между полями больше нет дополнительного Spacer.
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = onPasswordChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 0.dp),
                        label = {
                            Text("Пароль")
                        },
                        placeholder = {
                            Text("Не менее 8 символов")
                        },
                        singleLine = true,
                        isError = state.passwordError != null,
                        supportingText = state.passwordError?.let { error ->
                            @Composable {
                                Text(text = error)
                            }
                        },
                        shape = CoFlowShapes.Input,
                        colors = coFlowAuthFieldColors()
                    )

                    // Повтор пароля показывается только при регистрации.
                    if (state.mode == AuthMode.REGISTER) {
                        OutlinedTextField(
                            value = state.confirmPassword,
                            onValueChange = onConfirmChange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 0.dp),
                            label = {
                                Text("Повторите пароль")
                            },
                            placeholder = {
                                Text("Введите пароль ещё раз")
                            },
                            singleLine = true,
                            isError = state.confirmError != null,
                            supportingText = state.confirmError?.let { error ->
                                @Composable {
                                    Text(text = error)
                                }
                            },
                            shape = CoFlowShapes.Input,
                            colors = coFlowAuthFieldColors()
                        )
                    }

                    // Небольшой отступ перед основной кнопкой.
                    Spacer(modifier = Modifier.height(16.dp))

                    // Основная синяя кнопка.
                    Button(
                        onClick = onSubmit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = CoFlowShapes.Button,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CoFlowColors.Blue,
                            contentColor = CoFlowColors.White
                        )
                    ) {
                        Text(
                            text = if (state.mode == AuthMode.LOGIN) {
                                "Войти"
                            } else {
                                "Зарегистрироваться"
                            },
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    // Переключение между авторизацией и регистрацией.
                    Spacer(modifier = Modifier.height(4.dp))

                    TextButton(
                        onClick = onToggleMode
                    ) {
                        Text(
                            text = if (state.mode == AuthMode.LOGIN) {
                                "Ещё нет аккаунта? Зарегистрироваться"
                            } else {
                                "Уже существует аккаунт? Войти"
                            },
                            color = CoFlowColors.TextPrimary
                        )
                    }
                }
            }
        }
    }
}

// Связь экрана с существующей ViewModel.
@Composable
fun AuthRoute(
    onAuthSuccess: () -> Unit,
    viewModel: AuthViewModel = AuthViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AuthScreen(
        state = state,
        onContactChange = viewModel::onContactChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmChange = viewModel::onConfirmChange,
        onToggleMode = viewModel::onToggleMode,
        onSubmit = viewModel::onSubmit
    )
}

// Предпросмотр авторизации.
@Preview(
    showBackground = true,
    widthDp = 390,
    heightDp = 844,
    name = "Авторизация"
)
@Composable
private fun AuthLoginPreview() {
    MaterialTheme {
        AuthScreen(
            state = AuthUiState(),
            onContactChange = {},
            onPasswordChange = {},
            onConfirmChange = {},
            onToggleMode = {},
            onSubmit = {}
        )
    }
}

// Предпросмотр регистрации.
@Preview(
    showBackground = true,
    widthDp = 390,
    heightDp = 844,
    name = "Регистрация"
)
@Composable
private fun AuthRegisterPreview() {
    MaterialTheme {
        AuthScreen(
            state = AuthUiState(mode = AuthMode.REGISTER),
            onContactChange = {},
            onPasswordChange = {},
            onConfirmChange = {},
            onToggleMode = {},
            onSubmit = {}
        )
    }
}

// Предпросмотр ошибок валидации.
@Preview(
    showBackground = true,
    widthDp = 390,
    heightDp = 844,
    name = "Ошибки авторизации"
)
@Composable
private fun AuthErrorPreview() {
    MaterialTheme {
        AuthScreen(
            state = AuthUiState(
                contact = "liza@mail",
                password = "123",
                contactError = "Проверьте формат почты или телефона",
                passwordError = "Минимум 8 символов"
            ),
            onContactChange = {},
            onPasswordChange = {},
            onConfirmChange = {},
            onToggleMode = {},
            onSubmit = {}
        )
    }
}
