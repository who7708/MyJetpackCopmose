package com.example.fe.myapplication.modelview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Project(val name: String, val location: String, val status: String)

sealed class ProjectState {
    object Loading : ProjectState()
    data class Success(val projects: List<Project>) : ProjectState()
    data class Error(val message: String) : ProjectState()
}

class ProjectViewModel : ViewModel() {

    // 状态 Flow
    private val _projectState = MutableStateFlow<ProjectState>(ProjectState.Loading)
    val projectState: StateFlow<ProjectState> = _projectState

    // 分页参数
    private var currentPage = 1
    private val pageSize = 10

    private var allProjects: List<Project> = listOf(
        Project("项目A", "场地1", "进行中"),
        Project("项目B", "场地2", "已完成"),
        Project("项目C", "场地3", "未开始"),
        Project("项目D", "场地4", "进行中"),
        // 添加更多项目
    )

    private var filteredProjects: List<Project> = allProjects

    init {
        // 加载第一页数据
        loadProjects()
    }

    // 加载项目数据
    fun loadProjects() {
        viewModelScope.launch {
            try {
                _projectState.value = ProjectState.Loading
                // 模拟网络延迟
                delay(1000)

                val start = (currentPage - 1) * pageSize
                val end = (start + pageSize).coerceAtMost(filteredProjects.size)

                if (start < end) {
                    val currentProjects = filteredProjects.subList(0, end)
                    _projectState.value = ProjectState.Success(currentProjects)
                    currentPage++
                } else {
                    _projectState.value = ProjectState.Error("没有更多项目了")
                }
            } catch (e: Exception) {
                _projectState.value = ProjectState.Error("加载项目失败：${e.message}")
            }
        }
    }

    // 搜索项目
    fun searchProjects(name: String, location: String) {
        filteredProjects = allProjects.filter { project ->
            project.name.contains(name, ignoreCase = true) &&
                    project.location.contains(location, ignoreCase = true)
        }
        currentPage = 1 // 重置分页
        loadProjects() // 重新加载数据
    }
}

