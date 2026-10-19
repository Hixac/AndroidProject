
package com.example.coflow.data

import java.time.LocalDate

// Модель одной задачи.
data class Task(
    val id: Int,
    val title: String,
    val projectName: String,
    val dueDate: LocalDate,
    val status: TaskStatus,
    val isMine: Boolean = true
)

// Состояния задачи для группировки
enum class TaskStatus {
    TODO,        // К исполнению
    IN_PROGRESS, // В процессе
    DONE         // Сделано
}
