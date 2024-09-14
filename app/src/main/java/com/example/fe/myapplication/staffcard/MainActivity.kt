package com.example.fe.myapplication.staffcard

import android.os.Bundle
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.fe.myapplication.R
import com.example.fe.myapplication.network.HttpClient
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
        LoginScreen { loggedIn ->
            isLoggedIn.value = loggedIn
        }
    } else {
        val apiService by lazy { HttpClient.create(AppApiService::class.java) }
        SearchScreen(query, searchResults, apiService)
    }
}

@Composable
fun LoginScreen(onLoginSuccess: (Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF6798B2),
                        Color(0xFF4CAFA3)
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
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Username") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Password") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        onLoginSuccess(true)
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
    query: MutableState<String>,
    searchResults: MutableState<List<SearchResult>?>,
    apiService: AppApiService
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
                                .enqueue(object : retrofit2.Callback<List<SearchResult>> {
                                    override fun onResponse(
                                        call: Call<List<SearchResult>>,
                                        response: retrofit2.Response<List<SearchResult>>
                                    ) {
                                        if (response.isSuccessful) {
                                            searchResults.value = response.body()
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<List<SearchResult>>,
                                        t: Throwable
                                    ) {
                                        // Handle error
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