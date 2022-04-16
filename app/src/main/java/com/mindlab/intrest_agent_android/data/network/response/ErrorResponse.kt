package com.mindlab.intrest_agent_android.data.network.response

import com.squareup.moshi.Json

/**
 * Created by Alireza Nezami on 12/13/2021.
 */
data class ErrorResponse(
//    @Json(name = "timestamp")
//    val timestamp: String,
//
//    @Json(name = "status")
//    val status: Int,
//
//    @Json(name = "error")
//    val error: String?,
//
//    @Json(name = "trace")
//    val trace: String?,

    @Json(name = "message")
    val message: String?,
//
//    @Json(name = "path")
//    val path: String?,
//
//    @Json(name = "debugMessage")
//    val debugMessage: String?
) {
}