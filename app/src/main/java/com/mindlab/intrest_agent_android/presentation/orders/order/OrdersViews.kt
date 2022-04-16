package com.mindlab.intrest_agent_android.presentation.orders.order

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.mindlab.intrest_agent_android.R
import com.mindlab.intrest_agent_android.data.network.response.IncomingResponse
import com.mindlab.intrest_agent_android.presentation.components.*
import com.mindlab.intrest_agent_android.presentation.components.events.ViewModelState
import com.mindlab.intrest_agent_android.presentation.components.theme.GRAY_150
import com.mindlab.intrest_agent_android.presentation.orders.order.OrderViewModel.Companion.DELIVER_TAB
import com.mindlab.intrest_agent_android.presentation.orders.order.OrderViewModel.Companion.INCOMING_TAB
import com.mindlab.intrest_agent_android.presentation.orders.order.OrderViewModel.Companion.IN_PROGRESS_TAB
import com.mindlab.intrest_agent_android.utils.OrderActionType
import com.mindlab.intrest_agent_android.utils.OrderContentType
import com.mindlab.intrest_agent_android.utils.events.OrderEvents
import com.mindlab.intrest_agent_android.utils.extention.LazyPagingItemsExt.pagingStates
import kotlinx.coroutines.flow.collect

/**
 * Created by Alireza Nezami on 12/18/2021.
 */

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun OrdersViews(
    viewModel: OrderViewModel = hiltViewModel()
) {
    val state = viewModel.state().collectAsState(initial = ViewModelState.Loading())
    val viewState = viewModel.orderState.collectAsState(initial = OrderViewState.Nothing)

    val incomingList = viewModel.incomingList.collectAsLazyPagingItems()
    val incomingTabCount = incomingList.itemCount
    val incomingRefreshState =
        rememberSwipeRefreshState(incomingList.loadState.source.refresh is LoadState.Loading)

    val inProgressList = viewModel.inProgressList.collectAsLazyPagingItems()
    val inProgressTabCount = inProgressList.itemCount
    val inProgressRefreshState =
        rememberSwipeRefreshState(inProgressList.loadState.source.refresh is LoadState.Loading)

    val deliverList = viewModel.deliverList.collectAsLazyPagingItems()
    val deliverTabCount = deliverList.itemCount
    val deliverRefreshState =
        rememberSwipeRefreshState(deliverList.loadState.source.refresh is LoadState.Loading)

    val lifecycleOwner = LocalLifecycleOwner.current
    val events = remember(viewModel.events, lifecycleOwner) {
        viewModel.events.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is OrderEvents.IncomingRefresh ->
                    incomingList.refresh()
                is OrderEvents.InProgressRefresh ->
                    inProgressList.refresh()
                is OrderEvents.DeliverRefresh ->
                    deliverList.refresh()

            }
        }
    }

    val selectedTabIndex by viewModel.tabIndex.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val expandedCardIds by viewModel.expandedCardIdsList.collectAsState()

    var showRejectDialog by remember { mutableStateOf(-1) }
    var showDelayDialog by remember { mutableStateOf(-1) }
    var showPriceAdjView by remember { mutableStateOf(-1) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        RejectReasonDialog(
            modifier = Modifier
                .fillMaxWidth(),
            rejectionItemId = showRejectDialog,
            onSubmit = { text, id ->
                viewModel.rejectOrder(id, text)
            },
            onDismiss = {
                showRejectDialog = -1
            }
        )

        NumberPicker(
            modifier = Modifier
                .fillMaxWidth(),
            itemId = showDelayDialog,
            onSubmit = { text, id ->
                viewModel.delayOrder(id, text)
            },
            onDismiss = {
                showDelayDialog = -1
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colors.secondary
                )
        ) {

            Header(
                modifier = Modifier
                    .fillMaxWidth(),
                selectedIndex = selectedTabIndex,
                incoming = incomingTabCount,
                inProgress = inProgressTabCount,
                deliver = deliverTabCount,
                onClick = {
                    viewModel.changeTab(it)
                    expandedCardIds.clear()
                }
            )

            if (selectedTabIndex == INCOMING_TAB) {
                SwipeRefresh(
                    state = incomingRefreshState,
                    onRefresh = {
                        incomingList.refresh()
                    }
                ) {

                    Content(
                        list = incomingList,
                        expandedCardIds = expandedCardIds,
                        typeOrder = OrderContentType.INCOMING,
                        onParentClick = {
                            viewModel.onExpandCard(it)
                        }
                    ) { action, item ->
                        when (action) {
                            OrderActionType.REJECT ->
                                showRejectDialog = item.id
                            OrderActionType.ACCEPT ->
                                viewModel.acceptOrder(item)
                        }
                    }
                }
            }

            if (selectedTabIndex == IN_PROGRESS_TAB) {
                SwipeRefresh(
                    state = inProgressRefreshState,
                    onRefresh = {
                        inProgressList.refresh()
                    }
                ) {

                    Content(
                        list = inProgressList,
                        expandedCardIds = expandedCardIds,
                        typeOrder = OrderContentType.IN_PROGRESS,
                        onParentClick = {
                            viewModel.onExpandCard(it)
                        }
                    ) { action, item ->
                        when (action) {
                            OrderActionType.PRICE_ADJ ->
                                item.invoice?.let {
                                    viewModel.onShowPriceAdjustment(it, item.trackingNumber)
                                }
                            OrderActionType.OPEN_DELAY ->
                                showDelayDialog = item.id
                            OrderActionType.PREPARE ->
                                viewModel.prepareOrder(item)
                        }
                    }
                }
            }

            if (selectedTabIndex == DELIVER_TAB) {
                SwipeRefresh(
                    state = deliverRefreshState,
                    onRefresh = {
                        deliverList.refresh()
                    }
                ) {
                    Content(
                        list = deliverList,
                        expandedCardIds = expandedCardIds,
                        typeOrder = OrderContentType.DELIVER,
                        onParentClick = {
                            viewModel.onExpandCard(it)
                        }
                    ) { action, item ->
                        when (action) {
                            OrderActionType.DELIVER ->
                                viewModel.deliverOrder(item)
                        }
                    }
                }
            }
        }


        PriceAdjustmentViews(
            modifier = Modifier.fillMaxSize(),
            state = viewState.value,
            viewModel = viewModel
        )

        if (loading) {
            FullScreenLoading(
                modifier = Modifier.fillMaxSize()
            )
        }

        CustomNotification(
            modifier = Modifier.fillMaxWidth(),
            state = state.value
        )
    }

}

