package com.example.fe.myapplication.staffcard

import com.example.fe.myapplication.model.ApiResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * @author Chris
 * @version 1.0
 * @since 2024/09/14
 */
interface AppApiService {
    @POST("/pub/login")
    // fun login(
    //     @Query("username") username: String,
    //     @Query("password") password: String
    // ): ApiResponse<Boolean>
    fun login(@Body request: LoginRequest): Call<ApiResponse<LoginResponse>>


    @GET("/backend/v1/cyy/ac_staff_cards")
    fun search(@Query("query") query: String): Call<ApiResponse<List<SearchResult>>>
}