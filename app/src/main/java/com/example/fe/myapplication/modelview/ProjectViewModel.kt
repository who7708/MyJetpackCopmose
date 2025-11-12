package com.example.fe.myapplication.modelview

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.example.fe.myapplication.model.Project
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProjectViewModel(
    // private val projectRepository: ProjectRepository
) : ViewModel() {

    fun getProjectStream(name: String, location: String): Flow<PagingData<Project>> {
        // return projectRepository.getProjectStream(name, location).cachedIn(viewModelScope)
        return flow {
            // 模拟分页数据加载，这里可以根据实际情况从数据源加载数据
            val items = mutableListOf<Project>()
            for (i in 1..100) {
                items.add(Project(i, "项目 $i", "场地 $i", if (i % 2 == 0) "进行中" else "已完成"))
            }
            emit(PagingData.from(items))
        }
    }


    private fun generateDummyProjects(): List<Project> {
        return (1..50).map { i ->
            Project(i, "项目 $i", "场地 $i", if (i % 2 == 0) "进行中" else "已完成")
        }
    }


}
