package com.mindlab.intrest_agent_android.utils.extention

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.mindlab.intrest_agent_android.presentation.components.EmptyOrderList
import com.mindlab.intrest_agent_android.presentation.components.ErrorStateFullPage
import com.mindlab.intrest_agent_android.presentation.components.ErrorStateRowItem
import com.mindlab.intrest_agent_android.presentation.components.LoadingStateRowItem

/**
 * Created by Alireza Nezami on 1/4/2022.
 */
object LazyPagingItemsExt {

    fun <T : Any> LazyListScope.pagingStates(
        emptyText: String,
        pagingItems: LazyPagingItems<T>,
        emptyState: @Composable LazyItemScope.() -> Unit = {
            EmptyOrderList(
                modifier = Modifier.fillParentMaxSize(),
                text = emptyText
            )
        },
        initialLoadState: @Composable LazyItemScope.() -> Unit = {
//            FullScreenLoading()
        },
        nextPageLoadingState: @Composable LazyItemScope.() -> Unit = {
            LoadingStateRowItem()
        },
        initialErrorState: @Composable LazyItemScope.() -> Unit = {
            ErrorStateFullPage(
                onRetryClicked = {
                    pagingItems.retry()
                }
            )
        },
        nextPageLoadingErrorState: @Composable LazyItemScope.() -> Unit = {
            ErrorStateRowItem(
                onRetryClicked = {
                    pagingItems.retry()
                }
            )
        }
    ) {

        val refreshLoadState = pagingItems.loadState.source.refresh
        val appendLoadState = pagingItems.loadState.source.append

        when {
            refreshLoadState is LoadState.NotLoading
                    && appendLoadState is LoadState.NotLoading
                    && appendLoadState.endOfPaginationReached
                    && pagingItems.itemCount == 0 -> {
                // data loaded and we had 0 results...
                item { emptyState() }
            }
            refreshLoadState is LoadState.Loading -> {
                // first fetch of data is loading...
                item { initialLoadState() }
            }
            appendLoadState is LoadState.Loading -> {
                // next page of data is loading...
                item { nextPageLoadingState() }
            }
            refreshLoadState is LoadState.Error -> {
                // error occurred with initial loading of data...
                item { initialErrorState() }
            }
            appendLoadState is LoadState.Error -> {
                // error occurred when loading next page of data...
                item { nextPageLoadingErrorState() }
            }
        }
    }
}