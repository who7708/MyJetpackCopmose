// ProjectResponse.kt
package com.example.fe.myapplication.test1.network

import com.example.fe.myapplication.test1.model.Project

data class ProjectResponse(
    val projects: List<Project>,
    val total_pages: Int
)
