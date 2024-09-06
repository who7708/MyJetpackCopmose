// ProjectDao.kt
package com.example.fe.myapplication.test1.data

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fe.myapplication.test1.model.Project

@Dao
interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjects(projects: List<Project>)

    @Query("DELETE FROM projects")
    suspend fun clearAllProjects()

    @Query(
        """
        SELECT * FROM projects 
        WHERE name LIKE '%' || :name || '%' 
        AND location LIKE '%' || :location || '%'
        ORDER BY id ASC
    """
    )
    fun getProjects(name: String, location: String): PagingSource<Int, Project>
}
