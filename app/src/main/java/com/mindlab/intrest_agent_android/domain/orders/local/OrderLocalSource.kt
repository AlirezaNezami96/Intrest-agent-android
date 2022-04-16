package com.mindlab.intrest_agent_android.domain.orders.local

import com.mindlab.intrest_agent_android.data.local.user.UserLocal
import kotlinx.coroutines.flow.Flow

interface OrderLocalSource {

    suspend fun updateReceivingOrderStatus(status: Boolean)

    suspend fun getStatus(): Flow<Boolean>
}
