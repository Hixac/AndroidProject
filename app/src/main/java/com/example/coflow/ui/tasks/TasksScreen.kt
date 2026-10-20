package com.example.coflow.ui.tasks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coflow.data.Task
import com.example.coflow.models.TaskStatus
import com.example.coflow.data.TasksTestData
import com.example.coflow.ui.theme.CoFlowColors
import com.example.coflow.ui.theme.CoFlowGlassStyles
import com.example.coflow.ui.theme.CoFlowSpacing
import com.example.coflow.ui.theme.coFlowTasksBackgroundBrush
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private enum class TaskGrouping {
    STATUS,
    DEADLINE
}

private data class TaskGroup(
    val id: String,
    val title: String,
    val tasks: List<Task>
)

@Composable
fun TasksScreen() {
    var grouping by remember {
        mutableStateOf(TaskGrouping.STATUS)
    }
    var selectedFilter by remember {
        mutableStateOf("Без фильтра")
    }
    var collapsedGroups by remember {
        mutableStateOf(setOf("IN_PROGRESS"))
    }

    var dialogTitle by remember { mutableStateOf<String?>(null) }
    var dialogMessage by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()
    var previousScroll by remember { mutableStateOf(0) }
    var bottomVisible by remember { mutableStateOf(true) }

    // Прокрутка вниз скрывает нижние элементы, вверх — возвращает.
    LaunchedEffect(scrollState.value) {
        val current = scrollState.value

        if (current > previousScroll + 2) {
            bottomVisible = false
        } else if (current < previousScroll - 2) {
            bottomVisible = true
        }

        previousScroll = current
    }

    val today = LocalDate.now()

    val filteredTasks = TasksTestData.tasks.filter { task ->
        when (selectedFilter) {
            "Мои" -> task.isMine
            "Проект 1", "Проект 2", "Проект 3" ->
                task.projectName == selectedFilter
            else -> true
        }
    }

    val groups = when (grouping) {
        TaskGrouping.STATUS -> listOf(
            TaskGroup(
                "TODO",
                "К исполнению",
                filteredTasks.filter { it.status == TaskStatus.TODO }
            ),
            TaskGroup(
                "IN_PROGRESS",
                "В процессе",
                filteredTasks.filter {
                    it.status == TaskStatus.IN_PROGRESS
                }
            ),
            TaskGroup(
                "DONE",
                "Сделано",
                filteredTasks.filter { it.status == TaskStatus.DONE }
            )
        )

        TaskGrouping.DEADLINE -> listOf(
            TaskGroup(
                "TODAY",
                "Сегодня",
                filteredTasks.filter { it.dueDate == today }
                    .sortedBy { it.dueDate }
            ),
            TaskGroup(
                "THIS_WEEK",
                "Эта неделя",
                filteredTasks.filter {
                    it.dueDate.isAfter(today) &&
                            !it.dueDate.isAfter(today.plusDays(6))
                }.sortedBy { it.dueDate }
            ),
            TaskGroup(
                "NEXT_WEEK",
                "Следующая неделя",
                filteredTasks.filter {
                    it.dueDate.isAfter(today.plusDays(6)) &&
                            !it.dueDate.isAfter(today.plusDays(13))
                }.sortedBy { it.dueDate }
            )
        )
    }

    val completedCount = filteredTasks.count {
        it.status == TaskStatus.DONE
    }
    val activeCount = filteredTasks.size - completedCount

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(coFlowTasksBackgroundBrush())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 38.dp,
                    start = CoFlowSpacing.ScreenHorizontal,
                    end = CoFlowSpacing.ScreenHorizontal
                )
        ) {
            // Заголовок и уведомления.
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Мои задачи",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = CoFlowColors.TextPrimary
                    )

                    Spacer(Modifier.height(3.dp))

                    Text(
                        text = "$activeCount активных · " +
                                "$completedCount завершены",
                        fontSize = 13.sp,
                        color = CoFlowColors.TextSecondary
                    )
                }

                Box {
                    GlassIconButton(
                        icon = "bell",
                        onClick = {
                            dialogTitle = "Уведомления"
                            dialogMessage =
                                "Интерфейс уведомлений ещё не реализован."
                        }
                    )

                    // Индикатор новых уведомлений.
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 3.dp, end = 3.dp)
                            .size(6.dp)
                            .background(CoFlowColors.Blue, CircleShape)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Прокручивается только список задач.
            Column(
                modifier = Modifier
                    .weight(1f)
                    // Leave a clear gap before the controls and clip the scroll viewport.
                    .padding(bottom = 8.dp)
                    .clipToBounds()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                groups.forEach { group ->
                    TaskGroupCard(
                        group = group,
                        expanded = group.id !in collapsedGroups,
                        onToggle = {
                            collapsedGroups =
                                if (group.id in collapsedGroups) {
                                    collapsedGroups - group.id
                                } else {
                                    collapsedGroups + group.id
                                }
                        }
                    )
                }

                // Reserve a little breathing room before the fixed controls.
                Spacer(Modifier.height(18.dp))
            }

            // Нижние элементы занимают собственное место и не накрывают список.
            AnimatedVisibility(visible = bottomVisible) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = WindowInsets.navigationBars
                                .asPaddingValues()
                                .calculateBottomPadding() + 2.dp
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .widthIn(max = 330.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SortFilterPanel(
                            modifier = Modifier.weight(1f),
                            grouping = grouping,
                            onGroupingChange = { newGrouping ->
                                grouping = newGrouping
                                collapsedGroups = emptySet()
                            },
                            selectedFilter = selectedFilter,
                            onFilterChange = { selectedFilter = it }
                        )

                        Spacer(Modifier.width(16.dp))

                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .shadow(
                                    elevation = 3.dp,
                                    shape = CircleShape,
                                    ambientColor = CoFlowGlassStyles.Shadow,
                                    spotColor = CoFlowGlassStyles.Shadow
                                )
                                .clip(CircleShape)
                                .background(CoFlowColors.Blue)
                                .clickable {
                                    dialogTitle = "Создание задачи"
                                    dialogMessage =
                                        "Интерфейс создания задачи ещё не реализован."
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+",
                                fontSize = 27.sp,
                                fontWeight = FontWeight.Light,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(Modifier.height(7.dp))

                    BottomNavigationBar(
                        onProjectsClick = {
                            dialogTitle = "Проекты"
                            dialogMessage =
                                "Интерфейс проектов ещё не реализован."
                        },
                        onProfileClick = {
                            dialogTitle = "Профиль"
                            dialogMessage =
                                "Интерфейс профиля ещё не реализован."
                        }
                    )
                }
            }
        }

        if (dialogTitle != null) {
            AlertDialog(
                onDismissRequest = { dialogTitle = null },
                title = {
                    Text(
                        text = dialogTitle!!,
                        color = CoFlowColors.TextPrimary
                    )
                },
                text = {
                    Text(
                        text = dialogMessage,
                        color = CoFlowColors.TextSecondary
                    )
                },
                confirmButton = {
                    TextButton(onClick = { dialogTitle = null }) {
                        Text("Понятно", color = CoFlowColors.Blue)
                    }
                },
                containerColor = Color(0xFFF4F7FF)
            )
        }
    }
}

