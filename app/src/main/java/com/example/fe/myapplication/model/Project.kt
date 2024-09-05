package com.example.fe.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey val id: Int,
    val name: String,
    val location: String,
    val status: String
)
