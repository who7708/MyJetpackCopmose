// ProjectListScreen.kt
package com.example.fe.myapplication.test1.ui


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.fe.myapplication.test1.data.ProjectDatabase
import com.example.fe.myapplication.test1.model.Project
import com.example.fe.myapplication.test1.network.RetrofitInstance
import com.example.fe.myapplication.test1.repository.ProjectRepository
import com.example.fe.myapplication.test1.viewmodel.ProjectViewModel
import com.example.fe.myapplication.test1.viewmodel.ProjectViewModelFactory

@Composable
fun ProjectListScreen(navController: NavHostController) {
    // 获取上下文
    val context = LocalContext.current
    // 初始化数据库和仓库
    val database = ProjectDatabase.getDatabase(context)
    val repository = ProjectRepository(database, RetrofitInstance.projectService)
    // 创建 ViewModel
    val viewModel: ProjectViewModel = viewModel(
        factory = ProjectViewModelFactory(repository)
    )

    var searchName by remember { mutableStateOf("") }
    var searchLocation by remember { mutableStateOf("") }

    // 获取分页数据
    val pagingData: LazyPagingItems<Project> =
        viewModel.getProjects(searchName, searchLocation).collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("项目管理") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // 搜索栏
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchName,
                    onValueChange = { searchName = it },
                    label = { Text("项目名称") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = searchLocation,
                    onValueChange = { searchLocation = it },
                    label = { Text("场地") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { pagingData.refresh() },
                    modifier = Modifier
                        .height(56.dp)
                ) {
                    Text("查询")
                }
            }

            // 项目列表
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                // items(pagingData) { project ->
                items(pagingData.itemCount) { index ->
                    val project = pagingData[index]
                    project?.let {
                        ProjectListItem(project = it, onItemClick = {
                            navController.navigate("projectDetail/${it.id}")
                        })
                    }
                }

                // 加载状态
                pagingData.apply {
                    when {
                        loadState.refresh is androidx.paging.LoadState.Loading -> {
                            item {
                                LoadingItem()
                            }
                        }

                        loadState.append is androidx.paging.LoadState.Loading -> {
                            item {
                                LoadingItem()
                            }
                        }

                        loadState.refresh is androidx.paging.LoadState.Error -> {
                            val e = pagingData.loadState.refresh as androidx.paging.LoadState.Error
                            item {
                                ErrorItem(
                                    message = e.error.localizedMessage ?: "Unknown Error",
                                    onRetry = { pagingData.retry() })
                            }
                        }

                        loadState.append is androidx.paging.LoadState.Error -> {
                            val e = pagingData.loadState.append as androidx.paging.LoadState.Error
                            item {
                                ErrorItem(
                                    message = e.error.localizedMessage ?: "Unknown Error",
                                    onRetry = { pagingData.retry() })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProjectListItem(project: Project, onItemClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onItemClick() },
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = project.name, style = MaterialTheme.typography.subtitle1)
                Text(text = project.location, style = MaterialTheme.typography.body2)
            }
            Text(text = project.status, style = MaterialTheme.typography.body2)
        }
    }
}

@Composable
fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorItem(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, color = MaterialTheme.colors.error)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("重试")
        }
    }
}
