package com.example.fe.myapplication.modelview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class Project(val name: String, val location: String, val status: String)

class ProjectViewModel : ViewModel() {
    // 内部可修改的 MutableLiveData
    private val _projectList = MutableLiveData<List<Project>>()
    private val _filteredProjects = MutableLiveData<List<Project>>()

    // 外部只读的 LiveData
    val projectList: LiveData<List<Project>> get() = _filteredProjects

    init {
        // 初始化项目数据
        _projectList.value = listOf(
            Project("项目A", "场地1", "进行中"),
            Project("项目B", "场地2", "已完成"),
            Project("项目C", "场地3", "未开始")
        )
        // 初始时显示所有项目
        _filteredProjects.value = _projectList.value
    }

    // 搜索项目
    fun searchProjects(name: String, location: String) {
        val filtered = _projectList.value?.filter { project ->
            project.name.contains(name, ignoreCase = true) &&
                    project.location.contains(location, ignoreCase = true)
        } ?: emptyList()
        _filteredProjects.value = filtered
    }
}
