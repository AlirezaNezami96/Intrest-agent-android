package com.mindlab.intrest_agent_android.data.network.api

import com.mindlab.intrest_agent_android.data.network.body.SettingsBody
import com.mindlab.intrest_agent_android.data.network.response.SettingsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

/**
 * Created by Alireza Nezami on 12/13/2021.
 */
interface SettingsApi {

    @PATCH("/api/restaurants/{id}")
    suspend fun changeReceivingOrder(
        @Path("id") id: Int,
        @Body body: SettingsBody
    ): Response<SettingsResponse>

    @GET("/api/restaurants/{id}")
    suspend fun getSettings(
        @Path("id") id: Int,
    ): Response<SettingsResponse>

}