package com.example.coflow.data.firebase


interface AuthRepository {

    /**
     * Регистрирует нового пользователя.
     * @return [Result.success] с UID или [Result.failure] с текстом ошибки.
     */
    suspend fun register(email: String, password: String, name: String): Result<String>

    /**
     * Выполняет вход пользователя.
     * @return [Result.success] с UID или [Result.failure] с текстом ошибки.
     */
    suspend fun login(email: String, password: String): Result<String>

    /**
     * Авторизация через Google.
     * @param idToken Токен, полученный от Google Sign-In API.
     * @return [Result.success] с UID или [Result.failure] с ошибкой.
     */
    suspend fun signInWithGoogle(idToken: String): Result<String>

    /** Выход из системы. */
    fun logout()

    /** Получить UID текущего пользователя или null. */
    fun getCurrentUserUid(): String?
}