@Composable
private fun TaskGroupCard(
    group: TaskGroup,
    expanded: Boolean,
    onToggle: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = CoFlowGlassStyles.CardShape,
                ambientColor = CoFlowGlassStyles.Shadow,
                spotColor = CoFlowGlassStyles.Shadow
            )
            .clip(CoFlowGlassStyles.CardShape)
            .background(CoFlowGlassStyles.Surface)
            .border(
                1.dp,
                CoFlowGlassStyles.BlueBorder,
                CoFlowGlassStyles.CardShape
            )
            .padding(horizontal = 12.dp, vertical = 9.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggle),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = group.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = CoFlowColors.TextPrimary
            )

            Spacer(Modifier.width(7.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(5.dp))
                    .background(CoFlowColors.BlueLight.copy(alpha = 0.38f))
                    .padding(horizontal = 5.dp, vertical = 2.dp)
            ) {
                Text(
                    text = group.tasks.size.toString(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = CoFlowColors.Blue
                )
            }

            Spacer(Modifier.weight(1f))

            Text(
                text = if (expanded) "⌄" else "›",
                color = CoFlowColors.Blue,
                fontSize = 17.sp
            )
        }

        if (expanded) {
            if (group.tasks.isEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Нет задач",
                    fontSize = 11.sp,
                    color = CoFlowColors.TextSecondary
                )
            } else {
                Spacer(Modifier.height(3.dp))

                group.tasks.forEachIndexed { index, task ->
                    TaskRow(task)

                    if (index < group.tasks.lastIndex) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(
                                    CoFlowGlassStyles.BlueBorder.copy(
                                        alpha = 0.45f
                                    )
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TaskRow(task: Task) {
    val formatter = remember {
        DateTimeFormatter.ofPattern("dd.MM.yy")
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = task.title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (task.status == TaskStatus.DONE) {
                    CoFlowColors.TextSecondary
                } else {
                    CoFlowColors.TextPrimary
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            if (
                task.dueDate.isBefore(LocalDate.now()) &&
                task.status != TaskStatus.DONE
            ) {
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "Просрочено",
                    fontSize = 8.sp,
                    color = CoFlowColors.Error
                )
            }
        }

        Spacer(Modifier.height(3.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = task.projectName,
                fontSize = 10.sp,
                color = CoFlowColors.TextSecondary,
                maxLines = 1
            )

            Text(
                text = task.dueDate.format(formatter),
                fontSize = 10.sp,
                color = CoFlowColors.TextSecondary
            )
        }
    }
}

@Composable
private fun SortFilterPanel(
    modifier: Modifier = Modifier,
    grouping: TaskGrouping,
    onGroupingChange: (TaskGrouping) -> Unit,
    selectedFilter: String,
    onFilterChange: (String) -> Unit
) {
    // No drop shadow: the controls should read as thin glass, not as raised white cards.
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            var expanded by remember { mutableStateOf(false) }

            GlassControlSurface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
            ) {
                Column {
                    Text(
                        text = "Группировка",
                        fontSize = 9.sp,
                        color = CoFlowColors.TextSecondary
                    )
                    Spacer(Modifier.height(3.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (grouping == TaskGrouping.STATUS) "По статусу" else "По сроку",
                            fontSize = 11.sp,
                            color = CoFlowColors.TextPrimary,
                            maxLines = 1
                        )
                        Spacer(Modifier.width(3.dp))
                        Text("⌄", fontSize = 11.sp, color = CoFlowColors.Blue)
                    }
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                shape = RoundedCornerShape(16.dp),
                containerColor = Color(0xFFF4F8FF).copy(alpha = 0.62f),
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.82f))
            ) {
                GlassGroupingOption(
                    title = "По статусу",
                    selected = grouping == TaskGrouping.STATUS,
                    onClick = {
                        onGroupingChange(TaskGrouping.STATUS)
                        expanded = false
                    }
                )
                GlassGroupingOption(
                    title = "По сроку",
                    selected = grouping == TaskGrouping.DEADLINE,
                    onClick = {
                        onGroupingChange(TaskGrouping.DEADLINE)
                        expanded = false
                    }
                )
            }
        }

        Box(modifier = Modifier.weight(1f)) {
            var expanded by remember { mutableStateOf(false) }

            GlassControlSurface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }
            ) {
                Column {
                    Text(
                        text = "Фильтр",
                        fontSize = 9.sp,
                        color = CoFlowColors.TextSecondary
                    )
                    Spacer(Modifier.height(3.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = selectedFilter,
                            fontSize = 11.sp,
                            color = CoFlowColors.TextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        Spacer(Modifier.width(3.dp))
                        Text("⌄", fontSize = 11.sp, color = CoFlowColors.Blue)
                    }
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                shape = RoundedCornerShape(16.dp),
                containerColor = Color(0xFFF4F8FF).copy(alpha = 0.62f),
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.82f))
            ) {
                listOf(
                    "Без фильтра",
                    "Мои",
                    "Проект 1",
                    "Проект 2",
                    "Проект 3"
                ).forEach { option ->
                    GlassFilterOption(
                        title = option,
                        selected = selectedFilter == option,
                        onClick = {
                            onFilterChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun GlassControlSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(13.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.30f),
                        Color(0xFFDDEAFF).copy(alpha = 0.16f)
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.92f),
                        CoFlowColors.Blue.copy(alpha = 0.14f)
                    )
                ),
                shape = RoundedCornerShape(13.dp)
            )
            .padding(horizontal = 9.dp, vertical = 7.dp)
    ) {
        content()
    }
}

