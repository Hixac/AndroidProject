
package com.example.coflow.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// =====================================================
// ЦВЕТА — единая палитра приложения CoFlow
// =====================================================
object CoFlowColors {
    // Основной синий: кнопки, курсор, активные элементы
    val Blue = Color(0xFF245BDB)
    val BlueLight = Color(0xFF8DB4FF)

    // Неактивная рамка полей ввода
    val BlueBorder = Color(0xFF9BBEFF)

    // Основной и второстепенный цвета текста
    val TextPrimary = Color(0xFF10213B)
    val TextSecondary = Color(0xFF64748B)

    val White = Color(0xFFFFFFFF)

    // Цвет ошибок валидации
    val Error = Color(0xFFB42318)

    // Остановка градиента сверху вниз:
    // светлый верх → голубая середина → синий низ
    val BackgroundTop = Color(0xFFF9FBFF)
    val BackgroundLight = Color(0xFFE4EDFF)
    val BackgroundMiddle = Color(0xFFB9D1FF)
    val BackgroundBottom = Color(0xFF5791F8)

    // Полупрозрачный фон стеклянной карточки авторизации
    val CardBackground = Color(0x66DCE9FF)
}

// =====================================================
// ФОРМЫ — скругления компонентов
// =====================================================
object CoFlowShapes {
    // Основная карточка формы авторизации/регистрации
    val Card = RoundedCornerShape(24.dp)

    // Поля ввода почты и пароля
    val Input = RoundedCornerShape(12.dp)

    // Основная кнопка входа или регистрации
    val Button = RoundedCornerShape(14.dp)
}

// =====================================================
// ОТСТУПЫ — единые расстояния между элементами
// =====================================================
object CoFlowSpacing {
    val Small = 8.dp
    val Medium = 16.dp
    val Large = 24.dp

    // Боковые отступы экрана от краёв телефона
    val ScreenHorizontal = 20.dp

    // Расстояние от подзаголовка до первого поля
    val FieldStart = 16.dp

    // Расстояние между полями ввода
    val FieldGap = 4.dp
}

// =====================================================
// ФОН — вертикальный градиент экрана
// Применяется к фону AuthScreen
// =====================================================
fun coFlowBackgroundBrush(): Brush {
    return Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to CoFlowColors.BackgroundTop,
            0.22f to CoFlowColors.BackgroundLight,
            0.58f to CoFlowColors.BackgroundMiddle,
            1.0f to CoFlowColors.BackgroundBottom
        )
    )
}

// =====================================================
// ПОЛЯ ВВОДА — единое оформление OutlinedTextField
// Применяется к почте, паролю и повтору пароля
// =====================================================
@Composable
fun coFlowAuthFieldColors() = OutlinedTextFieldDefaults.colors(
    // Цвет вводимого текста
    focusedTextColor = CoFlowColors.TextPrimary,
    unfocusedTextColor = CoFlowColors.TextPrimary,

    // Полупрозрачный фон поля
    focusedContainerColor = CoFlowColors.White.copy(alpha = 0.35f),
    unfocusedContainerColor = CoFlowColors.White.copy(alpha = 0.28f),

    // Рамка: яркая при фокусе, светло-голубая без фокуса
    focusedBorderColor = CoFlowColors.Blue,
    unfocusedBorderColor = CoFlowColors.BlueBorder,

    // Подпись поля
    focusedLabelColor = CoFlowColors.Blue,
    unfocusedLabelColor = CoFlowColors.TextPrimary,

    // Подсказка внутри поля
    focusedPlaceholderColor = CoFlowColors.TextSecondary,
    unfocusedPlaceholderColor = CoFlowColors.TextSecondary,

    // Цвет курсора
    cursorColor = CoFlowColors.Blue,

    // Оформление ошибок
    errorBorderColor = CoFlowColors.Error,
    errorLabelColor = CoFlowColors.Error,
    errorSupportingTextColor = CoFlowColors.Error
)
