package com.example.fe.myapplication

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

data class ProjectItem(
    val id: Int,
    val name: String
)

@Composable
fun ProjectListScreen() {
    val searchQuery by remember { mutableStateOf("") }
    val projectFlow: Flow<PagingData<ProjectItem>> = getProjectFlow(searchQuery)
    val projectItems: LazyPagingItems<ProjectItem> = projectFlow.collectAsLazyPagingItems()

    Column {
        // 搜索栏
        // TODO: 添加实际的搜索栏组件并 update searchQuery when changed
        Text("Search Query: $searchQuery")

        when (val loadState = projectItems.loadState.refresh) {
            is LoadState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }

            is LoadState.Error -> {
                Text("Error: ${loadState.error.message}")
            }

            else -> {
                LazyColumn {
                    items(projectItems) { item ->
                        if (item != null) {
                            Text(text = item.name, modifier = Modifier.padding(8.dp))
                        }
                    }
                }
            }
        }
    }
}

fun getProjectFlow(searchQuery: String): Flow<PagingData<ProjectItem>> {
    return flow {
        // 模拟分页数据加载，这里可以根据实际情况从数据源加载数据
        val items = mutableListOf<ProjectItem>()
        for (i in 1..100) {
            items.add(ProjectItem(i, "Project $i"))
        }
        emit(PagingData.from(items))
    }
}