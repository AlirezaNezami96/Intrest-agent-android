package com.mindlab.intrest_agent_android.data.network.body


import com.squareup.moshi.Json

data class EditDeliverCostBody(
    @Json(name = "id")
    val id: Int? = null,
    @Json(name = "lowerRadius")
    val lowerRadius: Int,
    @Json(name = "upperRadius")
    val upperRadius: Int,
    @Json(name = "cost")
    val cost: Double,
    @Json(name = "restaurant")
    val restaurant: Restaurant
) {
    data class Restaurant(
        @Json(name = "id")
        val id: Int
    )
}