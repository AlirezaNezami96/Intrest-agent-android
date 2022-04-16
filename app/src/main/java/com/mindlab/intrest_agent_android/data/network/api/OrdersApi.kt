package com.mindlab.intrest_agent_android.data.network.api

import com.mindlab.intrest_agent_android.data.network.body.AdjustPriceBody
import com.mindlab.intrest_agent_android.data.network.body.DelayOrderBody
import com.mindlab.intrest_agent_android.data.network.body.OrderStatusBody
import com.mindlab.intrest_agent_android.data.network.response.AdjustPriceResponse
import com.mindlab.intrest_agent_android.data.network.response.ChangeOrderResponse
import com.mindlab.intrest_agent_android.data.network.response.IncomingResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Alireza Nezami on 12/13/2021.
 */
interface OrdersApi {

    @GET("/api/orders/restaurant/{id}/findByLastStatus/SUCCESSFUL_PAYMENT/")
    suspend fun getIncomingPaging(
        @Path("id") id: Int,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): IncomingResponse

    @GET("/api/orders/restaurant/{id}/findByLastStatus/ACCEPT_RESTAURANT/")
    suspend fun getInProgressPaging(
        @Path("id") id: Int,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): IncomingResponse

    @GET("/api/orders/restaurant/{id}/findByLastStatus/PREPARED/")
    suspend fun getDeliverPaging(
        @Path("id") id: Int,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): IncomingResponse

    @GET("/api/orders/restaurant/{id}")
    suspend fun getHistory(
        @Path("id") id: Int,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): IncomingResponse

    @POST("/api/orderStatus")
    suspend fun acceptOrder(
        @Body orderStatusBody: OrderStatusBody
    ): Response<ChangeOrderResponse>

    @POST("/api/orderStatus")
    suspend fun prepareOrder(
        @Body orderStatusBody: OrderStatusBody
    ): Response<ChangeOrderResponse>

    @POST("/api/orderStatus")
    suspend fun deliverToCourier(
        @Body orderStatusBody: OrderStatusBody
    ): Response<ChangeOrderResponse>

    @POST("/api/orders/{id}/delayTime")
    suspend fun delayOrder(
        @Path("id") id: Int,
        @Body delayOrderBody: DelayOrderBody
    ): Response<ChangeOrderResponse>

    @POST("/api/orders/adjustInvoice")
    suspend fun adjustPrice(
        @Body body: AdjustPriceBody
    ): Response<AdjustPriceResponse>
}