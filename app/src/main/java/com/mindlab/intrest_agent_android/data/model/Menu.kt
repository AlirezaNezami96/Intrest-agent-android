package com.mindlab.intrest_agent_android.data.model

import com.squareup.moshi.Json

/**
 * Created by Alireza Nezami on 1/9/2022.
 */
data class Menu(
    val id: Int=0,
    val parentId: Int = -1,
    val title: String="",
)
