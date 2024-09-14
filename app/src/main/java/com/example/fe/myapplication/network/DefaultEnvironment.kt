package com.example.fe.myapplication.network

import com.example.fe.myapplication.BuildConfig
import okhttp3.Cache
import okhttp3.ConnectionSpec
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.KeyStore
import java.util.concurrent.TimeUnit

/**
 * @author Chris
 * @version 1.0
 * @since 2024/09/14
 */
open class DefaultEnvironment : IAppEnvironment {

    override val retrofit: Retrofit
        get() {
            val builder = Retrofit.Builder()
                .baseUrl(apiBaseServiceUrl)
                .client(okHttpClient)
                .addConverterFactory(converterFactory)
            if (callAdapterFactory != null) {
                builder.addCallAdapterFactory(callAdapterFactory!!)
            }
            return builder.build()
        }

    override val okHttpClient: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
            builder.connectTimeout(DEFAULT_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(DEFAULT_READ_TIME_OUT.toLong(), TimeUnit.SECONDS)
                // IP直连
                .hostnameVerifier(TrueHostVerifier())
                .cache(cache)
                .connectionSpecs(
                    listOf(
                        ConnectionSpec.MODERN_TLS,
                        ConnectionSpec.COMPATIBLE_TLS,
                        ConnectionSpec.CLEARTEXT
                    )
                )

            for (interceptor in interceptors) {
                builder.addInterceptor(interceptor)
            }
            for (interceptor in networkInterceptors) {
                builder.addNetworkInterceptor(interceptor)
            }

            if (LogUtil.enable) {
                var ssl: SSLSocketFactoryImp? = null
                try {
                    ssl = SSLSocketFactoryImp(KeyStore.getInstance(KeyStore.getDefaultType()))
                } catch (e: Exception) {
                    LogUtil.d("SSLSocketFactory", "ssl:" + e.message)
                }

                if (null != ssl) {
                    builder.sslSocketFactory(ssl.sslContext.socketFactory, ssl.getTrustManager())
                }
            }

            return builder.build()
        }


    override val apiBaseServiceUrl: String
        get() = "http://192.168.1.4:30000"

    override val converterFactory: Converter.Factory
        get() = GsonConverterFactory.create()

    override val callAdapterFactory: CallAdapter.Factory? = null


    override val interceptors: MutableList<Interceptor>
        get() {
            val interceptors = ArrayList<Interceptor>()
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                interceptors.add(logging)
            }
            return interceptors
        }

    override val networkInterceptors: MutableList<Interceptor>
        get() {
            return ArrayList()
        }

    override val cache: Cache
        get() = Cache(BaseApp.application.cacheDir, (10240 * 1024).toLong())

    companion object {
        /**
         * 连接超时时间
         */
        private const val DEFAULT_TIME_OUT = 10

        /**
         * 读操作超时时间
         */
        private const val DEFAULT_READ_TIME_OUT = 5
    }


}
