// ProjectViewModel.kt
package com.example.fe.myapplication.test1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.fe.myapplication.test1.model.Project
import com.example.fe.myapplication.test1.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProjectViewModel(
    private val repository: ProjectRepository
) : ViewModel() {

    fun getProjects(name: String, location: String): Flow<PagingData<Project>> {
        return repository.getProjectStream(name, location).cachedIn(viewModelScope)
    }

    fun getProjectStream(name: String, location: String): Flow<PagingData<Project>> {
        return repository.getProjectStream(name, location).cachedIn(viewModelScope)
    }

    fun getProjectDetail(projectId: Int): Flow<Project?> {
        return flow {
            val project = repository.getProjectDetail(projectId)
            emit(project)
        }
    }
}
