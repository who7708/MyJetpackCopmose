package com.example.fe.myapplication.common

import com.example.fe.myapplication.service.ProjectService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://your-api-url.com/" // 替换为你的API基础URL

    // 可选：调试网络请求的日志拦截器
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor) // 添加拦截器以记录请求日志
        .build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()) // 使用 Gson 解析 JSON
            .build()
    }

    // 实例化 ProjectService
    val projectService: ProjectService by lazy {
        retrofit.create(ProjectService::class.java)
    }
}
