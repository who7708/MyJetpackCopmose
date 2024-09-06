// ProjectService.kt
package com.example.fe.myapplication.test1.network

import retrofit2.http.GET
import retrofit2.http.Query

interface ProjectService {
    @GET("/api/v1/projects/15")
    suspend fun getProjects(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("query") query: String = ""
    ): ProjectResponse
}
