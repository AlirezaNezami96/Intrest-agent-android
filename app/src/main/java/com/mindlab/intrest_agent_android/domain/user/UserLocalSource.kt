package com.mindlab.intrest_agent_android.domain.user

import com.mindlab.intrest_agent_android.data.local.user.UserLocal
import kotlinx.coroutines.flow.Flow

interface UserLocalSource {
    fun user(): Flow<UserLocal?>

    suspend fun save(userLocal: UserLocal?)

    suspend fun updateToken(userLocal: UserLocal)

    suspend fun updateRestaurant(userLocal: UserLocal)

    suspend fun updateImage(userLocal: UserLocal)

    suspend fun clear()

}
