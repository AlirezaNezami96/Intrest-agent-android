package com.mindlab.intrest_agent_android.data.network.body

import com.squareup.moshi.Json

/**
 * Created by Alireza Nezami on 1/11/2022.
 */
data class FoodAvailabilityBody(
    @Json(name = "active")
    val active: Boolean
)
