package com.mindlab.intrest_agent_android.data.local.order

import kotlinx.serialization.Serializable

/**
 * Created by Alireza Nezami on 1/22/2022.
 */
@Serializable
data class OrderLocal(
    var receivingOrderStatus: Boolean = false
)
