package com.mindlab.intrest_agent_android.data.local.user

import kotlinx.serialization.Serializable

/**
 * Created by Alireza Nezami on 12/13/2021.
 */
@Serializable
data class UserLocal(
    var username: String = "",
    var accessToken: String = "",
    var refreshToken: String = "",
    var expiresIn: Long = 0,

    var id: Int = 0,
    var restaurantName: String = "",

    var image: String = "",

    var isLoggedIn: Boolean = false
) {


    fun clear(): UserLocal =
        UserLocal(
            username = "",
            accessToken = "",
            refreshToken = "",
            expiresIn = 0,
            id = 0,
            restaurantName = "",
            image = "",
            isLoggedIn = false,
        )
}

