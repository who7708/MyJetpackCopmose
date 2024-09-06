// ProjectRepository.kt
package com.example.fe.myapplication.test1.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.fe.myapplication.test1.data.ProjectDatabase
import com.example.fe.myapplication.test1.model.Project
import com.example.fe.myapplication.test1.network.ProjectService
import kotlinx.coroutines.flow.Flow

class ProjectRepository(
    private val database: ProjectDatabase,
    private val projectService: ProjectService
) {

    @OptIn(ExperimentalPagingApi::class)
    fun getProjectStream(nameQuery: String, locationQuery: String): Flow<PagingData<Project>> {
        val query = "$nameQuery $locationQuery"

        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            remoteMediator = ProjectRemoteMediator(database, projectService, nameQuery),
            pagingSourceFactory = { database.projectDao().getProjects(nameQuery, locationQuery) }
        ).flow
    }

    fun getProjectDetail(projectId: Int): Project? {
        TODO("Not yet implemented")
    }
}
