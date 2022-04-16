package com.mindlab.intrest_agent_android.data.network.api

import com.mindlab.intrest_agent_android.data.network.body.FoodAvailabilityBody
import com.mindlab.intrest_agent_android.data.network.body.FoodPriceBody
import com.mindlab.intrest_agent_android.data.network.body.FoodTimeBody
import com.mindlab.intrest_agent_android.data.network.response.ChangeFoodDetailResponse
import com.mindlab.intrest_agent_android.data.network.response.FoodEdibleResponse
import com.mindlab.intrest_agent_android.data.network.response.MenuResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

/**
 * Created by Alireza Nezami on 12/13/2021.
 */
interface MenuApi {

    @GET("/api/menus/findAll/{id}")
    suspend fun getIncomingPaging(
        @Path("id") id: Int
    ): Response<List<MenuResponse>>

    @PATCH("/api/foodPrices/{id}")
    suspend fun changePrice(
        @Path("id") id: Int,
        @Body body:FoodPriceBody
    ): Response<ChangeFoodDetailResponse>

    @PATCH("/api/edibles/{id}")
    suspend fun changeTime(
        @Path("id") id: Int,
        @Body body: FoodTimeBody
    ): Response<FoodEdibleResponse>

    @PATCH("/api/edibles/{id}")
    suspend fun changeAvailability(
        @Path("id") id: Int,
        @Body body: FoodAvailabilityBody
    ): Response<FoodEdibleResponse>


}