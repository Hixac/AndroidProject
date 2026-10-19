package com.example.coflow.ui.tasks

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coflow.ui.theme.CoFlowColors
import com.example.coflow.ui.theme.CoFlowGlassStyles
import com.example.coflow.ui.theme.coFlowTasksBackgroundBrush
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview

// Модель данных для исполнителя
data class Assignee(
    val id: String,
    val name: String,
    val email: String,
    val initials: String,
    val avatarColor: Color
)

@Composable
fun ChoseAssigneeScreen(
    onBackClick: () -> Unit,
    onConfirmSelection: (List<Assignee>) -> Unit
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }

    // Тестовые данные (цвета подобраны по макету)
    val allAssignees = remember {
        listOf(
            Assignee("1", "Анна Смирнова", "anna.s@example.com", "АС", Color(0xFF6B9AF3)),
            Assignee("2", "Михаил Орлов", "m.orlov@example.com", "МО", Color(0xFFA3B8CC)),
            Assignee("3", "Елена Волкова", "elena.v@example.com", "ЕВ", Color(0xFF86D3B5)),
            Assignee("4", "Дмитрий Соколов", "d.sokolov@example.com", "ДС", Color(0xFF8BB8F8)),
            Assignee("5", "Мария Козлова", "maria.k@example.com", "МК", Color(0xFFAFA3F8))
        )
    }

    // По умолчанию выбран первый (как на макете)
    var selectedIds by remember { mutableStateOf(setOf("1")) }

    val filteredAssignees = allAssignees.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.email.contains(searchQuery, ignoreCase = true)
    }

    val focusManager = LocalFocusManager.current

    // Функция для склонения слова "исполнитель"
    fun getSelectedText(count: Int): String {
        if (count == 0) return "Никто не выбран"
        val lastDigit = count % 10
        val lastTwoDigits = count % 100
        val word = if (lastTwoDigits in 11..19) {
            "исполнителей"
        } else if (lastDigit == 1) {
            "исполнитель"
        } else if (lastDigit in 2..4) {
            "исполнителя"
        } else {
            "исполнителей"
        }
        return "Выбран $count $word"
    }

    BackHandler(onBack = onBackClick)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(coFlowTasksBackgroundBrush())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .imePadding()
                .padding(horizontal = 20.dp)
        ) {
            // Заголовок и возврат
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 22.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable {
                            focusManager.clearFocus()
                            onBackClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    CreateTaskIcon(
                        name = "back",
                        modifier = Modifier.size(22.dp),
                        color = CoFlowColors.TextSecondary
                    )
                }

                Spacer(Modifier.width(4.dp))

                Text(
                    text = "Добавить исполнителя",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = CoFlowColors.TextPrimary
                )
            }

            // Поле поиска
            AssigneeSearchInput(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = "Имя или почта"
            )

            Spacer(Modifier.height(20.dp))

            // Заголовок списка
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Исполнители",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CoFlowColors.TextPrimary
                )
                Text(
                    text = "${allAssignees.size}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CoFlowColors.TextSecondary
                )
            }

            // Список исполнителей
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredAssignees, key = { it.id }) { assignee ->
                    AssigneeItemRow(
                        assignee = assignee,
                        isSelected = selectedIds.contains(assignee.id),
                        onToggle = {
                            selectedIds = if (selectedIds.contains(assignee.id)) {
                                selectedIds - assignee.id
                            } else {
                                selectedIds + assignee.id
                            }
                        }
                    )
                }
            }

            // Нижняя часть с текстом и кнопкой
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 12.dp)
            ) {
                Text(
                    text = getSelectedText(selectedIds.size),
                    modifier = Modifier.padding(bottom = 12.dp, start = 4.dp),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = CoFlowColors.TextPrimary
                )

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        val selectedAssignees = allAssignees.filter { selectedIds.contains(it.id) }
                        onConfirmSelection(selectedAssignees)
                    },
                    enabled = selectedIds.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CoFlowColors.Blue,
                        contentColor = Color.White,
                        disabledContainerColor = CoFlowColors.Blue.copy(alpha = 0.45f),
                        disabledContentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 1.dp,
                        disabledElevation = 0.dp
                    )
                ) {
                    Text(
                        text = "Добавить исполнителей",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun AssigneeSearchInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(20.dp)
    val focusManager = LocalFocusManager.current

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 3.dp,
                shape = shape,
                ambientColor = CoFlowGlassStyles.Shadow,
                spotColor = CoFlowGlassStyles.Shadow
            )
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.72f),
                        Color(0xFFF5F8FF).copy(alpha = 0.58f)
                    )
                )
            )
            .border(
                width = 1.dp,
                color = CoFlowGlassStyles.BlueBorder,
                shape = shape
            ),
        textStyle = TextStyle(
            color = CoFlowColors.TextPrimary,
            fontSize = 14.sp,
            lineHeight = 20.sp
        ),
        cursorBrush = SolidColor(CoFlowColors.Blue),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { focusManager.clearFocus() }
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 52.dp)
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CreateTaskIcon(
                    name = "search",
                    modifier = Modifier.size(21.dp),
                    color = CoFlowColors.TextSecondary
                )

                Spacer(Modifier.width(10.dp))

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = CoFlowColors.TextSecondary,
                            fontSize = 14.sp
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}

