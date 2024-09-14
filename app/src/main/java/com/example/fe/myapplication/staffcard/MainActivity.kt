package com.example.fe.myapplication.staffcard

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.fe.myapplication.R
import com.example.fe.myapplication.model.ApiResponse
import com.example.fe.myapplication.network.HttpClient
import com.google.gson.Gson
import retrofit2.Call

/**
 * @author Chris
 * @version 1.0
 * @since 2024/09/14
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val context = LocalContext.current
    val isLoggedIn = remember { mutableStateOf(false) }
    val query = remember { mutableStateOf("") }
    val apiService by lazy { HttpClient.create(AppApiService::class.java) }

    val searchResults = remember {
        mutableStateOf<List<SearchResult>?>(
            listOf(
                SearchResult(
                    "https://lf-flow-web-cdn.doubao.com/obj/flow-doubao/doubao/web/static/image/logo-icon-white-bg.f3acc228.png",
                    "110",
                    "张三"
                ),
                SearchResult(
                    "https://lf-flow-web-cdn.doubao.com/obj/flow-doubao/doubao/web/static/image/logo-icon-white-bg.f3acc228.png",
                    "110",
                    "张三"
                )
            )
        )
    }

    if (!isLoggedIn.value) {
        LoginScreen(context, apiService) { loggedIn ->
            isLoggedIn.value = loggedIn
        }
    } else {
        SearchScreen(context, apiService, query, searchResults)
    }
}

@Composable
fun LoginScreen(
    context: Context,
    apiService: AppApiService,
    onLoginSuccess: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF67B26F),
                        Color(0xFF4CAF50)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(32.dp)
                .clip(RoundedCornerShape(16.dp)),
            backgroundColor = Color.White
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Login",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(16.dp))
                val usernameState = remember { mutableStateOf("") }
                val passwordState = remember { mutableStateOf("") }
                OutlinedTextField(
                    value = usernameState.value,
                    onValueChange = { usernameState.value = it },
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = passwordState.value,
                    onValueChange = { passwordState.value = it },
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val request = LoginRequest()
                        request.username = usernameState.value
                        request.password = passwordState.value
                        apiService.login(request)
                            .enqueue(object : retrofit2.Callback<ApiResponse<LoginResponse>> {
                                override fun onResponse(
                                    call: Call<ApiResponse<LoginResponse>>,
                                    response: retrofit2.Response<ApiResponse<LoginResponse>>
                                ) {
                                    if (response.isSuccessful) {
                                        val loginResponse = response.body()
                                        val json = Gson().toJson(loginResponse)
                                        Log.e("apiService.login", "登录成功 $json")
                                        // 假设登录成功后设置登录状态为 true
                                        onLoginSuccess(true)
                                    } else {
                                        // 处理登录失败情况
                                        Log.e("apiService.login", "onFailure: 登录失败")
                                    }
                                }

                                override fun onFailure(
                                    call: Call<ApiResponse<LoginResponse>>,
                                    t: Throwable
                                ) {
                                    // 处理网络错误等情况
                                    Log.e("apiService.search", "onFailure: 登录失败", t)
                                }
                            })
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Login")
                }
            }
        }
    }
}

@Composable
fun SearchScreen(
    context: Context,
    apiService: AppApiService,
    query: MutableState<String>,
    searchResults: MutableState<List<SearchResult>?>,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF8198C7),
                        Color(0xFF4C6DAF)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                backgroundColor = Color.White
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = query.value,
                        onValueChange = { query.value = it },
                        label = { Text("Search by name or work ID") },
                        modifier = Modifier
                            .weight(1f)
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            apiService.search(query.value)
                                .enqueue(object :
                                    retrofit2.Callback<ApiResponse<List<SearchResult>>> {
                                    override fun onResponse(
                                        call: Call<ApiResponse<List<SearchResult>>>,
                                        response: retrofit2.Response<ApiResponse<List<SearchResult>>>
                                    ) {
                                        val res = response.body()!!
                                        if (response.isSuccessful && res.isSuccess) {
                                            searchResults.value = res.data
                                        } else {
                                            // Handle error
                                            Log.e(
                                                "apiService.search",
                                                "onFailure: 搜索失败: ${res.comments}"
                                            )
                                            Toast.makeText(
                                                context,
                                                "搜索失败${res.comments}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<ApiResponse<List<SearchResult>>>,
                                        t: Throwable
                                    ) {
                                        // Handle error
                                        Log.e("apiService.search", "onFailure: 搜索失败", t)
                                        Toast.makeText(
                                            context,
                                            "搜索失败" + t.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })
                        }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = null
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (searchResults.value != null) {
                LazyColumn {
                    items(searchResults.value!!) { result ->
                        SearchResultCard(result)
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultCard(result: SearchResult) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = result.picUrl ?: "",
                contentDescription = null,
                placeholder = painterResource(R.drawable.placeholder_image),
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = result.staffCardNo, fontWeight = FontWeight.Bold)
                Text(text = result.realName)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp()
}