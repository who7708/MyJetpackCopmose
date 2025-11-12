package com.example.fe.myapplication.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.fe.myapplication.dao.ProjectDao
import com.example.fe.myapplication.model.Project

class ProjectPagingSource(
    private val projectDao: ProjectDao,
    private val nameQuery: String,
    private val locationQuery: String
) : PagingSource<Int, Project>() {
    
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Project> {
        return try {
            val page = params.key ?: 1
            val projects = projectDao.getProjects("%$nameQuery%", "%$locationQuery%")
            LoadResult.Page(
                data = projects,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (projects.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Project>): Int? {
        return state.anchorPosition
    }
}
