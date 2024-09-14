package com.example.fe.myapplication.staffcard

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.fe.myapplication.R
import com.example.fe.myapplication.model.ApiResponse
import com.example.fe.myapplication.network.HttpClient
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import retrofit2.Call
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

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
    val cardNo = remember { mutableStateOf("") }
    val realName = remember { mutableStateOf("") }
    val idNo = remember { mutableStateOf("") }
    val cellphone = remember { mutableStateOf("") }
    val staffCardSearchParam = remember { mutableStateOf(StaffCardSearchParam()) }
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
        SearchScreen(context, apiService, staffCardSearchParam, searchResults)
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
                    text = "登录",
                    style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(16.dp))
                val usernameState = remember { mutableStateOf("") }
                val passwordState = remember { mutableStateOf("") }
                OutlinedTextField(
                    value = usernameState.value,
                    onValueChange = { usernameState.value = it },
                    label = { Text("手机号") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = passwordState.value,
                    onValueChange = { passwordState.value = it },
                    label = { Text("密码") },
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
                    Text(text = "登录")
                }
            }
        }
    }
}

@Composable
fun SearchScreen(
    context: Context,
    apiService: AppApiService,
    staffCardSearchParam: MutableState<StaffCardSearchParam>,
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
                Column(
                    // verticalAlignment = Alignment.CenterVertically
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = staffCardSearchParam.value.cardNo ?: "",
                        onValueChange = { staffCardSearchParam.value.cardNo = it },
                        label = { Text("工作证号") },
                        // singleLine = true,
                        modifier = Modifier
                            // .weight(1f)
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = staffCardSearchParam.value.realName ?: "",
                        onValueChange = { staffCardSearchParam.value.realName = it },
                        label = { Text("姓名") },
                        // singleLine = true,
                        modifier = Modifier
                            // .weight(1f)
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = staffCardSearchParam.value.idNo ?: "",
                        onValueChange = { staffCardSearchParam.value.idNo = it },
                        label = { Text("身份证号") },
                        // singleLine = true,
                        modifier = Modifier
                            // .weight(1f)
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = staffCardSearchParam.value.cellphone ?: "",
                        onValueChange = { staffCardSearchParam.value.cellphone = it },
                        label = { Text("手机号") },
                        // singleLine = true,
                        modifier = Modifier
                            // .weight(1f)
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            apiService.acStaffCardsQuery(
                                acShowId = "",
                                realName = query.value,
                            )
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
    val context = LocalContext.current
    val rotationAngle by remember { mutableFloatStateOf(0f) }
    var showDialog by remember { mutableStateOf(false) }
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
            // if (result.picUrl != null) {
            //     runBlocking {
            //         val inputStream = bitmapToInputStream(result.picUrl)
            //         val exif = ExifInterface(inputStream)
            //         val orientation = exif.getAttributeInt(
            //             ExifInterface.TAG_ORIENTATION,
            //             ExifInterface.ORIENTATION_NORMAL
            //         )
            //         rotationAngle = when (orientation) {
            //             ExifInterface.ORIENTATION_ROTATE_90 -> 90f
            //             ExifInterface.ORIENTATION_ROTATE_180 -> 180f
            //             ExifInterface.ORIENTATION_ROTATE_270 -> 270f
            //             else -> 0f
            //         }
            //     }
            // }
            val picUrl = result.picUrl ?: ""
            AsyncImage(
                model = picUrl,
                contentDescription = null,
                placeholder = painterResource(R.drawable.placeholder_image),
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .graphicsLayer {
                        rotationZ = rotationAngle
                    }
                    .clickable { showDialog = true },
                contentScale = ContentScale.Crop
            )

            if (showDialog && picUrl.isNotBlank()) {
                Dialog(onDismissRequest = { showDialog = false }) {
                    Image(
                        painter = rememberAsyncImagePainter(picUrl),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = result.cardNo, fontWeight = FontWeight.Bold)
                Text(text = result.realName)
            }
        }
    }
}

fun bitmapToInputStream(url: String): InputStream {
    val bitmap = flipImageHorizontally(url)
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    return ByteArrayInputStream(outputStream.toByteArray())
}

private fun flipImageHorizontally(url: String): Bitmap {
    return runBlocking {
        val inputStream = convertNetworkImageToInputStream(url)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val matrix = Matrix()
        matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}

suspend fun convertNetworkImageToInputStream(imageUrl: String): InputStream? {
    return try {
        withContext(Dispatchers.IO) {
            URL(imageUrl).openStream()
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

fun downloadImage(imageUrl: String): File? {
    return try {
        val url = URL(imageUrl)
        val connection = url.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        val inputStream: InputStream = connection.inputStream
        val tempFile = File.createTempFile("image", ".jpg", null)
        val outputStream = FileOutputStream(tempFile)
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream.write(buffer, 0, bytesRead)
        }
        outputStream.close()
        inputStream.close()
        tempFile
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp()
}