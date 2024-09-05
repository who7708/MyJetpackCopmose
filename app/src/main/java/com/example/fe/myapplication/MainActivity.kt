package com.example.fe.myapplication


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.fe.myapplication.model.Project
import com.example.fe.myapplication.modelview.ProjectViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen()
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SideMenu(navController: NavHostController) {
    Column {
        Text("菜单", modifier = Modifier.padding(16.dp))
        Spacer(modifier = Modifier.height(8.dp))
        ListItem(text = { Text("项目 1") }, modifier = Modifier.clickable {
            navController.navigate("project1")
        })
        ListItem(text = { Text("项目 2") }, modifier = Modifier.clickable {
            // 处理项目 2 的导航逻辑
        })
        ListItem(text = { Text("项目 3") }, modifier = Modifier.clickable {
            // 处理项目 3 的导航逻辑
        })
    }
}

@Composable
fun MainScreen() {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    val drawerItems = listOf("项目1", "项目2", "项目3")

    Scaffold(scaffoldState = scaffoldState, drawerContent = {
        Column {
            drawerItems.forEach { item ->
                Text(text = item, modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        when (item) {
                            "项目1" -> navController.navigate("projectList")
                            "项目2" -> { /* 项目2跳转逻辑 */
                            }

                            "项目3" -> { /* 项目3跳转逻辑 */
                            }
                        }
                        scope.launch { scaffoldState.drawerState.close() }
                    }
                    .padding(16.dp))
            }
        }
    }) {
        it.calculateTopPadding()
        NavHost(navController, startDestination = "projectList") {
            composable("projectList") { ProjectListScreen(navController) }
            composable("projectDetail/{projectName}") { backStackEntry ->
                ProjectDetailScreen(backStackEntry.arguments?.getString("projectName") ?: "")
            }
        }
    }
}

@Composable
fun ProjectListScreen(
    navController: NavHostController, projectViewModel: ProjectViewModel = viewModel()
) {
    val projectStream = projectViewModel.getProjectStream("", "")
    val pagingData: LazyPagingItems<Project> = projectStream.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(pagingData) { project ->
            project?.let {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("projectDetail/${project.name}") }
                    .padding(16.dp)) {
                    Text(text = project.name, modifier = Modifier.weight(1f))
                    Text(text = project.location, modifier = Modifier.weight(1f))
                    Text(text = project.status, modifier = Modifier.weight(1f))
                }
            }
        }

        // 显示加载状态或错误信息
        pagingData.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }

                loadState.append is LoadState.Loading -> {
                    item {
                        CircularProgressIndicator(
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }

                loadState.refresh is LoadState.Error -> {
                    val error = pagingData.loadState.refresh as LoadState.Error
                    item {
                        Text(text = "加载失败: ${error.error.localizedMessage}")
                    }
                }

                loadState.append is LoadState.Error -> {
                    val error = pagingData.loadState.append as LoadState.Error
                    item {
                        Text(text = "加载更多失败: ${error.error.localizedMessage}")
                    }
                }
            }
        }
    }
}


@Composable
fun ProjectItem(
    navController: NavHostController, project: Project
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { navController.navigate("projectDetail/${project.name}") }
        .padding(16.dp)) {
        Text(text = project.name, modifier = Modifier.weight(1f))
        Text(text = project.location, modifier = Modifier.weight(1f))
        Text(text = project.status, modifier = Modifier.weight(1f))
    }
}


@Composable
fun ProjectDetailScreen(projectName: String) {
    // 项目详情页面
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("项目名称: $projectName")
        // 根据 projectName 展示项目的其他详细信息
        Text("场地: 场地1")
        Text("状态: 进行中")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainScreen()
}
