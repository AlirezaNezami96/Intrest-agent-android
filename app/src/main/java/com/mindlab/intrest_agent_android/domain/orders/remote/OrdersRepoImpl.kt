package com.mindlab.intrest_agent_android.domain.orders.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mindlab.intrest_agent_android.data.network.api.OrdersApi
import com.mindlab.intrest_agent_android.data.network.body.AdjustPriceBody
import com.mindlab.intrest_agent_android.data.network.body.DelayOrderBody
import com.mindlab.intrest_agent_android.data.network.body.OrderStatusBody
import com.mindlab.intrest_agent_android.data.network.pagingSource.OrdersPagingSource
import com.mindlab.intrest_agent_android.data.network.response.AdjustPriceResponse
import com.mindlab.intrest_agent_android.data.network.response.ChangeOrderResponse
import com.mindlab.intrest_agent_android.data.network.response.IncomingResponse
import com.mindlab.intrest_agent_android.domain.orders.local.OrderLocalSource
import com.mindlab.intrest_agent_android.domain.user.UserLocalSource
import com.mindlab.intrest_agent_android.domain.utils.ResponseValidator
import com.mindlab.intrest_agent_android.utils.Constants.ORDER_STATUS_ACCEPT
import com.mindlab.intrest_agent_android.utils.Constants.ORDER_STATUS_DELIVER_TO_COURIER
import com.mindlab.intrest_agent_android.utils.Constants.ORDER_STATUS_PREPARED
import com.mindlab.intrest_agent_android.utils.Constants.ORDER_STATUS_REJECT
import com.mindlab.intrest_agent_android.utils.OrderContentType
import com.mindlab.intrest_agent_android.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by Alireza Nezami on 12/20/2021.
 */
class OrdersRepoImpl
@Inject constructor
    (
    private val ordersApi: OrdersApi,
    private val userLocalSource: UserLocalSource,
    private val orderLocalSource: OrderLocalSource,
    private val responseValidator: ResponseValidator,
    private val pagingConfig: PagingConfig
) : OrdersRepo {

    override fun getIncoming(
    ): Flow<PagingData<IncomingResponse.Content>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                OrdersPagingSource(
                    userLocalSource = userLocalSource,
                    ordersApi = ordersApi,
                    type = OrderContentType.INCOMING
                )
            }
        ).flow
    }

    override fun getInProgress(
    ): Flow<PagingData<IncomingResponse.Content>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                OrdersPagingSource(
                    userLocalSource = userLocalSource,
                    ordersApi = ordersApi,
                    type = OrderContentType.IN_PROGRESS
                )
            }
        ).flow
    }

    override fun getDeliver(
    ): Flow<PagingData<IncomingResponse.Content>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                OrdersPagingSource(
                    userLocalSource = userLocalSource,
                    ordersApi = ordersApi,
                    type = OrderContentType.DELIVER
                )
            }
        ).flow
    }

    override suspend fun acceptOrderFlow(id: Int): Flow<Resource<ChangeOrderResponse>> = flow {
        val response = ordersApi.acceptOrder(
            OrderStatusBody(
                orderStatus = ORDER_STATUS_ACCEPT,
                order = OrderStatusBody.Order(
                    id = id
                )
            )
        )
        emit(responseValidator.validateResponse(response))
    }

    override suspend fun rejectOrder(
        id: Int,
        rejectReason: String
    ): Flow<Resource<ChangeOrderResponse>> = flow {
        val response = ordersApi.acceptOrder(
            OrderStatusBody(
                rejectReason = rejectReason,
                orderStatus = ORDER_STATUS_REJECT,
                order = OrderStatusBody.Order(
                    id = id
                )
            )
        )
        emit(responseValidator.validateResponse(response))
    }

    override suspend fun delayOrder(id: Int, time: Int): Flow<Resource<ChangeOrderResponse>> =
        flow {
            val response = ordersApi
                .delayOrder(
                    id,
                    DelayOrderBody(
                        time
                    )
                )

            emit(responseValidator.validateResponse(response))
        }

    override suspend fun prepareOrder(id: Int): Flow<Resource<ChangeOrderResponse>> = flow {
        val response = ordersApi.prepareOrder(
            OrderStatusBody(
                orderStatus = ORDER_STATUS_PREPARED,
                order = OrderStatusBody.Order(
                    id = id
                )
            )
        )
        emit(responseValidator.validateResponse(response))
    }

    override suspend fun deliverByCourier(id: Int): Flow<Resource<ChangeOrderResponse>> = flow {
        val response = ordersApi.deliverToCourier(
            OrderStatusBody(
                orderStatus = ORDER_STATUS_DELIVER_TO_COURIER,
                order = OrderStatusBody.Order(
                    id = id
                )
            )
        )
        emit(responseValidator.validateResponse(response))

    }

    override suspend fun adjustPrice(body: AdjustPriceBody): Flow<Resource<AdjustPriceResponse>> =
        flow {
            val response = ordersApi.adjustPrice(body)
            emit(responseValidator.validateResponse(response))
        }

    override suspend fun isReceivingOrder(): Flow<Boolean> {
        return orderLocalSource.getStatus().map {
            it
        }
    }
}
