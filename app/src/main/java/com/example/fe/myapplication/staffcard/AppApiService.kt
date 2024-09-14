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


    // /**
    //  * @param acShowId 核验项目id
    //  * @param cardNo 工作证号
    //  * @param realName 姓名
    //  * @param idNo 证件号
    //  * @param cellphone 手机号
    //  */
    // @GET("/backend/v1/cyy/ac_staff_cards")
    // fun acStaffCardsQuery(@Query("acShowId") acShowId: String): Call<ApiResponse<List<SearchResult>>>

    /**
     * @param acShowId 核验项目id
     * @param cardNo 工作证号
     * @param realName 姓名
     * @param idNo 证件号
     * @param cellphone 手机号
     */
    @GET("/backend/v1/cyy/ac_staff_cards")
    fun acStaffCardsQuery(
        @Query(value = "acShowId") acShowId: String,
        @Query(value = "cardNo") cardNo: String? = null,
        @Query(value = "realName") realName: String? = null,
        @Query(value = "idNo") idNo: String? = null,
        @Query(value = "cellphone") cellphone: String? = null,
    ): Call<ApiResponse<List<SearchResult>>>
}