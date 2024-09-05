package com.example.fe.myapplication.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.fe.myapplication.common.ProjectDatabase
import com.example.fe.myapplication.common.RetrofitInstance
import com.example.fe.myapplication.model.Project
import kotlinx.coroutines.flow.Flow

class ProjectRepository(
    private val database: ProjectDatabase,
) {

    private val projectService = RetrofitInstance.projectService // 获取 ProjectService 实例

    @OptIn(ExperimentalPagingApi::class)
    fun getProjectStream(nameQuery: String, locationQuery: String): Flow<PagingData<Project>> {
        return Pager(config = PagingConfig(pageSize = 10),
            remoteMediator = ProjectRemoteMediator(database, projectService, nameQuery),
            pagingSourceFactory = {
                ProjectPagingSource(
                    database.projectDao(), nameQuery, locationQuery
                )
            }).flow
    }
}
