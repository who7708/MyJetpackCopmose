package com.example.fe.myapplication.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fe.myapplication.model.Project

@Dao
interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjects(projects: List<Project>)

    @Query("SELECT * FROM projects WHERE name LIKE :name AND location LIKE :location")
    fun getProjects(name: String, location: String): PagingSource<Int, Project>

    @Query("delete FROM projects")
    fun clearAllProjects()

}