@Composable
private fun GlassGroupingOption(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 6.dp, vertical = 2.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected) CoFlowColors.BlueLight.copy(alpha = 0.14f)
                else Color.Transparent
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(modifier = Modifier.size(10.dp)) {
            if (selected) {
                drawCircle(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF6EA5FF), CoFlowColors.Blue),
                        start = Offset.Zero,
                        end = Offset(size.width, size.height)
                    )
                )
            } else {
                drawCircle(
                    color = CoFlowGlassStyles.BlueBorder,
                    style = Stroke(width = 1.2.dp.toPx())
                )
            }
        }
        Spacer(Modifier.width(9.dp))
        Text(
            text = title,
            fontSize = 13.sp,
            color = CoFlowColors.TextPrimary,
            maxLines = 1
        )
    }
}

@Composable
private fun GlassFilterOption(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 6.dp, vertical = 2.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected) CoFlowColors.BlueLight.copy(alpha = 0.14f)
                else Color.Transparent
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(
                    if (selected) CoFlowColors.Blue
                    else Color.Transparent
                )
                .border(
                    width = 1.dp,
                    color = if (selected) CoFlowColors.Blue else CoFlowGlassStyles.BlueBorder,
                    shape = RoundedCornerShape(5.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (selected) {
                Text("✓", fontSize = 10.sp, color = Color.White)
            }
        }
        Spacer(Modifier.width(9.dp))
        Text(
            text = title,
            fontSize = 13.sp,
            color = CoFlowColors.TextPrimary,
            maxLines = 1
        )
    }
}

