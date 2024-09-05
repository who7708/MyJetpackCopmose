package com.example.fe.myapplication


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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

@Composable
fun MainScreen() {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    val drawerItems = listOf("项目1", "项目2", "项目3")

    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            Column {
                drawerItems.forEach { item ->
                    Text(
                        text = item,
                        modifier = Modifier
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
                            .padding(16.dp)
                    )
                }
            }
        }
    ) {
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
    navController: NavHostController,
    projectViewModel: ProjectViewModel = viewModel()
) {
    var searchName by remember { mutableStateOf("") }
    var searchLocation by remember { mutableStateOf("") }
    val projectList by projectViewModel.projectList.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 搜索栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            BasicTextField(
                value = searchName,
                onValueChange = { searchName = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                decorationBox = { innerTextField ->
                    Box(Modifier.padding(8.dp)) {
                        if (searchName.isEmpty()) Text("项目名称")
                        innerTextField()
                    }
                }
            )
            BasicTextField(
                value = searchLocation,
                onValueChange = { searchLocation = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                decorationBox = { innerTextField ->
                    Box(Modifier.padding(8.dp)) {
                        if (searchLocation.isEmpty()) Text("场地")
                        innerTextField()
                    }
                }
            )
            Button(onClick = {
                projectViewModel.searchProjects(searchName, searchLocation)
            }) {
                Text("查询")
            }
        }

        // 项目列表
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(projectList) { project ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("projectDetail/${project.name}") }
                        .padding(16.dp)
                ) {
                    Text(text = project.name, modifier = Modifier.weight(1f))
                    Text(text = project.location, modifier = Modifier.weight(1f))
                    Text(text = project.status, modifier = Modifier.weight(1f))
                }
            }
        }
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
