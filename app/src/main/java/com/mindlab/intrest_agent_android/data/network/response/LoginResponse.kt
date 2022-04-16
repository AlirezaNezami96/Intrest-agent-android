package com.mindlab.intrest_agent_android.data.network.response


import com.squareup.moshi.Json

data class LoginResponse(
    @Json(name = "accessToken")
    val accessToken: String = "",

    @Json(name = "refreshToken")
    val refreshToken: String = "",

    @Json(name = "expiresIn")
    val expiresIn: Long = 0,

    @Json(name = "message")
    val message: String = "",


    )