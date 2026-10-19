
package com.example.coflow.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.res.stringResource
import com.example.coflow.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coflow.ui.theme.CoFlowColors
import com.example.coflow.ui.theme.CoFlowShapes
import com.example.coflow.ui.theme.CoFlowSpacing
import com.example.coflow.ui.theme.coFlowAuthFieldColors
import com.example.coflow.ui.theme.coFlowBackgroundBrush

// Режим формы: авторизация или регистрация.
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
    // Общий градиент экрана берём из CoFlowStylesB.kt.
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(coFlowBackgroundBrush()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = CoFlowSpacing.ScreenHorizontal,
                    vertical = 20.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Полупрозрачная карточка: цвет и скругление заданы в общих стилях.
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = CoFlowShapes.Card,
                color = CoFlowColors.CardBackground,
                border = BorderStroke(
                    1.dp,
                    CoFlowColors.BlueBorder.copy(alpha = 0.75f)
                ),
                shadowElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = 18.dp,
                        vertical = 20.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Две отдельные строки исключают вывод символов "\\n".
                    Text(
                        text = stringResource(R.string.auth_welcome_title),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = CoFlowColors.TextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "в CoFlow",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = CoFlowColors.TextPrimary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    // Подзаголовок зависит от режима формы.
                    Text(
                        text = stringResource(
                            if (state.mode == AuthMode.LOGIN) R.string.auth_subtitle_login
                            else R.string.auth_subtitle_register
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = CoFlowColors.TextPrimary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Поле почты
                    OutlinedTextField(
                        value = state.contact,
                        onValueChange = onContactChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(stringResource(R.string.auth_label_contact))
                        },
                        placeholder = {
                            Text(stringResource(R.string.auth_placeholder_contact))
                        },
                        singleLine = true,
                        isError = state.contactError != null,
                        supportingText = state.contactError?.let { error ->
                            {
                                Text(text = error)
                            }
                        },
                        shape = CoFlowShapes.Input,
                        colors = coFlowAuthFieldColors()
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    // Поле пароля с маскировкой введённых символов.
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = onPasswordChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 0.dp),
                        label = {
                            Text(stringResource(R.string.auth_label_password))
                        },
                        placeholder = {
                            Text(stringResource(R.string.auth_placeholder_password))
                        },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        isError = state.passwordError != null,
                        supportingText = {
                            state.passwordError?.let { Text(it) }
                        },
                        shape = CoFlowShapes.Input,
                        colors = coFlowAuthFieldColors()
                    )

                    // Подтверждение пароля показываем только при регистрации.
                    if (state.mode == AuthMode.REGISTER) {
                        Spacer(modifier = Modifier.height(5.dp))

                        OutlinedTextField(
                            value = state.confirmPassword,
                            onValueChange = onConfirmChange,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 0.dp),
                            label = {
                                Text(stringResource(R.string.auth_label_confirm_password))
                            },
                            placeholder = {
                                Text(stringResource(R.string.auth_placeholder_confirm_password))
                            },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            isError = state.confirmError != null,
                            supportingText = state.confirmError?.let { error ->
                                {
                                    Text(text = error)
                                }
                            },
                            shape = CoFlowShapes.Input,
                            colors = coFlowAuthFieldColors()
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Основная кнопка использует общий синий цвет и форму.
                    Button(
                        onClick = onSubmit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = CoFlowShapes.Button,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CoFlowColors.Blue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = stringResource(
                                if (state.mode == AuthMode.LOGIN) R.string.auth_button_login
                                else R.string.auth_button_register
                            ),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    // Переключатель режима; существующие проверки не удалены.
                    TextButton(onClick = onToggleMode) {
                        Text(
                            text = stringResource(
                                if (state.mode == AuthMode.LOGIN) R.string.auth_toggle_to_register
                                else R.string.auth_toggle_to_login
                            ),
                            color = CoFlowColors.TextPrimary
                        )
                    }
                }
            }
        }
    }
}

// Compose создаёт и сохраняет ViewModel в пределах текущего владельца экрана.
@Composable
fun AuthRoute(
    onAuthSuccess: () -> Unit,
    viewModel: AuthViewModel = viewModel(),
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

// Preview: пустая форма авторизации.
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
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

// Preview: ошибки авторизации.
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AuthLoginErrorPreview() {
    MaterialTheme {
        AuthScreen(
            state = AuthUiState(
                contact = "liza@mail",
                password = "123",
                contactError = "Проверьте формат почты",
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

// Preview: пустая форма регистрации.
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
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

// Preview: несовпадающие пароли при регистрации.
@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AuthRegisterMismatchPreview() {
    MaterialTheme {
        AuthScreen(
            state = AuthUiState(
                mode = AuthMode.REGISTER,
                contact = "liza@mail.ru",
                password = "parol123",
                confirmPassword = "parol124",
                confirmError = "Пароли не совпадают"
            ),
            onContactChange = {},
            onPasswordChange = {},
            onConfirmChange = {},
            onToggleMode = {},
            onSubmit = {}
        )
    }
}
