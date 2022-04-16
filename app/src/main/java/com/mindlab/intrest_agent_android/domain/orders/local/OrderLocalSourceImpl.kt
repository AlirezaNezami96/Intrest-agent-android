package com.mindlab.intrest_agent_android.domain.orders.local

import androidx.datastore.core.DataStore
import com.mindlab.intrest_agent_android.data.local.order.OrderLocal
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class OrderLocalSourceImpl
@Inject constructor(
    private val dataStore: DataStore<OrderLocal>
) : OrderLocalSource {

    override suspend fun updateReceivingOrderStatus(status: Boolean) {
        dataStore.updateData {
            OrderLocal(
                status
            )
        }
    }

    override suspend fun getStatus(): Flow<Boolean> = dataStore.data
        .map { it.receivingOrderStatus }
        .catch { cause: Throwable ->
            if (cause is IOException) {
                emit(false)
            } else {
                throw cause
            }
        }
}
