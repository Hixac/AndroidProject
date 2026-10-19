package com.example.coflow.ui.tasks

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coflow.ui.theme.CoFlowColors
import com.example.coflow.ui.theme.CoFlowGlassStyles
import com.example.coflow.ui.theme.coFlowTasksBackgroundBrush

// Модель данных для передачи результата создания задачи
data class CreateTaskFormData(
    val title: String,
    val description: String,
    val assignees: String,
    val project: String,
    val dueDate: String
)

@Composable
fun CreateTaskScreen(
    onBackClick: () -> Unit,
    onCreateTask: (CreateTaskFormData) -> Unit,
    onNavigateToAssignees: () -> Unit, // <-- Навигация к выбору исполнителей
    onNavigateToProject: () -> Unit,   // <-- Навигация к выбору проекта
    onNavigateToDate: () -> Unit       // <-- Навигация к выбору даты
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var assignees by rememberSaveable { mutableStateOf("") }
    var project by rememberSaveable { mutableStateOf("") }
    var dueDate by rememberSaveable { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val canCreate = title.isNotBlank()

    // Обработка системной кнопки "Назад"
    BackHandler(onBack = onBackClick)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(coFlowTasksBackgroundBrush())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                // safeDrawingPadding и imePadding корректно обрабатывают вырезы и клавиатуру
                .padding(horizontal = 20.dp)
        ) {
            // Заголовок и кнопка возврата.
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
                    text = "Создание задачи",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = CoFlowColors.TextPrimary
                )
            }

            // Прокручивается только содержимое формы.
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CreateTaskInput(
                    label = "Название",
                    value = title,
                    onValueChange = { title = it },
                    placeholder = "Введите название задачи"
                )

                CreateTaskInput(
                    label = "Описание",
                    value = description,
                    onValueChange = { description = it.take(250) },
                    placeholder = "Введите описание задачи",
                    multiline = true,
                    maxLength = 250
                )

                CreateTaskInput(
                    label = "Исполнители",
                    value = assignees,
                    onValueChange = { assignees = it },
                    placeholder = "Выберите исполнителей",
                    icon = "person",
                    showArrow = true,
                    onClick = onNavigateToAssignees // <-- Клик открывает экран выбора
                )

                CreateTaskInput(
                    label = "Проект",
                    value = project,
                    onValueChange = { project = it },
                    placeholder = "Выберите проект",
                    icon = "folder",
                    showArrow = true,
                    onClick = onNavigateToProject // <-- Клик открывает экран выбора
                )

                CreateTaskInput(
                    label = "Дата окончания",
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    placeholder = "Выберите дату",
                    icon = "calendar",
                    showArrow = true,
                    onClick = onNavigateToDate, // <-- Клик открывает экран выбора
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    )
                )

                Spacer(Modifier.height(16.dp))
            }

            // Кнопка занимает собственное место и не перекрывает форму.
            Button(
                onClick = {
                    focusManager.clearFocus()
                    onCreateTask(
                        CreateTaskFormData(
                            title = title.trim(),
                            description = description.trim(),
                            assignees = assignees.trim(),
                            project = project.trim(),
                            dueDate = dueDate.trim()
                        )
                    )
                },
                enabled = canCreate,
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 12.dp)
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
                    text = "Создать задачу",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun CreateTaskInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    multiline: Boolean = false,
    maxLength: Int? = null,
    icon: String? = null,
    showArrow: Boolean = false,
    onClick: (() -> Unit)? = null, // <-- Добавлен параметр для обработки клика по всей строке
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Sentences,
        imeAction = ImeAction.Next
    ),
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val shape = RoundedCornerShape(20.dp)
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            modifier = Modifier.padding(start = 12.dp, bottom = 4.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = CoFlowColors.TextPrimary
        )

        BasicTextField(
            value = value,
            onValueChange = {
                onValueChange(if (maxLength != null) it.take(maxLength) else it)
            },
            modifier = Modifier
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
                .border(width = 1.dp, color = CoFlowGlassStyles.BlueBorder, shape = shape),
            textStyle = TextStyle(
                color = CoFlowColors.TextPrimary,
                fontSize = 14.sp,
                lineHeight = 20.sp
            ),
            cursorBrush = SolidColor(CoFlowColors.Blue),
            singleLine = !multiline,
            keyboardOptions = if (multiline) {
                KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Default
                )
            } else {
                keyboardOptions
            },
            keyboardActions = if (!multiline && keyboardOptions.imeAction == ImeAction.Next) {
                KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) }
                )
            } else {
                keyboardActions
            },
            decorationBox = { innerTextField ->
                if (multiline) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 88.dp),
                            contentAlignment = Alignment.TopStart
                        ) {
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    color = CoFlowColors.TextSecondary,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp
                                )
                            }
                            innerTextField()
                        }

                        if (maxLength != null) {
                            Text(
                                text = "${value.length}/$maxLength",
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .padding(top = 6.dp),
                                fontSize = 11.sp,
                                color = CoFlowColors.TextSecondary
                            )
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 52.dp)
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                            // <-- КЛЮЧЕВОЕ ИЗМЕНЕНИЕ: делаем всю строку кликабельной, если передан onClick
                            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (icon != null) {
                            CreateTaskIcon(
                                name = icon,
                                modifier = Modifier.size(21.dp),
                                color = CoFlowColors.TextPrimary
                            )
                            Spacer(Modifier.width(10.dp))
                        }

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

                        if (showArrow) {
                            Spacer(Modifier.width(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .background(Color.White.copy(alpha = 0.65f), CircleShape)
                                    .border(1.dp, CoFlowGlassStyles.BlueBorder, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                CreateTaskIcon(
                                    name = "chevron",
                                    modifier = Modifier.size(14.dp),
                                    color = CoFlowColors.Blue
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}

// Собственные иконки: не нужен material-icons-extended.
@Composable
private fun CreateTaskIcon(
    name: String,
    modifier: Modifier = Modifier,
    color: Color = CoFlowColors.TextPrimary
) {
    val description = when (name) {
        "back" -> "Назад"
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
                drawLine(color = color, start = point(7f, 12f), end = point(21f, 12f), strokeWidth = strokeWidth)
            }
            "chevron" -> {
                val path = Path().apply {
                    moveTo(9f * s, 5f * s)
                    lineTo(16f * s, 12f * s)
                    lineTo(9f * s, 19f * s)
                }
                drawPath(path, color, style = stroke)
            }
            "person" -> {
                drawCircle(color = color, radius = 3.3f * s, center = point(12f, 7f), style = stroke)
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
                    size = Size(18f * s, 16f * s),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(2f * s),
                    style = stroke
                )
                drawLine(color, point(3f, 10f), point(21f, 10f), strokeWidth)
                drawLine(color, point(8f, 3f), point(8f, 7f), strokeWidth)
                drawLine(color, point(16f, 3f), point(16f, 7f), strokeWidth)
                for (x in listOf(8f, 12f, 16f)) {
                    for (y in listOf(14f, 18f)) {
                        drawCircle(color = color, radius = 0.8f * s, center = point(x, y))
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Создание задачи",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun CreateTaskScreenPreview() {
    MaterialTheme {
        CreateTaskScreen(
            onBackClick = { println("Preview: Назад") },
            onCreateTask = { println("Preview: Создание задачи: $it") },
            onNavigateToAssignees = { println("Preview: Переход к выбору исполнителей") },
            onNavigateToProject = { println("Preview: Переход к выбору проекта") },
            onNavigateToDate = { println("Preview: Переход к выбору даты") }
        )
    }
}