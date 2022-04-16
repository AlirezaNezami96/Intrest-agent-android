package com.mindlab.intrest_agent_android.data.network.body


import com.squareup.moshi.Json

data class SettingsBody(
    @Json(name = "availableService")
    val availableService: Boolean
)