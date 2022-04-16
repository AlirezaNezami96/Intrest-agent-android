package com.mindlab.intrest_agent_android.domain.cost

import com.mindlab.intrest_agent_android.data.network.api.CostApi
import com.mindlab.intrest_agent_android.data.network.response.DeliverCostResponse
import com.mindlab.intrest_agent_android.domain.user.UserLocalSource
import com.mindlab.intrest_agent_android.domain.utils.ResponseValidator
import com.mindlab.intrest_agent_android.utils.Resource
import com.mindlab.intrest_agent_android.utils.mapper.CostMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by Alireza Nezami on 12/20/2021.
 */
class DeliverCostRepoImpl
@Inject constructor
    (
    private val costApi: CostApi,
    private val userLocalSource: UserLocalSource,
    private val responseValidator: ResponseValidator,
) : DeliverCostRepo {

    override suspend fun getDeliverCost(): Flow<Resource<DeliverCostResponse>> = flow {
        emit(responseValidator.validateResponse(costApi.getDeliverCosts(8)))
    }

    override suspend fun sync(value: List<DeliverCostResponse.Embedded.Cost>)
            : Flow<Resource< List<DeliverCostResponse.Embedded.Cost>>> = flow {
        userLocalSource.user().first()?.id?.apply {
            CostMapper.map(value, this).let { body ->
                emit(responseValidator.validateResponse(costApi.editDeliverCost(body)))
            }
        }
    }
}
