package com.mindlab.intrest_agent_android.presentation.history

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mindlab.intrest_agent_android.R
import com.mindlab.intrest_agent_android.data.network.response.IncomingResponse
import com.mindlab.intrest_agent_android.presentation.components.EqualRowTexts
import com.mindlab.intrest_agent_android.presentation.components.HistoryItem
import com.mindlab.intrest_agent_android.presentation.components.OrderItem
import com.mindlab.intrest_agent_android.presentation.components.theme.GRAY_150
import com.mindlab.intrest_agent_android.utils.OrderContentType
import com.mindlab.intrest_agent_android.utils.extention.LazyPagingItemsExt.pagingStates

/**
 * Created by Alireza Nezami on 12/18/2021.
 */

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun HistoryViews(
    viewModel: HistoryViewModel = hiltViewModel()
) {

    val historyList = viewModel.historyList.collectAsLazyPagingItems()
    val expandedCardIds by viewModel.expandedCardIdsList.collectAsState()
    val historyRefreshState =
        rememberSwipeRefreshState(historyList.loadState.source.refresh is LoadState.Loading)

    SwipeRefresh(
        state = historyRefreshState,
        onRefresh = {
            historyList.refresh()
        }
    ) {

        Content(
            list = historyList,
            expandedCardIds = expandedCardIds,
            typeOrder = OrderContentType.HISTORY,
            onParentClick = {
                viewModel.onExpandCard(it)
            }
        )
    }
}

@ExperimentalAnimationApi
@Composable
fun Content(
    list: LazyPagingItems<IncomingResponse.Content>,
    expandedCardIds: List<Int>,
    typeOrder: OrderContentType,
    onParentClick: (index: Int) -> Unit,
) {
    val emptyText = stringResource(R.string.there_are_no_history_order)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp)
    ) {


        item {
            Spacer(Modifier.height(36.dp))
        }

        item {
            EqualRowTexts(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = GRAY_150
                    ),
                texts =
                listOf(
                    stringResource(R.string.tracking_num),
                    stringResource(R.string.customer),
                    stringResource(R.string.order_time),
                    stringResource(R.string.delivery_type),
                    stringResource(R.string.details),
                ),
                isHeader = true,
                isLastOneAction = true
            )
        }

        item {
            Spacer(Modifier.height(16.dp))
        }

        items(list) { item ->
            if (item != null) {
                HistoryItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    item = item,
                    type = typeOrder,
                    expanded = expandedCardIds.contains(item.id),
                    onCardArrowClick = {
                        onParentClick(item.id)
                    },
                    onActionClick = {
                    }
                )
            }
        }

        pagingStates(
            emptyText =emptyText,
            pagingItems = list,
        )
    }
}
