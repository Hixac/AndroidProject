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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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

@Composable
fun ChoseProjectScreen(
    onBackClick: () -> Unit,
    onConfirmSelection: (String) -> Unit
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedProject by rememberSaveable { mutableStateOf("Проект 1") }

    // Тестовые данные, в реальном приложении приходят из ViewModel
    val projects = remember {
        listOf("Проект 1", "Проект 2", "Проект 3", "Проект 4", "Проект 4")
    }

    val filteredProjects = projects.filter {
        it.contains(searchQuery, ignoreCase = true)
    }

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
                    text = "Выбор проекта",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = CoFlowColors.TextPrimary
                )
            }

            // Поле поиска
            ProjectSearchInput(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = "Название проекта"
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
                    text = "Проекты",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CoFlowColors.TextPrimary
                )
                Text(
                    text = "${filteredProjects.size}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = CoFlowColors.TextSecondary
                )
            }

            // Список проектов
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredProjects) { project ->
                    ProjectItemRow(
                        projectName = project,
                        isSelected = project == selectedProject,
                        onClick = { selectedProject = project }
                    )
                }
            }

            // Кнопка "Готово"
            Button(
                onClick = {
                    focusManager.clearFocus()
                    onConfirmSelection(selectedProject)
                },
                enabled = selectedProject.isNotBlank(),
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
                    text = "Готово",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ProjectSearchInput(
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
                // Иконка поиска
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
private fun ProjectItemRow(
    projectName: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)
    val backgroundColor = if (isSelected) {
        CoFlowColors.Blue.copy(alpha = 0.12f)
    } else {
        Color.White.copy(alpha = 0.65f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(shape)
            .background(backgroundColor)
            .border(1.dp, CoFlowGlassStyles.BlueBorder, shape)
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = projectName,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = CoFlowColors.TextPrimary
        )
    }
}

// Дополненная функция иконок (добавлена иконка поиска)
@Composable
private fun CreateTaskIcon(
    name: String,
    modifier: Modifier = Modifier,
    color: Color = CoFlowColors.TextPrimary
) {
    val description = when (name) {
        "back" -> "Назад"
        "search" -> "Поиск"
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
                    cubicTo(
                        5f * s, 12f * s,
                        19f * s, 12f * s,
                        19f * s, 18f * s
                    )
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
                    cornerRadius = CornerRadius(2f * s),
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
    name = "Выбор проекта",
    showBackground = true,
    widthDp = 390,
    heightDp = 844
)
@Composable
private fun ChoseProjectScreenPreview() {
    MaterialTheme {
        ChoseProjectScreen(
            onBackClick = {},
            onConfirmSelection = {}
        )
    }
}