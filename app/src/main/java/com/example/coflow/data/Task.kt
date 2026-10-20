
package com.example.coflow.data

import java.time.LocalDate
import com.example.coflow.models.TaskStatus

// Модель одной задачи.
data class Task(
    val id: Int,
    val title: String,
    val projectName: String,
    val dueDate: LocalDate,
    val status: TaskStatus,
    val isMine: Boolean = true
)