package com.mindlab.intrest_agent_android.data.network.body

import com.squareup.moshi.Json

/**
 * Created by Alireza Nezami on 12/13/2021.
 */
data class LoginBody(
    @Json(name = "username") val username: String,
    @Json(name = "password") val password: String,
)
