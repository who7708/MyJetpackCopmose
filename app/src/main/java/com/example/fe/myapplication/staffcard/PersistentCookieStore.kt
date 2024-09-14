package com.example.fe.myapplication.staffcard

import android.content.Context
import okhttp3.Cookie
import okhttp3.HttpUrl
import java.security.MessageDigest

class PersistentCookieStore(ctx: Context) {

    private val ctx2: Context = ctx

    fun addCookie(url: HttpUrl, cookies: List<Cookie>) {
        val sharedPreferences = sharedPreferences()
        val editor = sharedPreferences.edit()
        val hashedUrl = hashUrl(url.toString())
        for (cookie in cookies) {
            editor.putString("${hashedUrl}-${cookie.name}", cookie.value)
        }
        editor.apply()
    }

    fun getCookie(url: HttpUrl): List<Cookie> {
        val cookieList = mutableListOf<Cookie>()
        val sharedPreferences = sharedPreferences()
        val hashedUrl = hashUrl(url.toString())
        sharedPreferences.all.forEach { (key, value) ->
            val cookie = Cookie.parse(url, "${key.replace("${hashedUrl}-", "")}=$value")
            if (cookie != null) {
                cookieList.add(cookie)
            }
        }
        return cookieList
    }

    private fun sharedPreferences(): android.content.SharedPreferences {
        // return ctx2.getSharedPreferences("cookie_store_$urlMd5", Context.MODE_PRIVATE)
        return ctx2.getSharedPreferences("cookie_store", Context.MODE_PRIVATE)
    }

    private fun hashUrl(url: String): String {
        val md = MessageDigest.getInstance("MD5")
        val bytes = md.digest(url.encodeToByteArray())
        val sb = StringBuilder()
        for (b in bytes) {
            sb.append(Integer.toHexString(b.toInt() and 0xff))
        }
        return sb.toString()
    }
}

