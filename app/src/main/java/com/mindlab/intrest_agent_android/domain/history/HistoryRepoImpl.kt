package com.mindlab.intrest_agent_android.domain.history

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mindlab.intrest_agent_android.data.network.api.OrdersApi
import com.mindlab.intrest_agent_android.data.network.pagingSource.OrdersPagingSource
import com.mindlab.intrest_agent_android.data.network.response.IncomingResponse
import com.mindlab.intrest_agent_android.domain.user.UserLocalSource
import com.mindlab.intrest_agent_android.utils.OrderContentType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Alireza Nezami on 12/20/2021.
 */
class HistoryRepoImpl
@Inject constructor(
    private val ordersApi: OrdersApi,
    private val pagingConfig: PagingConfig,
    private val userLocalSource: UserLocalSource
) : HistoryRepo {

    override fun getHistory(): Flow<PagingData<IncomingResponse.Content>> {
            return Pager(
                config = pagingConfig,
                pagingSourceFactory = {
                    OrdersPagingSource(
                        userLocalSource = userLocalSource,
                        ordersApi = ordersApi,
                        type = OrderContentType.HISTORY
                    )
                }
            ).flow
    }
}
