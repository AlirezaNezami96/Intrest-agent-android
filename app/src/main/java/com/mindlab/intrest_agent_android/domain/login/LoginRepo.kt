package com.mindlab.intrest_agent_android.domain.login

import com.mindlab.intrest_agent_android.data.model.Menu
import com.mindlab.intrest_agent_android.data.model.User
import com.mindlab.intrest_agent_android.data.network.body.LoginBody
import com.mindlab.intrest_agent_android.utils.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Created by Alireza Nezami on 12/13/2021.
 */
interface LoginRepo {

    fun user(): Flow<User?>

    suspend fun login(loginBody: LoginBody): Flow<Resource<Any>>

    suspend fun saveRestaurant(item: Menu): Flow<Resource<Any>>
}