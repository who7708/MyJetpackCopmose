package com.example.fe.myapplication.network

import java.io.IOException
import java.net.URL
import java.util.Enumeration

/**
 * @author Chris
 * @version 1.0
 * @since 2024/09/14
 */
internal class Load {

    var loader: ClassLoader? = null
        private set
    private var configs: Enumeration<URL>? = null

    init {
        // 初始化加载器
        val cl = Thread.currentThread().contextClassLoader
        if (null != cl) {
            loader = cl
        } else {
            loader = ClassLoader.getSystemClassLoader()
        }
    }

    // 获取URL
    @Throws(Exception::class)
    fun initLoad(location: String): URL {
        if (configs == null) {
            try {
                if (loader == null) {
                    configs = ClassLoader.getSystemResources(location)
                } else {
                    configs = loader!!.getResources(location)
                }
            } catch (x: IOException) {
                x.printStackTrace()
            }

        }
        return configs!!.nextElement()
    }
}