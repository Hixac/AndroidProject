package com.example.coflow.models
import kotlin.collections.plus

class ProjectList (
    initialProjects: List<ProjectModel> = emptyList()
){
    var projects: List<ProjectModel> = initialProjects
        private set

    init {
        require(projects.distinctBy { it.id }.size == projects.size) {
            "Дубли id проектов: ${projects.map { it.id }}"
        }
    }

    fun addProject(project: ProjectModel){
        require(projects.none { it.id == project.id }) { "Проект ${project.id} уже есть" }
        projects += project
    }

    fun removeProject(projectId: Int){
        val filtered = projects.filterNot { it.id == projectId }
        projects = filtered
    }
}