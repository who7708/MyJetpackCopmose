// RetrofitInstance.kt
package com.example.fe.myapplication.test1.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    // 替换为实际的 API 基础 URL
    // private const val BASE_URL = "https://your-api-url.com/"
    private const val BASE_URL = "http://192.168.100.47:5000/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val projectService: ProjectService by lazy {
        retrofit.create(ProjectService::class.java)
    }
}