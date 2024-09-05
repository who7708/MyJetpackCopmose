package com.example.fe.myapplication.modelview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Project(val name: String, val location: String, val status: String)

sealed class ProjectUiState {
    object Loading : ProjectUiState()
    data class Success(val projects: List<Project>) : ProjectUiState()
    data class Error(val message: String) : ProjectUiState()
}

class ProjectViewModel : ViewModel() {
    private val _projectList = MutableStateFlow<List<Project>>(emptyList())
    private val _uiState = MutableStateFlow<ProjectUiState>(ProjectUiState.Loading)

    // 用来管理分页数据
    private var currentPage = 1
    private val pageSize = 10
    private var isLoadingMore = false

    // 外部只读的 StateFlow
    val uiState: StateFlow<ProjectUiState> get() = _uiState

    init {
        // 初始化加载第一页数据
        loadProjects()
    }

    // 模拟项目数据加载
    fun loadProjects() {
        viewModelScope.launch {
            _uiState.value = ProjectUiState.Loading
            try {
                val projects = fetchProjects(currentPage, pageSize)
                _projectList.value += projects // 加载更多数据
                _uiState.value = ProjectUiState.Success(_projectList.value)
                currentPage++ // 下一页
            } catch (e: Exception) {
                _uiState.value = ProjectUiState.Error("加载项目失败: ${e.message}")
            }
        }
    }

    // 模拟分页数据
    private suspend fun fetchProjects(page: Int, size: Int): List<Project> {
        // 模拟网络延迟
        delay(2000)
        if (page > 3) throw Exception("没有更多数据了") // 模拟分页结束
        return List(size) { index ->
            Project("项目${(page - 1) * size + index + 1}", "场地${index + 1}", "状态${index % 3}")
        }
    }

    // 搜索项目
    fun searchProjects(name: String, location: String) {
        viewModelScope.launch {
            val filtered = _projectList.value.filter { project ->
                project.name.contains(name, ignoreCase = true) &&
                        project.location.contains(location, ignoreCase = true)
            }
            _uiState.value = ProjectUiState.Success(filtered)
        }
    }
}

