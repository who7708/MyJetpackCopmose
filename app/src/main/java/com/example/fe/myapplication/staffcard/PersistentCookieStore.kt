package com.example.fe.myapplication.staffcard

import android.content.Context
import okhttp3.Cookie
import okhttp3.HttpUrl

class PersistentCookieStore(ctx: Context) {

    private val ctx2: Context = ctx
    private val sharedPreferences =
        ctx.getSharedPreferences("cookie_store", Context.MODE_PRIVATE)

    fun add(url: HttpUrl, cookies: List<Cookie>) {
        val editor = sharedPreferences.edit()
        for (cookie in cookies) {
            editor.putString("$url-${cookie.name}", cookie.value)
        }
        editor.apply()
    }

    fun get(url: HttpUrl): List<Cookie> {
        val cookieList = mutableListOf<Cookie>()
        val sharedPreferences = sharedPreferences(url.toString())
        sharedPreferences.all.forEach { (key, value) ->
            val cookie = Cookie.parse(url, "$key=$value")
            if (cookie != null) {
                cookieList.add(cookie)
            }
        }
        return cookieList
    }

    private fun sharedPreferences(url: String): android.content.SharedPreferences {
        return ctx2.getSharedPreferences("cookie_store_$url", Context.MODE_PRIVATE)
    }
}