@Composable
private fun GlassIconButton(
    icon: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(38.dp)
            .shadow(
                elevation = 2.dp,
                shape = CircleShape,
                ambientColor = CoFlowGlassStyles.Shadow,
                spotColor = CoFlowGlassStyles.Shadow
            )
            .clip(CircleShape)
            .background(CoFlowGlassStyles.ControlSurface)
            .border(1.dp, CoFlowGlassStyles.Border, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        CoFlowIcon(
            name = icon,
            modifier = Modifier.size(21.dp),
            color = CoFlowColors.TextPrimary
        )
    }
}

@Composable
private fun BottomNavigationBar(
    onProjectsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .widthIn(max = 278.dp)
            .shadow(
                elevation = 1.dp,
                shape = CoFlowGlassStyles.NavigationShape,
                ambientColor = CoFlowGlassStyles.Shadow.copy(alpha = 0.28f),
                spotColor = CoFlowGlassStyles.Shadow.copy(alpha = 0.28f)
            )
            .clip(CoFlowGlassStyles.NavigationShape)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CoFlowGlassStyles.ControlSurface.copy(alpha = 0.52f),
                        CoFlowGlassStyles.ControlSurface.copy(alpha = 0.34f)
                    )
                ),
                shape = CoFlowGlassStyles.NavigationShape
            )
            .border(
                1.dp,
                CoFlowGlassStyles.Border.copy(alpha = 0.68f),
                CoFlowGlassStyles.NavigationShape
            )
            .padding(3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationItem(
            modifier = Modifier.weight(1f),
            icon = "tasks",
            title = "Мои задачи",
            selected = true,
            onClick = {}
        )

        NavigationItem(
            modifier = Modifier.weight(1f),
            icon = "folder",
            title = "Проекты",
            selected = false,
            onClick = onProjectsClick
        )

        NavigationItem(
            modifier = Modifier.weight(1f),
            icon = "person",
            title = "Профиль",
            selected = false,
            onClick = onProfileClick
        )
    }
}

