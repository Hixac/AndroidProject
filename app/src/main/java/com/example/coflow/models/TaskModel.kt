package com.example.coflow.data

import java.time.LocalDateTime
import kotlin.time.Instant

/** Перечисление для статусов задачи */
enum class TaskStatus (val code:Int) {
    TODO(0),
    IN_PROGRESS(1),
    DONE(2);

    companion object {
        fun fromInt(value: Int): TaskStatus {
            return entries.firstOrNull { it.code == value } ?: TODO
        }
    }
}

class TaskModel (
    val id: Int,
    _title: String, // название
    _description: String, // описание
    var performers: Array<UserModel>, // исполнители задачи
    var deadline: LocalDateTime, // дата окончания
    //var color: String, // цвет задачи
    var status: TaskStatus // TODO - к исполнению, IN_PROGRESS - в процессе, DONE - сделано
) {

    var title: String = ""
        set(value) {
            require(value.isNotBlank()) { "Название не может быть пустым" }
            field = value.trim()
        }

    var description: String = ""
        set(value) {
            field = value.trim()
        }

    init {
        title = _title
        description = _description
        require(deadline.isAfter(LocalDateTime.now())) { "Дедлайн должен быть в будущем" }
    }

    fun ClearPerformers() {
        performers = emptyArray()
    }
}