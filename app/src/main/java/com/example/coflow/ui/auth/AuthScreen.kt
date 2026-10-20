package com.example.coflow.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coflow.R
import com.example.coflow.ui.components.* // <-- Групповой импорт шаблонов
import com.example.coflow.ui.theme.*      // <-- Групповой импорт темы

@Composable
fun AuthScreen(
    state: AuthUiState,
    onUsernameChange: (String) -> Unit,      // <-- НОВОЕ
    onContactChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmChange: (String) -> Unit,
    onToggleMode: () -> Unit,
    onSubmit: () -> Unit,
) {
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
                    vertical = CoFlowSpacing.Medium // 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Стеклянная карточка авторизации
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = CoFlowShapes.Card,
                color = CoFlowColors.CardBackground,
                border = BorderStroke(1.dp, CoFlowColors.BlueBorder.copy(alpha = 0.75f)),
                shadowElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = CoFlowSpacing.Medium,
                        vertical = CoFlowSpacing.Large // 24.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.auth_welcome_title),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = CoFlowColors.TextPrimary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(CoFlowSpacing.Small))

                    Text(
                        text = stringResource(
                            if (state.mode == AuthMode.LOGIN) R.string.auth_subtitle_login
                            else R.string.auth_subtitle_register
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = CoFlowColors.TextPrimary
                    )

                    Spacer(modifier = Modifier.height(CoFlowSpacing.Medium))

                    // --- ПОЛЕ: ИМЯ ПОЛЬЗОВАТЕЛЯ (Только для регистрации) ---
                    if (state.mode == AuthMode.REGISTER) {
                        OutlinedTextField(
                            value = state.username,
                            onValueChange = onUsernameChange,
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(stringResource(R.string.auth_label_username))},
                            placeholder = { Text(stringResource(R.string.auth_placeholder_username))},
                            singleLine = true,
                            isError = state.usernameError != null,
                            supportingText = state.usernameError?.let { { Text(it) } },
                            shape = CoFlowShapes.Input,
                            colors = coFlowAuthFieldColors()
                        )
                        Spacer(modifier = Modifier.height(CoFlowSpacing.FieldGap)) // 4.dp
                    }

                    // --- ПОЛЕ: ПОЧТА / ТЕЛЕФОН ---
                    OutlinedTextField(
                        value = state.contact,
                        onValueChange = onContactChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.auth_label_contact)) },
                        placeholder = { Text(stringResource(R.string.auth_placeholder_contact)) },
                        singleLine = true,
                        isError = state.contactError != null,
                        supportingText = state.contactError?.let { { Text(it) } },
                        shape = CoFlowShapes.Input,
                        colors = coFlowAuthFieldColors()
                    )

                    Spacer(modifier = Modifier.height(CoFlowSpacing.FieldGap))

                    // --- ПОЛЕ: ПАРОЛЬ ---
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = onPasswordChange,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(R.string.auth_label_password)) },
                        placeholder = { Text(stringResource(R.string.auth_placeholder_password)) },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        isError = state.passwordError != null,
                        supportingText = state.passwordError?.let { { Text(it) } },
                        shape = CoFlowShapes.Input,
                        colors = coFlowAuthFieldColors()
                    )

                    // --- ПОЛЕ: ПОДТВЕРЖДЕНИЕ ПАРОЛЯ (Только для регистрации) ---
                    if (state.mode == AuthMode.REGISTER) {
                        Spacer(modifier = Modifier.height(CoFlowSpacing.FieldGap))
                        OutlinedTextField(
                            value = state.confirmPassword,
                            onValueChange = onConfirmChange,
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(stringResource(R.string.auth_label_confirm_password)) },
                            placeholder = { Text(stringResource(R.string.auth_placeholder_confirm_password)) },
                            singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            isError = state.confirmError != null,
                            supportingText = state.confirmError?.let { { Text(stringResource((R.string.auth_error_not_equal_password))) } },
                            shape = CoFlowShapes.Input,
                            colors = coFlowAuthFieldColors()
                        )
                    }

                    Spacer(modifier = Modifier.height(CoFlowSpacing.Medium))

                    // --- КНОПКА: ИСПОЛЬЗУЕМ ШАБЛОН CoFlowPrimaryButton ---
                    CoFlowPrimaryButton(
                        text = stringResource(
                            if (state.mode == AuthMode.LOGIN) R.string.auth_button_login
                            else R.string.auth_button_register
                        ),
                        onClick = onSubmit,
                        modifier = Modifier.fillMaxWidth(),
                        // Переопределяем высоту, если нужно, или оставляем дефолтную (52.dp)
                        // height = 48.dp, 
                        enabled = !state.isSubmitting
                    )

                    Spacer(modifier = Modifier.height(CoFlowSpacing.Small))

                    // --- ПЕРЕКЛЮЧАТЕЛЬ РЕЖИМА ---
                    TextButton(onClick = onToggleMode) {
                        Text(
                            text = stringResource(
                                if (state.mode == AuthMode.LOGIN) R.string.auth_toggle_to_register
                                else R.string.auth_toggle_to_login
                            ),
                            color = CoFlowColors.TextPrimary,
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AuthRoute(
    onAuthSuccess: () -> Unit,
    viewModel: AuthViewModel = viewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    AuthScreen(
        state = state,
        onUsernameChange = viewModel::onUsernameChange,      // <-- НОВОЕ
        onContactChange = viewModel::onContactChange,
        onPasswordChange = viewModel::onPasswordChange,
        onConfirmChange = viewModel::onConfirmChange,
        onToggleMode = viewModel::onToggleMode,
        onSubmit = viewModel::onSubmit
    )
}

// ============================================================================
// ПРЕВЬЮ (Previews)
// ============================================================================

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AuthLoginPreview() {
    CoFlowTheme {
        AuthScreen(
            state = AuthUiState(),
            onUsernameChange = {},
            onContactChange = {},
            onPasswordChange = {},
            onConfirmChange = {},
            onToggleMode = {},
            onSubmit = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AuthLoginErrorPreview() {
    CoFlowTheme {
        AuthScreen(
            state = AuthUiState(
                contact = "liza@mail",
                password = "123",
                contactError = "Проверьте формат почты",
                passwordError = "Минимум 8 символов"
            ),
            onUsernameChange = {},
            onContactChange = {},
            onPasswordChange = {},
            onConfirmChange = {},
            onToggleMode = {},
            onSubmit = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AuthRegisterPreview() {
    CoFlowTheme {
        AuthScreen(
            state = AuthUiState(
                mode = AuthMode.REGISTER,
                username = "Лиза" // <-- НОВОЕ
            ),
            onUsernameChange = {},
            onContactChange = {},
            onPasswordChange = {},
            onConfirmChange = {},
            onToggleMode = {},
            onSubmit = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun AuthRegisterMismatchPreview() {
    CoFlowTheme {
        AuthScreen(
            state = AuthUiState(
                mode = AuthMode.REGISTER,
                username = "Лиза",                     // <-- НОВОЕ
                contact = "liza@mail.ru",
                password = "parol123",
                confirmPassword = "parol124",
                confirmError = "Пароли не совпадают"
            ),
            onUsernameChange = {},
            onContactChange = {},
            onPasswordChange = {},
            onConfirmChange = {},
            onToggleMode = {},
            onSubmit = {}
        )
    }
}