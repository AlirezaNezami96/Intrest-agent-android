package com.mindlab.intrest_agent_android.data.network.body


import com.squareup.moshi.Json

data class AdjustPriceBody(
    @Json(name = "invoiceId")
    val invoiceId: Int,
    @Json(name = "adjustedAmount")
    val adjustedAmount: Int,
    @Json(name = "adjustedTax")
    val adjustedTax: Int,
    @Json(name = "description")
    val description: String
)