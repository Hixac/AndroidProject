package com.example.coflow.models

import java.time.LocalDateTime

class ProjectModel (
    val id: Int,
    _title: String,
    _description: String,
    _updated: LocalDateTime = LocalDateTime.now(),
    initialTasks: List<TaskModel> = emptyList() // Начальная инициализация списка задач для бд
){
    var title: String = _title.trim()
        set(value) {
            require(value.isNotBlank())
            field = value.trim()
            touch()
        }

    var description: String = _description.trim()
        set(value) {
            field = value.trim()
            touch()
        }

    var updated: LocalDateTime = _updated
        private set


    private fun touch(){ // Вызов "последние обновление проекта" вручную
        updated = LocalDateTime.now()
    }

    var tasks: List<TaskModel> = initialTasks
        private set

    init {
        require(_title.isNotBlank()) { "Название проекта не может быть пустым" }   // проверка при создании
        require(tasks.distinctBy { it.id }.size == tasks.size) {
            "Дубли id задач в проекте $id: ${tasks.map { it.id }}"
        }
    }

    fun addTask(task: TaskModel){
        require(tasks.none { it.id == task.id }) { "Задача ${task.id} уже есть" }
        tasks += task
        touch()
    }

    // Возвращает значение, на случай если в UI был даблклик или несколько пользователей одновременно удалили задачу
    fun removeTask(taskId: Int): Boolean {
        val filtered = tasks.filterNot { it.id == taskId }
        if (filtered.size == tasks.size) return false
        tasks = filtered
        touch()
        return true
    }

    // Если задача была обновлена
    fun markUpdated() = touch()
}