@ExperimentalMaterialApi
@Composable
fun Header(
    modifier: Modifier,
    selectedIndex: Int,
    incoming: Int,
    inProgress: Int,
    deliver: Int,
    onClick: (index: Int) -> Unit,
) {
    val titles = listOf(R.string.incoming, R.string.in_progress, R.string.deliver)
    Column(modifier = modifier) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 32.dp
                )
        ) {

            Row(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(
                        horizontal = 16.dp,
                    )
            ) {
                titles.forEachIndexed { i, item ->
                    CustomTab(
                        modifier = Modifier,
                        text = stringResource(item),
                        enabled = i == selectedIndex,
                        count =
                        when (i) {
                            0 -> incoming
                            1 -> inProgress
                            2 -> deliver
                            else -> 0
                        }
                    ) { onClick(i) }

                    Spacer(Modifier.width(16.dp))
                }

            }

        }

        EqualRowTexts(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .clip(shape = RoundedCornerShape(5.dp))
                .background(
                    color = GRAY_150
                ),
            texts =
            listOf(
                stringResource(R.string.tracking_num),
                stringResource(R.string.customer),
                stringResource(R.string.order_time),
                stringResource(R.string.serve_type),
                stringResource(R.string.details),
            ),
            isHeader = true,
            isLastOneAction = true
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
    onActionClick: (type: OrderActionType, item: IncomingResponse.Content) -> Unit
) {
    val emptyText = stringResource(
        R.string.there_are_no_order,
        when (typeOrder) {
            OrderContentType.INCOMING -> stringResource(R.string.incoming)
            OrderContentType.IN_PROGRESS -> stringResource(R.string.in_progress)
            OrderContentType.DELIVER -> stringResource(R.string.deliver)
            else -> stringResource(R.string.incoming)
        }
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp)
    ) {

        item {
            Spacer(Modifier.height(16.dp))
        }

        items(list) { item ->
            if (item != null) {
                OrderItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    item = item,
                    type = typeOrder,
                    expanded = expandedCardIds.contains(item.id),
                    onCardArrowClick = {
                        onParentClick(item.id)
                    },
                    onActionClick = {
                        onActionClick(it, item)
                    }
                )
            }
        }

        pagingStates(
            emptyText = emptyText,
            pagingItems = list,
        )
    }
}

