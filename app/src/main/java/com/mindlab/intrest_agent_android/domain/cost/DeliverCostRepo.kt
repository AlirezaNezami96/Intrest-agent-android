package com.mindlab.intrest_agent_android.domain.cost

import com.mindlab.intrest_agent_android.data.network.response.DeliverCostResponse
import com.mindlab.intrest_agent_android.utils.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Created by Alireza Nezami on 12/13/2021.
 */
interface DeliverCostRepo {

    suspend fun getDeliverCost(): Flow<Resource<DeliverCostResponse>>

    suspend fun sync(value: List<DeliverCostResponse.Embedded.Cost>): Flow<Resource<List<DeliverCostResponse.Embedded.Cost>>>

}