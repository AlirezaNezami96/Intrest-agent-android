package com.mindlab.intrest_agent_android.domain.settings

import com.mindlab.intrest_agent_android.data.local.user.UserLocal
import com.mindlab.intrest_agent_android.data.network.response.SettingsResponse
import com.mindlab.intrest_agent_android.utils.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Created by Alireza Nezami on 12/13/2021.
 */
interface SettingsRepo {

    suspend fun changeReceivingOrder(status: Boolean): Flow<Resource<SettingsResponse>>

    suspend fun getSettings(): Flow<Resource<SettingsResponse>>

    suspend fun getUser(): Flow<Resource<UserLocal>>


}