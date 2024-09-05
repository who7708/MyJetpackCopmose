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

    private val _projectState = MutableStateFlow<ProjectState>(ProjectState.Loading)
    val projectState: StateFlow<ProjectState> = _projectState

    private var currentPage = 1
    private val pageSize = 10
    private var isLoading = false
    private var allProjects: List<Project> = generateDummyProjects()

    private var filteredProjects: List<Project> = allProjects
    private var displayedProjects: MutableList<Project> = mutableListOf()

    init {
        loadProjects()
    }

    fun loadProjects() {
        if (isLoading) return  // 防止重复加载
        viewModelScope.launch {
            try {
                isLoading = true
                _projectState.value = ProjectState.Loading
                // 模拟网络延迟
                delay(1000)

                val start = (currentPage - 1) * pageSize
                val end = (start + pageSize).coerceAtMost(filteredProjects.size)

                if (start < end) {
                    displayedProjects.addAll(filteredProjects.subList(start, end))
                    _projectState.value = ProjectState.Success(displayedProjects)
                    currentPage++
                } else {
                    _projectState.value = ProjectState.Error("没有更多项目了")
                }
            } catch (e: Exception) {
                _projectState.value = ProjectState.Error("加载项目失败：${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    // 搜索项目
    fun searchProjects(name: String, location: String) {
        filteredProjects = allProjects.filter { project ->
            project.name.contains(name, ignoreCase = true) &&
                    project.location.contains(location, ignoreCase = true)
        }
        // 重置分页
        currentPage = 1
        // 重新加载数据
        displayedProjects.clear()
        loadProjects()
    }

    private fun generateDummyProjects(): List<Project> {
        return (1..50).map { i ->
            Project("项目 $i", "场地 $i", if (i % 2 == 0) "进行中" else "已完成")
        }
    }
}

