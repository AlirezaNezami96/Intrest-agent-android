package com.mindlab.intrest_agent_android.data.network.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mindlab.intrest_agent_android.data.network.api.OrdersApi
import com.mindlab.intrest_agent_android.data.network.response.IncomingResponse
import com.mindlab.intrest_agent_android.domain.user.UserLocalSource
import com.mindlab.intrest_agent_android.utils.Constants.DEFAULT_PAGE_INDEX
import com.mindlab.intrest_agent_android.utils.Constants.DEFAULT_PAGE_SIZE
import com.mindlab.intrest_agent_android.utils.OrderContentType
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import java.io.IOException

/**
 * Created by Alireza Nezami on 1/4/2022.
 */
class OrdersPagingSource(
    private val userLocalSource: UserLocalSource,
    private val ordersApi: OrdersApi,
    private val type: OrderContentType
) : PagingSource<Int, IncomingResponse.Content>() {

    override fun getRefreshKey(state: PagingState<Int, IncomingResponse.Content>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val closestPage = state.closestPageToPosition(anchorPosition)?.prevKey
                ?: DEFAULT_PAGE_INDEX
            val refKey = if (anchorPosition > (closestPage) * DEFAULT_PAGE_SIZE + DEFAULT_PAGE_SIZE)
                closestPage + 1
            else
                closestPage
            refKey
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, IncomingResponse.Content> {
        val page = params.key ?: DEFAULT_PAGE_INDEX
        val restaurantId = userLocalSource.user().first()?.id
            ?: return LoadResult.Error(Throwable("user not found"))

        return try {
            val response = when (type) {
                OrderContentType.INCOMING ->
                    ordersApi.getIncomingPaging(restaurantId, page, params.loadSize).content
                OrderContentType.IN_PROGRESS ->
                    ordersApi.getInProgressPaging(restaurantId, page, params.loadSize).content
                OrderContentType.DELIVER ->
                    ordersApi.getDeliverPaging(restaurantId, page, params.loadSize).content
                OrderContentType.HISTORY ->
                    ordersApi.getHistory(restaurantId, page, params.loadSize).content
            }
            LoadResult.Page(
                response,
                prevKey = if (page == DEFAULT_PAGE_INDEX) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}