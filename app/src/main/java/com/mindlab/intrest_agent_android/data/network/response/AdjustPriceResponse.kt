package com.mindlab.intrest_agent_android.data.network.response


import com.squareup.moshi.Json

data class AdjustPriceResponse(
    @Json(name = "id")
    val id: Int,
)