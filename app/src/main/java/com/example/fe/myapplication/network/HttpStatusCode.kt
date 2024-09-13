package com.example.fe.myapplication.network

/**
 * 网络请求错误码
 * @author Chris
 * @version 1.0
 * @since 2024/09/14
 */
object HttpStatusCode {

    val NETWORK_EXCEPTION = -1000

    /**
     * 网络请求成功
     */
    val SUCCESS_OK = 200

    /**
     *  未知的设备型号   （注册/更新时可能发生）
     */
    val RGISTER_EXCEPTION = 10001

    /**
     * 未知的设备序列号   (设备信息获取时)
     */
    val SN_EXCEPTION = 10002

    /**
     * 人脸库比对失败
     */
    val FACE_COMPARE_EXCEPTION = 999

}