@Composable
private fun NavigationItem(
    modifier: Modifier = Modifier,
    icon: String,
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(CoFlowGlassStyles.SelectedItemShape)
            .background(
                if (selected) {
                    CoFlowColors.Blue
                } else {
                    Color.Transparent
                }
            )
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CoFlowIcon(
            name = icon,
            modifier = Modifier.size(18.dp),
            color = if (selected) Color.White else CoFlowColors.TextPrimary
        )

        Spacer(Modifier.height(1.dp))

        Text(
            text = title,
            fontSize = 10.sp,
            color = if (selected) Color.White else CoFlowColors.TextPrimary,
            maxLines = 1
        )
    }
}

// Простые контурные иконки без сторонних библиотек.
@Composable
private fun CoFlowIcon(
    name: String,
    modifier: Modifier = Modifier,
    color: Color
) {
    Canvas(modifier = modifier) {
        val s = size.minDimension / 24f
        val stroke = Stroke(width = 1.6f * s)

        fun point(x: Float, y: Float) = Offset(x * s, y * s)

        when (name) {
            "bell" -> {
                val path = Path().apply {
                    moveTo(5f * s, 16f * s)
                    lineTo(7f * s, 13.5f * s)
                    lineTo(7f * s, 10f * s)
                    cubicTo(
                        7f * s, 3.5f * s,
                        17f * s, 3.5f * s,
                        17f * s, 10f * s
                    )
                    lineTo(17f * s, 13.5f * s)
                    lineTo(19f * s, 16f * s)
                    close()
                }
                drawPath(path, color, style = stroke)
                drawLine(
                    color,
                    point(5f, 16f),
                    point(19f, 16f),
                    strokeWidth = 1.6f * s
                )
                drawArc(
                    color,
                    startAngle = 0f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = point(9f, 15.5f),
                    size = androidx.compose.ui.geometry.Size(6f * s, 5f * s),
                    style = stroke
                )
            }

            "tasks" -> {
                drawRoundRect(
                    color,
                    topLeft = point(5f, 4f),
                    size = androidx.compose.ui.geometry.Size(14f * s, 17f * s),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(1.5f * s),
                    style = stroke
                )
                for (y in listOf(8f, 12f, 16f)) {
                    drawLine(
                        color,
                        point(10f, y),
                        point(16f, y),
                        strokeWidth = 1.3f * s
                    )
                    drawCircle(
                        color,
                        radius = 0.8f * s,
                        center = point(7.5f, y)
                    )
                }
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

            "person" -> {
                drawCircle(
                    color,
                    radius = 3.2f * s,
                    center = point(12f, 7.5f),
                    style = stroke
                )
                val path = Path().apply {
                    moveTo(4f * s, 20f * s)
                    lineTo(4f * s, 17.5f * s)
                    cubicTo(
                        4f * s, 12.5f * s,
                        20f * s, 12.5f * s,
                        20f * s, 17.5f * s
                    )
                    lineTo(20f * s, 20f * s)
                }
                drawPath(path, color, style = stroke)
            }
        }
    }
}
