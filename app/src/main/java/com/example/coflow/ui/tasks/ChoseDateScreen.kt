package com.example.coflow.ui.tasks

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coflow.ui.theme.CoFlowColors
import com.example.coflow.ui.theme.CoFlowGlassStyles
import com.example.coflow.ui.theme.coFlowTasksBackgroundBrush
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.tooling.preview.Preview
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ChoseDateScreen(
    onBackClick: () -> Unit,
    onConfirmSelection: (String) -> Unit
) {
    // Инициализируем состояние как на макете (Январь 2027, выбрано 7-е число)
    var currentMonth by rememberSaveable { mutableStateOf(YearMonth.of(2027, 1)) }
    var selectedDate by rememberSaveable { mutableStateOf(LocalDate.of(2027, 1, 7)) }

    val focusManager = LocalFocusManager.current

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
                    text = "Дата окончания",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = CoFlowColors.TextPrimary
                )
            }

            // Карточка календаря
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = CoFlowGlassStyles.Shadow,
                        spotColor = CoFlowGlassStyles.Shadow
                    )
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White.copy(alpha = 0.85f))
                    .padding(16.dp)
            ) {
                Column {
                    // Шапка календаря (Месяц, Год, Стрелки)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = currentMonth.format(
                                DateTimeFormatter.ofPattern("LLLL yyyy", Locale("ru"))
                            ).replaceFirstChar { it.titlecase(Locale("ru")) },
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CoFlowColors.TextPrimary
                        )

                        Row {
                            // Стрелка влево
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .clickable { currentMonth = currentMonth.minusMonths(1) },
                                contentAlignment = Alignment.Center
                            ) {
                                CreateTaskIcon(
                                    name = "chevron_left",
                                    modifier = Modifier.size(16.dp),
                                    color = CoFlowColors.Blue
                                )
                            }
                            Spacer(Modifier.width(8.dp))
                            // Стрелка вправо
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .clickable { currentMonth = currentMonth.plusMonths(1) },
                                contentAlignment = Alignment.Center
                            ) {
                                CreateTaskIcon(
                                    name = "chevron",
                                    modifier = Modifier.size(16.dp),
                                    color = CoFlowColors.Blue
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Дни недели
                    val daysOfWeek = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        daysOfWeek.forEach { day ->
                            Text(
                                text = day,
                                modifier = Modifier.width(36.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = CoFlowColors.TextSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    // Сетка дней
                    CalendarGrid(
                        currentMonth = currentMonth,
                        selectedDate = selectedDate,
                        onDateSelected = { date ->
                            if (date.month == currentMonth.month) {
                                selectedDate = date
                            }
                        }
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            // Кнопка "Готово"
            Button(
                onClick = {
                    focusManager.clearFocus()
                    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
                    onConfirmSelection(selectedDate.format(formatter))
                },
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CoFlowColors.Blue,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 1.dp
                )
            ) {
                Text(
                    text = "Готово",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun CalendarGrid(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val firstDayOfMonth = currentMonth.atDay(1)
    // Получаем день недели первого дня (1 = Пн, 7 = Вс). Вычитаем 1, чтобы получить смещение от 0 до 6.
    val offset = firstDayOfMonth.dayOfWeek.value - 1
    val daysInMonth = currentMonth.lengthOfMonth()

    val prevMonth = currentMonth.minusMonths(1)
    val daysInPrevMonth = prevMonth.lengthOfMonth()

    val totalCells = 42 // 6 недель по 7 дней

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        for (week in 0 until 6) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (dayOfWeek in 0 until 7) {
                    val cellIndex = week * 7 + dayOfWeek

                    val (dayNumber, date, isCurrentMonth) = when {
                        cellIndex < offset -> {
                            // Дни предыдущего месяца
                            val day = daysInPrevMonth - offset + cellIndex + 1
                            Triple(day, prevMonth.atDay(day), false)
                        }
                        cellIndex >= offset + daysInMonth -> {
                            // Дни следующего месяца
                            val day = cellIndex - (offset + daysInMonth) + 1
                            Triple(day, currentMonth.plusMonths(1).atDay(day), false)
                        }
                        else -> {
                            // Дни текущего месяца
                            val day = cellIndex - offset + 1
                            Triple(day, currentMonth.atDay(day), true)
                        }
                    }

                    val isSelected = isCurrentMonth && date == selectedDate

                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(40.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) CoFlowColors.Blue else Color.Transparent)
                            .clickable(enabled = isCurrentMonth) {
                                if (isCurrentMonth) {
                                    onDateSelected(date)
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = dayNumber.toString(),
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = when {
                                isSelected -> Color.White
                                !isCurrentMonth -> CoFlowColors.TextSecondary.copy(alpha = 0.4f)
                                else -> CoFlowColors.TextPrimary
                            }
                        )
                    }
                }
            }
        }
    }
}

// Дополненная функция иконок (добавлены chevron_left)
@Composable
private fun CreateTaskIcon(
    name: String,
    modifier: Modifier = Modifier,
    color: Color = CoFlowColors.TextPrimary
) {
    val description = when (name) {
        "back" -> "Назад"
        "chevron_left" -> "Предыдущий месяц"
        "chevron" -> "Следующий месяц"
        else -> null
    }

    Canvas(
        modifier = modifier.then(
            if (description != null) {
                Modifier.semantics { contentDescription = description }
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

            "chevron" -> {
                val path = Path().apply {
                    moveTo(9f * s, 5f * s)
                    lineTo(16f * s, 12f * s)
                    lineTo(9f * s, 19f * s)
                }
                drawPath(path, color, style = stroke)
            }

            "chevron_left" -> {
                val path = Path().apply {
                    moveTo(15f * s, 5f * s)
                    lineTo(8f * s, 12f * s)
                    lineTo(15f * s, 19f * s)
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

            "folder" -> {
                val path = Path().apply {
                    moveTo(3f * s, 6f * s)
                    lineTo(9f * s, 6f * s)
                    lineTo(11f * s, 8f * s)
                    lineTo(21f * s, 8f * s)
                    lineTo(21f * s, 19f * s)
                    lineTo(3f * s, 19f * s)
                    close()
                }
                drawPath(path, color, style = stroke)
            }

            "calendar" -> {
                drawRoundRect(
                    color = color,
                    topLeft = point(3f, 5f),
                    size = androidx.compose.ui.geometry.Size(18f * s, 16f * s),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(2f * s),
                    style = stroke
                )
                drawLine(color, point(3f, 10f), point(21f, 10f), strokeWidth)
                drawLine(color, point(8f, 3f), point(8f, 7f), strokeWidth)
                drawLine(color, point(16f, 3f), point(16f, 7f), strokeWidth)
                for (x in listOf(8f, 12f, 16f)) {
                    for (y in listOf(14f, 18f)) {
                        drawCircle(
                            color = color,
                            radius = 0.8f * s,
                            center = point(x, y)
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Дата окончания",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun ChoseDateScreenPreview() {
    MaterialTheme {
        ChoseDateScreen(
            onBackClick = {},
            onConfirmSelection = {}
        )
    }
}