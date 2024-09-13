package com.example.fe.myapplication.network

import android.annotation.SuppressLint
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

/**
 * IP直连忽略host检测
 * @author Chris
 * @version 1.0
 * @since 2024/09/14
 */
class TrueHostVerifier() : HostnameVerifier {

    @SuppressLint("BadHostnameVerifier")
    override fun verify(hostname: String, session: SSLSession): Boolean {
        return true
    }
}