package com.example.fe.myapplication.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * @author Chris
 * @version 1.0
 * @since 2024/09/14
 */
internal class RetrofitFactory private constructor() {

    private var mOkHttpClient: OkHttpClient? = null
    private var mRetrofit: Retrofit? = null
    private val appEnvironment: IAppEnvironment = EnvironmentManager.environment

    val retrofit: Retrofit
        get() {
            if (null == mRetrofit) {
                mRetrofit = appEnvironment.retrofit
            }
            return mRetrofit!!
        }

    val okHttpClient: OkHttpClient
        get() = provideOkHttpClient()

    private object RetrofitManagerHelper {
        val INSTANCE = RetrofitFactory()
    }

    private fun provideOkHttpClient(): OkHttpClient {
        if (null == mOkHttpClient) {
            mOkHttpClient = appEnvironment.okHttpClient
        }
        return mOkHttpClient!!
    }

    companion object {

        val instance: RetrofitFactory
            get() = RetrofitManagerHelper.INSTANCE
    }


}
