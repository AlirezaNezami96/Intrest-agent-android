package com.mindlab.intrest_agent_android.data.network.body


import com.mindlab.intrest_agent_android.utils.DateTime.getTimeStamp
import com.squareup.moshi.Json

data class OrderStatusBody(
    @Json(name = "statusDate")
    val statusDate: String = getTimeStamp(),
    @Json(name = "orderStatus")
    val orderStatus: String,
    @Json(name = "rejectReason")
    val rejectReason: String = "",
    @Json(name = "order")
    val order: Order
) {
    data class Order(
        @Json(name = "id")
        val id: Int
    )
}