package com.example.coflow.models

import java.time.LocalDate

data class TaskModel(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val projectId: String = "",
    val authorId: String = "",
    val assigneeIds: List<String> = emptyList(),
    val dueDate: LocalDate = LocalDate.now(),
    val status: TaskStatus = TaskStatus.TODO,
    val isMine: Boolean = true
)