@Composable
private fun AssigneeItemRow(
    assignee: Assignee,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    val backgroundColor = if (isSelected) {
        CoFlowColors.Blue.copy(alpha = 0.12f)
    } else {
        Color.White.copy(alpha = 0.65f)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(backgroundColor)
            .border(1.dp, CoFlowGlassStyles.BlueBorder, shape)
            .clickable { onToggle() }
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Аватарка
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(assignee.avatarColor.copy(alpha = 0.2f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = assignee.initials,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = assignee.avatarColor
            )
        }

        Spacer(Modifier.width(12.dp))

        // Имя и почта
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = assignee.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = CoFlowColors.TextPrimary
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = assignee.email,
                fontSize = 12.sp,
                color = CoFlowColors.TextSecondary
            )
        }

        // Чекбокс
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(if (isSelected) CoFlowColors.Blue else Color.White)
                .border(
                    width = 1.5.dp,
                    color = if (isSelected) CoFlowColors.Blue else CoFlowColors.TextSecondary.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(6.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                CreateTaskIcon(
                    name = "check",
                    modifier = Modifier.size(14.dp),
                    color = Color.White
                )
            }
        }
    }
}

// Дополненная функция иконок (добавлены search и check)
@Composable
private fun CreateTaskIcon(
    name: String,
    modifier: Modifier = Modifier,
    color: Color = CoFlowColors.TextPrimary
) {
    val description = when (name) {
        "back" -> "Назад"
        "search" -> "Поиск"
        "check" -> "Выбрано"
        else -> null
    }

    Canvas(
        modifier = modifier.then(
            if (description != null) {
                Modifier.semantics {
                    contentDescription = description
                }
            } else {
                Modifier
            }
        )
    ) {
        val s = size.minDimension / 24f
        val strokeWidth = 1.6f * s
        val stroke = Stroke(width = strokeWidth)

        fun point(x: Float, y: Float) = Offset(x * s, y * s)

        when (name) {
            "back" -> {
                val path = Path().apply {
                    moveTo(14f * s, 5f * s)
                    lineTo(7f * s, 12f * s)
                    lineTo(14f * s, 19f * s)
                }
                drawPath(path, color, style = stroke)
                drawLine(
                    color = color,
                    start = point(7f, 12f),
                    end = point(21f, 12f),
                    strokeWidth = strokeWidth
                )
            }

            "search" -> {
                drawCircle(
                    color = color,
                    radius = 6f * s,
                    center = point(10f, 10f),
                    style = stroke
                )
                drawLine(
                    color = color,
                    start = point(15f, 15f),
                    end = point(21f, 21f),
                    strokeWidth = strokeWidth
                )
            }

            "check" -> {
                val path = Path().apply {
                    moveTo(5f * s, 12f * s)
                    lineTo(10f * s, 17f * s)
                    lineTo(19f * s, 7f * s)
                }
                drawPath(path, color, style = Stroke(width = 2.5f * s, cap = androidx.compose.ui.graphics.StrokeCap.Round, join = androidx.compose.ui.graphics.StrokeJoin.Round))
            }

            // Остальные иконки из вашего кода (на случай переиспользования)
            "chevron" -> {
                val path = Path().apply {
                    moveTo(9f * s, 5f * s)
                    lineTo(16f * s, 12f * s)
                    lineTo(9f * s, 19f * s)
                }
                drawPath(path, color, style = stroke)
            }

            "person" -> {
                drawCircle(
                    color = color,
                    radius = 3.3f * s,
                    center = point(12f, 7f),
                    style = stroke
                )
                val path = Path().apply {
                    moveTo(5f * s, 21f * s)
                    lineTo(5f * s, 18f * s)
                    cubicTo(5f * s, 12f * s, 19f * s, 12f * s, 19f * s, 18f * s)
                    lineTo(19f * s, 21f * s)
                }
                drawPath(path, color, style = stroke)
            }
        }
    }
}

@Preview(
    name = "Выбор исполнителя",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun ChoseAssigneeScreenPreview() {
    MaterialTheme {
        ChoseAssigneeScreen (
            onBackClick = {},
            onConfirmSelection = {}
        )
    }
}