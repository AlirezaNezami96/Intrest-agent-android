package com.mindlab.intrest_agent_android.data.network.response


import com.squareup.moshi.Json

data class ChangeOrderResponse(
    @Json(name = "id")
    val id: Int,
)