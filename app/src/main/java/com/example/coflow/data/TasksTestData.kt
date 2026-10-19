package com.example.coflow.data

import java.time.LocalDate

/**
 * Временные данные для проверки экрана задач.
 * Позже этот объект заменят данными из репозитория/бэкенда.
 */
object TasksTestData {
    private val today = LocalDate.now()

    val tasks = listOf(
        Task(
            id = 1,
            title = "Согласовать план проекта",
            projectName = "Проект 1",
            dueDate = today,
            status = TaskStatus.TODO,
            isMine = true
        ),
        Task(
            id = 2,
            title = "Подготовить макеты",
            projectName = "Проект 1",
            dueDate = today,
            status = TaskStatus.TODO,
            isMine = true
        ),
        Task(
            id = 3,
            title = "Отправить материалы",
            projectName = "Проект 1",
            dueDate = today,
            status = TaskStatus.DONE,
            isMine = true
        ),
        Task(
            id = 4,
            title = "Собрать обратную связь",
            projectName = "Проект 1",
            dueDate = today.plusDays(1),
            status = TaskStatus.TODO,
            isMine = true
        ),
        Task(
            id = 5,
            title = "Разобрать заметки",
            projectName = "Мои",
            dueDate = today.plusDays(2),
            status = TaskStatus.DONE,
            isMine = true
        ),
        Task(
            id = 6,
            title = "Настроить навигацию",
            projectName = "Проект 2",
            dueDate = today.plusDays(3),
            status = TaskStatus.IN_PROGRESS,
            isMine = false
        ),
        Task(
            id = 7,
            title = "Проверить адаптивность экрана",
            projectName = "Проект 2",
            dueDate = today.plusDays(4),
            status = TaskStatus.TODO,
            isMine = true
        ),
        Task(
            id = 8,
            title = "Исправить замечания по интерфейсу",
            projectName = "Проект 3",
            dueDate = today.plusDays(5),
            status = TaskStatus.IN_PROGRESS,
            isMine = false
        ),
        Task(
            id = 9,
            title = "Подготовить описание API",
            projectName = "Проект 2",
            dueDate = today.plusDays(7),
            status = TaskStatus.TODO,
            isMine = true
        ),
        Task(
            id = 10,
            title = "Проверить сценарии авторизации",
            projectName = "Проект 1",
            dueDate = today.plusDays(8),
            status = TaskStatus.IN_PROGRESS,
            isMine = true
        ),
        Task(
            id = 11,
            title = "Обновить документацию",
            projectName = "Проект 3",
            dueDate = today.plusDays(10),
            status = TaskStatus.TODO,
            isMine = false
        ),
        Task(
            id = 12,
            title = "Согласовать структуру базы данных",
            projectName = "Проект 2",
            dueDate = today.plusDays(12),
            status = TaskStatus.DONE,
            isMine = true
        ),
        Task(
            id = 13,
            title = "Проверить интеграцию",
            projectName = "Проект 1",
            dueDate = today.minusDays(1),
            status = TaskStatus.TODO,
            isMine = true
        ),
        Task(
            id = 14,
            title = "Исправить ошибку фильтрации",
            projectName = "Проект 2",
            dueDate = today.minusDays(2),
            status = TaskStatus.IN_PROGRESS,
            isMine = false
        ),
        Task(
            id = 15,
            title = "Сдать промежуточный отчёт",
            projectName = "Проект 3",
            dueDate = today.minusDays(4),
            status = TaskStatus.DONE,
            isMine = true
        ),
        Task(
            id = 16,
            title = "Пересмотреть цвета карточек",
            projectName = "Проект 1",
            dueDate = today.minusDays(8),
            status = TaskStatus.TODO,
            isMine = false
        ),
        Task(
            id = 17,
            title = "Проверить отображение уведомлений",
            projectName = "Проект 3",
            dueDate = today.minusDays(14),
            status = TaskStatus.IN_PROGRESS,
            isMine = true
        ),
        Task(
            id = 18,
            title = "Подготовить демонстрацию",
            projectName = "Проект 2",
            dueDate = today.plusDays(20),
            status = TaskStatus.TODO,
            isMine = true
        ),
        Task(
            id = 19,
            title = "Закрыть старые задачи",
            projectName = "Проект 3",
            dueDate = today.minusDays(30),
            status = TaskStatus.DONE,
            isMine = false
        ),
        Task(
            id = 20,
            title = "Проверить крайние случаи",
            projectName = "Проект 1",
            dueDate = today.plusDays(30),
            status = TaskStatus.IN_PROGRESS,
            isMine = true
        )
    )
}
