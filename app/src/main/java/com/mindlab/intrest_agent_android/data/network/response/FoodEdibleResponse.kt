package com.mindlab.intrest_agent_android.data.network.response


import com.squareup.moshi.Json

data class FoodEdibleResponse(
    @Json(name = "@edibleId")
    val edibleId: Int,
    @Json(name = "id")
    val id: Int,
    @Json(name = "preparationTime")
    val preparationTime: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "active")
    val active: Boolean
)