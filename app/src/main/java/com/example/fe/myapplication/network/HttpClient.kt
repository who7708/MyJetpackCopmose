package com.example.fe.myapplication.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * @author Chris
 * @version 1.0
 * @since 2024/09/14
 */
object HttpClient {

    private val retrofit: Retrofit
        get() = RetrofitFactory.instance.retrofit

    val okHttpClient: OkHttpClient
        get() = RetrofitFactory.instance.okHttpClient

    fun <T> create(clazz: Class<T>): T {
        return retrofit.create(clazz)
    }

}
