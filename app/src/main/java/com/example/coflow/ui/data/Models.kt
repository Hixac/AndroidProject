package com.example.coflow.data

import android.content.Context
import com.example.coflow.R
import java.time.LocalDateTime
import kotlin.time.Instant

class TaskModel (
    val id: Int,
    _title: String, // название
    _description: String, // описание
    _deadline: LocalDateTime, // дата, до которой сделать
    var _isSolved: Boolean // решена или нет
) {

    var title: String = _title
        set(value) {
            require(value.isNotBlank())
            field = value.trim()
        }

    var description: String = _description
        set(value) {
            field = value.trim()
        }

    var deadline: LocalDateTime = _deadline
        set(value) {
            require(LocalDateTime.now()>value)
            field = value
        }
}