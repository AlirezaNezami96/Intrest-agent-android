package com.mindlab.intrest_agent_android.domain.settings

import com.mindlab.intrest_agent_android.data.local.user.UserLocal
import com.mindlab.intrest_agent_android.data.network.api.SettingsApi
import com.mindlab.intrest_agent_android.data.network.body.SettingsBody
import com.mindlab.intrest_agent_android.data.network.response.SettingsResponse
import com.mindlab.intrest_agent_android.domain.orders.local.OrderLocalSource
import com.mindlab.intrest_agent_android.domain.user.UserLocalSource
import com.mindlab.intrest_agent_android.domain.utils.ResponseValidator
import com.mindlab.intrest_agent_android.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by Alireza Nezami on 12/20/2021.
 */
class SettingsRepoImpl
@Inject constructor
    (
    private val settingsApi: SettingsApi,
    private val userLocalSource: UserLocalSource,
    private val orderLocalSource: OrderLocalSource,
    private val responseValidator: ResponseValidator,
) : SettingsRepo {

    override suspend fun changeReceivingOrder(
        status: Boolean
    ): Flow<Resource<SettingsResponse>> = flow {
        orderLocalSource.updateReceivingOrderStatus(status)
        userLocalSource.user().first()?.id.apply {
            this?.let { id ->
                emit(
                    responseValidator.validateResponse(
                        settingsApi.changeReceivingOrder(
                            id,
                            SettingsBody(status)
                        )
                    )
                )
            }
        }
    }

    override suspend fun getUser(): Flow<Resource<UserLocal>> = flow {
        userLocalSource.user().map {
            it?.let { user ->
                emit(Resource.success(user))
            } ?: emit(Resource.shouldLogout(null))
        }
    }

    override suspend fun getSettings(): Flow<Resource<SettingsResponse>> = flow {
        userLocalSource.user().first()?.id.apply {
            this?.let { id ->
                emit(
                    responseValidator.validateResponse(
                        settingsApi.getSettings(
                            id,
                        )
                    )
                )
            }
        }
    }
}
