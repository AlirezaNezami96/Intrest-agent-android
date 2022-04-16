package com.mindlab.intrest_agent_android.data.network.body


import com.squareup.moshi.Json

data class DelayOrderBody(
    @Json(name = "delayMinutes")
    val delayMinutes: Int
)