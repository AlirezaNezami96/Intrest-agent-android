package com.mindlab.intrest_agent_android.domain.orders.remote

import androidx.paging.PagingData
import com.mindlab.intrest_agent_android.data.network.body.AdjustPriceBody
import com.mindlab.intrest_agent_android.data.network.response.AdjustPriceResponse
import com.mindlab.intrest_agent_android.data.network.response.ChangeOrderResponse
import com.mindlab.intrest_agent_android.data.network.response.IncomingResponse
import com.mindlab.intrest_agent_android.utils.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Created by Alireza Nezami on 12/13/2021.
 */
interface OrdersRepo {

    fun getIncoming():Flow<PagingData<IncomingResponse.Content>>

    fun getInProgress():Flow<PagingData<IncomingResponse.Content>>

    fun getDeliver():Flow<PagingData<IncomingResponse.Content>>

    suspend fun acceptOrderFlow(id: Int): Flow<Resource<ChangeOrderResponse>>

    suspend fun rejectOrder(id: Int, rejectReason: String): Flow<Resource<ChangeOrderResponse>>

    suspend fun delayOrder(id: Int, time: Int): Flow<Resource<ChangeOrderResponse>>

    suspend fun prepareOrder(id: Int): Flow<Resource<ChangeOrderResponse>>

    suspend fun deliverByCourier(id: Int): Flow<Resource<ChangeOrderResponse>>

    suspend fun adjustPrice(body: AdjustPriceBody): Flow<Resource<AdjustPriceResponse>>

    suspend fun isReceivingOrder(): Flow<Boolean>

}