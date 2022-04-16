package com.mindlab.intrest_agent_android.data.model

import com.squareup.moshi.Json

/**
 * Created by Alireza Nezami on 1/9/2022.
 */
data class MenuCategory(
    val id: Int,
    val parentId: Int,
    val title: String
)
