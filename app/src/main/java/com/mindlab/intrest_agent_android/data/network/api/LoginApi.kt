package com.mindlab.intrest_agent_android.data.network.api

import com.mindlab.intrest_agent_android.data.network.body.LoginBody
import com.mindlab.intrest_agent_android.data.network.body.RefreshTokenBody
import com.mindlab.intrest_agent_android.data.network.response.LoginResponse
import com.mindlab.intrest_agent_android.data.network.response.ResturantLogoResponse
import com.mindlab.intrest_agent_android.data.network.response.UserResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Alireza Nezami on 12/13/2021.
 */
interface LoginApi {

    @Headers("$CUSTOM_HEADER: $NO_AUTH")
    @POST("api/login")
    suspend fun login(@Body loginBody: LoginBody): Response<LoginResponse>

    @Headers("$CUSTOM_HEADER: $NO_AUTH")
    @POST("api/token/refresh")
    suspend fun refreshToken(@Body() refreshToken: RefreshTokenBody): Response<LoginResponse>

    @GET("api/users/currentUser")
    suspend fun currentUser(
    ): Response<UserResponse>


//    @Headers("$CUSTOM_HEADER: $WITH_ID")
    @GET(
        "/api/restaurantLogoes/search/findAllByRestaurantLogoStatusIsAndRestaurantIdIs?" +
                "status=ACCEPTED"
    )
    suspend fun getLogo(
    @Query("restaurantId") id: Int
    ): Response<ResturantLogoResponse>


    companion object Factory {
        const val CUSTOM_HEADER = "@"
        const val NO_AUTH = "NoAuth"
    }
}