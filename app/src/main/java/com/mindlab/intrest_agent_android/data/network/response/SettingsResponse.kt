package com.mindlab.intrest_agent_android.data.network.response


import com.squareup.moshi.Json

data class SettingsResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "availableService")
    val availableService: Boolean,
    @Json(name = "serveType")
    val serveType: Int
)