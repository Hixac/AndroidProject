package com.example.coflow.data.firebase

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

// Реализация репозитория задач для Firestore
class FirebaseTaskRepositoryImpl(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : TaskRepository {

    private val taskCollection: CollectionReference = db.collection("Task")

    override suspend fun getById(id: String): TaskSchema? {
        val snapshot = taskCollection.document(id).get().await()
        return TaskSchema.fromDocument(snapshot)
    }

    override suspend fun getByTitle(title: String): List<TaskSchema> {
        val querySnapshot = taskCollection
            .whereEqualTo("title", title)
            .get()
            .await()

        return querySnapshot.documents.mapNotNull { TaskSchema.fromDocument(it) }
    }

    override suspend fun getByProjectId(id: String): List<TaskSchema> {
        val projectRef = db.collection("Project").document(id)
        val querySnapshot = taskCollection
            .whereEqualTo("projectRef", projectRef)
            .get()
            .await()

        return querySnapshot.documents.mapNotNull { TaskSchema.fromDocument(it) }
    }

    override suspend fun addTask(taskSchema: TaskSchema): TaskSchema {
        return if (taskSchema.id.isNotBlank()) {
            taskCollection.document(taskSchema.id).set(taskSchema.toMap()).await()
            taskSchema
        } else {
            val docRef = taskCollection.add(taskSchema.toMap()).await()
            taskSchema.copy(id = docRef.id)
        }
    }

    override suspend fun deleteTask(id: String) {
        taskCollection.document(id).delete().await()
    }
}
