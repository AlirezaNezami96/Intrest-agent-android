package com.mindlab.intrest_agent_android.data.network.api

import com.mindlab.intrest_agent_android.data.network.body.AdjustPriceBody
import com.mindlab.intrest_agent_android.data.network.body.DelayOrderBody
import com.mindlab.intrest_agent_android.data.network.body.EditDeliverCostBody
import com.mindlab.intrest_agent_android.data.network.body.OrderStatusBody
import com.mindlab.intrest_agent_android.data.network.response.AdjustPriceResponse
import com.mindlab.intrest_agent_android.data.network.response.ChangeOrderResponse
import com.mindlab.intrest_agent_android.data.network.response.DeliverCostResponse
import com.mindlab.intrest_agent_android.data.network.response.IncomingResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Alireza Nezami on 12/13/2021.
 */
interface CostApi {

    @GET("/api/restaurants/{id}/restaurantDeliveryCosts/")
    suspend fun getDeliverCosts(
        @Path("id") id: Int
    ): Response<DeliverCostResponse>


    @POST("/api/delivery/restaurantDeliveryCost/")
    suspend fun editDeliverCost(
        @Body body: List<EditDeliverCostBody>
    ): Response< List<DeliverCostResponse.Embedded.Cost>>


}