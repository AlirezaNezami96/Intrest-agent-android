package com.mindlab.intrest_agent_android.data.network.body

import com.squareup.moshi.Json

/**
 * Created by Alireza Nezami on 12/14/2021.
 */
data class RefreshTokenBody(
    @Json(name = "refreshToken") val refreshToken: String,
) {
}