package com.example.coflow.ui.tasks

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coflow.ui.components.*
import com.example.coflow.ui.theme.*
import androidx.compose.ui.tooling.preview.Preview

data class CreateTaskFormData(
    val title: String, val description: String, val assignees: String,
    val project: String, val dueDate: String
)

@Composable
fun CreateTaskScreen(
    onBackClick: () -> Unit,
    onCreateTask: (CreateTaskFormData) -> Unit,
    onNavigateToAssignees: () -> Unit,
    onNavigateToProject: () -> Unit,
    onNavigateToDate: () -> Unit
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var assignees by rememberSaveable { mutableStateOf("") }
    var project by rememberSaveable { mutableStateOf("") }
    var dueDate by rememberSaveable { mutableStateOf("") }

    val focusManager = LocalFocusManager.current
    val canCreate = title.isNotBlank()

    BackHandler(onBack = onBackClick)

    Box(modifier = Modifier.fillMaxSize().background(coFlowTasksBackgroundBrush())) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = CoFlowSpacing.ScreenHorizontal)) {
            Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 22.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(48.dp).clip(CircleShape).clickable { focusManager.clearFocus(); onBackClick() },
                    contentAlignment = Alignment.Center
                ) {
                    CoFlowIcon(name = "back", modifier = Modifier.size(22.dp), color = CoFlowColors.TextSecondary, contentDescription = "Назад")
                }
                Spacer(Modifier.width(4.dp))
                Text(text = "Создание задачи", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = CoFlowColors.TextPrimary)
            }

            Column(modifier = Modifier.weight(1f).fillMaxWidth().verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                CoFlowGlassTextField(
                    label = "Название", value = title, onValueChange = { title = it },
                    placeholder = "Введите название задачи"
                )
                CoFlowGlassTextField(
                    label = "Описание", value = description, onValueChange = { description = it.take(250) },
                    placeholder = "Введите описание задачи", singleLine = false, maxLength = 250
                )
                CoFlowGlassTextField(
                    label = "Исполнители", value = assignees, onValueChange = { assignees = it },
                    placeholder = "Выберите исполнителей", leadingIcon = "person", showTrailingArrow = true, onClick = onNavigateToAssignees
                )
                CoFlowGlassTextField(
                    label = "Проект", value = project, onValueChange = { project = it },
                    placeholder = "Выберите проект", leadingIcon = "folder", showTrailingArrow = true, onClick = onNavigateToProject
                )
                CoFlowGlassTextField(
                    label = "Дата окончания", value = dueDate, onValueChange = { dueDate = it },
                    placeholder = "Выберите дату", leadingIcon = "calendar", showTrailingArrow = true, onClick = onNavigateToDate,
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                    keyboardActions = androidx.compose.foundation.text.KeyboardActions(onDone = { focusManager.clearFocus() })
                )
                Spacer(Modifier.height(16.dp))
            }

            CoFlowPrimaryButton(
                text = "Создать задачу",
                onClick = {
                    focusManager.clearFocus()
                    onCreateTask(CreateTaskFormData(title.trim(), description.trim(), assignees.trim(), project.trim(), dueDate.trim()))
                },
                enabled = canCreate,
                modifier = Modifier.padding(top = 12.dp, bottom = 12.dp)
            )
        }
    }
}

@Preview(name = "Создание задачи", showBackground = true, widthDp = 390, heightDp = 844)
@Composable
private fun CreateTaskScreenPreview() {
    CoFlowTheme {
        CreateTaskScreen(
            onBackClick = { println("Preview: Назад") },
            onCreateTask = { println("Preview: Создание задачи: $it") },
            onNavigateToAssignees = { println("Preview: Переход к выбору исполнителей") },
            onNavigateToProject = { println("Preview: Переход к выбору проекта") },
            onNavigateToDate = { println("Preview: Переход к выбору даты") }
        )
    }
}