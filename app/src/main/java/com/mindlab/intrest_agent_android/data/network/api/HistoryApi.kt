package com.mindlab.intrest_agent_android.data.network.api

import com.mindlab.intrest_agent_android.data.network.response.IncomingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by Alireza Nezami on 12/13/2021.
 */
interface HistoryApi {

    @GET("/api/orders/restaurant/{id}")
    suspend fun getHistory(
        @Path("id") id: Int
    ): Response<IncomingResponse>


}