package com.example.coflow.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class FirebaseAuthRepositoryImpl : AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override suspend fun register(email: String, password: String, name: String): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: throw Exception("Не удалось получить UID")

            db.collection("users").document(uid).set(
                mapOf(
                    "uid" to uid,
                    "email" to email,
                    "name" to name,
                    "createdAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
                )
            ).await()

            Result.success(uid)
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is FirebaseAuthWeakPasswordException -> "Пароль слишком простой (минимум 6 символов)"
                is FirebaseAuthUserCollisionException -> "Пользователь с такой почтой уже существует"
                else -> e.message ?: "Ошибка регистрации"
            }
            Result.failure(Exception(errorMessage))
        }
    }

    override suspend fun login(email: String, password: String): Result<String> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(auth.currentUser?.uid ?: "")
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is FirebaseAuthInvalidCredentialsException -> "Неверная почта или пароль"
                else -> e.message ?: "Ошибка входа"
            }
            Result.failure(Exception(errorMessage))
        }
    }

    override suspend fun signInWithGoogle(idToken: String): Result<String> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            val user = authResult.user ?: throw Exception("Не удалось получить пользователя")
            val uid = user.uid

            // Если пользователь заходит впервые — создаем документ в Firestore
            val userDoc = db.collection("users").document(uid).get().await()
            if (!userDoc.exists()) {
                db.collection("users").document(uid).set(
                    mapOf(
                        "uid" to uid,
                        "email" to (user.email ?: ""),
                        "name" to (user.displayName ?: "Пользователь Google"),
                        "createdAt" to com.google.firebase.firestore.FieldValue.serverTimestamp()
                    )
                ).await()
            }

            Result.success(uid)
        } catch (e: Exception) {
            Result.failure(Exception(e.message ?: "Ошибка входа через Google"))
        }
    }

    override fun logout() {
        auth.signOut()
    }

    override fun getCurrentUserUid(): String? {
        return auth.currentUser?.uid
    }
}