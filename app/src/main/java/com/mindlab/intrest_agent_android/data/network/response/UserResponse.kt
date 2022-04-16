package com.mindlab.intrest_agent_android.data.network.response


import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
@Keep
data class UserResponse(
    @Json(name = "user")
    val user: User,
): Parcelable {
    @Parcelize
    @Keep
    data class User(
        @Json(name = "id")
        val id: Int,
        @Json(name = "restaurants")
        val restaurants: List<Restaurant>,
    ) : Parcelable {
        @Parcelize
        @Keep
        data class Restaurant(
            @Json(name = "id")
            val id: Int,
            @Json(name = "name")
            val name: String,
        ):Parcelable

    }
}