package com.mindlab.intrest_agent_android.domain.history

import androidx.paging.PagingData
import com.mindlab.intrest_agent_android.data.network.response.IncomingResponse
import com.mindlab.intrest_agent_android.utils.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Created by Alireza Nezami on 12/13/2021.
 */
interface HistoryRepo {

    fun getHistory(): Flow<PagingData<IncomingResponse.Content>>


}