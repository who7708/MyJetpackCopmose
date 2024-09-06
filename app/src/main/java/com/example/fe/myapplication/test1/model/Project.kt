// Project.kt
package com.example.fe.myapplication.test1.model

import androidx.room.PrimaryKey

@androidx.room.Entity(tableName = "projects")
data class Project(
    @PrimaryKey val id: Int,
    val name: String,
    val location: String,
    val status: String
)
