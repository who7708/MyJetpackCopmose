package com.example.fe.myapplication.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.fe.myapplication.common.ProjectDatabase
import com.example.fe.myapplication.model.Project
import com.example.fe.myapplication.service.ProjectService
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ProjectRemoteMediator(
    private val database: ProjectDatabase,
    private val projectService: ProjectService,  // 假设你有个 Retrofit 服务来获取数据
    private val searchQuery: String
) : RemoteMediator<Int, Project>() {

    private val projectDao = database.projectDao()

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

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    projectDao.clearAllProjects()  // 刷新时清空缓存
                }
                projectDao.insertProjects(response.projects)
            }
            return MediatorResult.Success(endOfPaginationReached = response.projects.isEmpty())
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }
}
