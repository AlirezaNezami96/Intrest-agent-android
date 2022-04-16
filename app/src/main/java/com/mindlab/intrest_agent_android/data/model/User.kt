package com.mindlab.intrest_agent_android.data.model

/**
 * Created by Alireza Nezami on 12/14/2021.
 */

data class User(
    val accessToken: String ,
    val refreshToken: String,
    val expiresIn: Long ,
)