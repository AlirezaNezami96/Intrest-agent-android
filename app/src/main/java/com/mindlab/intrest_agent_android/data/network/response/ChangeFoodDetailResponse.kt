package com.mindlab.intrest_agent_android.data.network.response


import com.squareup.moshi.Json

data class ChangeFoodDetailResponse(
    @Json(name = "@foodPriceId")
    val foodPriceId: Int,
    @Json(name = "tax")
    val tax: Double,
    @Json(name = "price")
    val price: Double,
    @Json(name = "available")
    val available: Boolean
)