package com.example.fe.myapplication.service

import com.example.fe.myapplication.model.ProjectResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProjectService {
    @GET("projects")
    suspend fun getProjects(
        @Query("page") page: Int,            // 当前页码
        @Query("size") size: Int,            // 每页数量
        @Query("query") query: String        // 搜索关键字（例如项目名称）
    ): ProjectResponse
}
