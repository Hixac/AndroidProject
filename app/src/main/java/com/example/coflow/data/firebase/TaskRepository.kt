package com.example.coflow.data.firebase

// Интерфейс репозитория для работы с задачами в Firestore
interface TaskRepository {
    suspend fun getById(id: String): TaskSchema?
    suspend fun getByTitle(title: String): List<TaskSchema>
    suspend fun getByProjectId(id: String): List<TaskSchema>
    suspend fun addTask(taskSchema: TaskSchema): TaskSchema
    suspend fun deleteTask(id: String)
}
