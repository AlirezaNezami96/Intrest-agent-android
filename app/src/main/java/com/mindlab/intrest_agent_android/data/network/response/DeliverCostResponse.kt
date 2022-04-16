package com.mindlab.intrest_agent_android.data.network.response


import com.squareup.moshi.Json

data class DeliverCostResponse(
    @Json(name = "_embedded")
    val embedded: Embedded
) {
    data class Embedded(
        @Json(name = "restaurantDeliveryCosts")
        val costs: List<Cost>
    ) {
        data class Cost(
            @Json(name = "id")
            val id: Int =0,
            @Json(name = "lowerRadius")
            val lowerRadius: Int = 0,
            @Json(name = "upperRadius")
            val upperRadius: Int = 0,
            @Json(name = "cost")
            val cost: Double=0.0,

            val tempId: Int = 0
        )
    }
}