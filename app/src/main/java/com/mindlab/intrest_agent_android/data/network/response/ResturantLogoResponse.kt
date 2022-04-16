package com.mindlab.intrest_agent_android.data.network.response


import android.media.tv.TvContract
import com.squareup.moshi.Json

data class ResturantLogoResponse(
    @Json(name = "_embedded")
    val embedded: Embedded
) {
    data class Embedded(
        @Json(name = "restaurantLogoes")
        val logos: List<Logo>?
    ) {
        data class Logo(
            @Json(name = "id")
            val id: Int,
            @Json(name = "logoImage")
            val logoImage: String
        )
    }
}