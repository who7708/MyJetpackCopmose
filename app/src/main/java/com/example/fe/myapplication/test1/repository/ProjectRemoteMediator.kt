// ProjectRemoteMediator.kt
package com.example.fe.myapplication.test1.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.fe.myapplication.test1.data.ProjectDao
import com.example.fe.myapplication.test1.data.ProjectDatabase
import com.example.fe.myapplication.test1.model.Project
import com.example.fe.myapplication.test1.network.ProjectService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ProjectRemoteMediator(
    private val database: ProjectDatabase,
    private val projectService: ProjectService,
    private val searchQuery: String
) : RemoteMediator<Int, Project>() {

    private val projectDao: ProjectDao = database.projectDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Project>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
                (lastItem.id / state.config.pageSize) + 1
            }
        }

        try {
            val response = projectService.getProjects(page, state.config.pageSize, searchQuery)
            val projects = response.projects
            val endOfPaginationReached = projects.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    projectDao.clearAllProjects()
                }
                projectDao.insertProjects(projects)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }
}
