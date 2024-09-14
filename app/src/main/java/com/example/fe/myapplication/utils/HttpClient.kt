package com.example.fe.myapplication.utils

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Chris
 * @version 1.0
 * @since 2024/09/14
 */
class HttpClient {
    // private val apiService by lazy { HttpClient.create(AppApiService::class.java) }

    // kotlin.runCatching {
    //     singleCycleStartTime = System.currentTimeMillis()
    //     val res: ApiResponseExt<List<OfflineVerify>> = apiService.pullOfflineVerify(
    //         acShowId,
    //         updateTime,
    //         acItemIdFrom,
    //         notQueryCount,
    //         page.length,
    //         page.offset
    //     )
    //     return@runCatching res
    // }.onSuccess {
    //     if (it.isSuccess) {
    //         // 异步任务执行
    //         limitedConcurrentExecutor.submitTask {
    //             insertOfflineVerifyTable(it.data)
    //         }
    //         // ioScope.launch {
    //         //     insertOfflineVerifyTable(it.data)
    //         // }
    //         updateProgress(it, listener)
    //         // 更新请求参数
    //         notQueryCount = true
    //         acItemIdFrom = getTicketStartId(it.data)
    //         result = it.data
    //     } else {
    //         result = null
    //         exception = SyncException("拉取票码数据失败：${it.comments}")
    //         Logx.e(TAG, "拉取票码数据失败：${it.comments}")
    //     }
    // }.onFailure {
    //     result = null
    //     exception = SyncException("拉取票码数据失败：${it.message}")
    //     Logx.e(TAG, "拉取票码数据失败：${it.message}")
    // }
    companion object {
        // 构建Retrofit实例
        private val retrofit: Retrofit = Retrofit.Builder()
            // 设置网络请求BaseUrl地址
            // .baseUrl("https://api.aaa.com/")
            .baseUrl("http://192.168.1.4:30000/fhl_acsapi/")
            // 设置数据解析器
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fun <T> create(clazz: Class<T>): T {
            return retrofit.create(clazz)
        }
    }

}
