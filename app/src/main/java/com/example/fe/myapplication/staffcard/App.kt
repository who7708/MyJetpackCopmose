package com.example.fe.myapplication.staffcard

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.example.fe.myapplication.network.BaseApp
import com.example.fe.myapplication.network.LogUtil
import com.facebook.drawee.backends.pipeline.Fresco

/**
 * @Author zhiqiang
 * @Date 2019-06-27
 * @Email liuzhiqiang@moretickets.com
 * @Description
 */
class App : MultiDexApplication(), BaseApp {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        BaseApp.initApplication(this)
        LogUtil.enable = BaseApp.isDebug()
        // 初始化日志类
        // Logx.init(this, PATH)
        initToast()
        initCrashHandler()
        initRxJavaPluginsError()
        // Facebook 的开源图片加载库 Fresco，作用：
        // Fresco 主要用于高效地加载和显示图片，它提供了以下一些优势：
        // 处理复杂的图片显示需求，如渐进式 JPEG 加载、从网络加载大图片等。
        // 内存管理优化，减少因图片加载导致的内存问题。
        Fresco.initialize(this)
    }

    private fun initRxJavaPluginsError() {
        // RxJavaPlugins.setErrorHandler { it ->
        //     Logx.e("App RxJavaPlugins ", it.toString())
        // }
    }

    private fun initCrashHandler() {
        // AppCrashHandler.instance.init()
    }

    private fun initToast() {
        // ToastUtil.init(this)
    }


    companion object {
        private const val PATH = "log"
    }
}
