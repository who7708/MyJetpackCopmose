package com.example.fe.myapplication.staffcard

data class LoginResponse(
    val userName: String,
    val loginTime: Long = 0,
    val merchantId: String? = null
)