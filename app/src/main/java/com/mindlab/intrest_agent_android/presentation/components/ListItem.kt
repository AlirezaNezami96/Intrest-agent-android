package com.mindlab.intrest_agent_android.presentation.components

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.mindlab.intrest_agent_android.R
import com.mindlab.intrest_agent_android.data.network.response.DeliverCostResponse
import com.mindlab.intrest_agent_android.data.network.response.IncomingResponse
import com.mindlab.intrest_agent_android.data.network.response.MenuResponse
import com.mindlab.intrest_agent_android.presentation.components.theme.*
import com.mindlab.intrest_agent_android.utils.DeliverType
import com.mindlab.intrest_agent_android.utils.OrderActionType
import com.mindlab.intrest_agent_android.utils.OrderContentType
import com.mindlab.intrest_agent_android.utils.extention.*

/**
 * Created by Alireza Nezami on 12/20/2021.
 */
@SuppressLint("UnusedTransitionTargetStateParameter")
@ExperimentalAnimationApi
@Composable
fun OrderItem(
    modifier: Modifier,
    item: IncomingResponse.Content,
    expanded: Boolean,
    type: OrderContentType,
    onCardArrowClick: () -> Unit,
    onActionClick: (type: OrderActionType) -> Unit
) {

    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }
    val transition = updateTransition(transitionState, label = "")

    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = 200)
    }, label = "") {
        if (expanded) 0f else 180f
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.clickable {
            onCardArrowClick()
        },
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
            ) {

                ItemTitle(
                    modifier = Modifier
                        .weight(1.0f)
                        .align(Alignment.CenterVertically),
                    text = item.trackingNumber.toString(),
                    color = MaterialTheme.colors.onSecondary
                )
                ItemTitle(
                    modifier = Modifier
                        .weight(1.0f)
                        .align(Alignment.CenterVertically),
                    text = item.customer.fullName,
                    color = MaterialTheme.colors.onSecondary
                )

                ItemTitle(
                    text = item.preparedTime.substringDate(),
                    modifier = Modifier
                        .weight(1.0f)
                        .align(Alignment.CenterVertically),
                    color = MaterialTheme.colors.onSecondary
                )

                Row(
                    modifier = Modifier
                        .weight(1.0f)
                        .align(Alignment.CenterVertically)
                ) {
                    Image(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .align(Alignment.CenterVertically),
                        painter = painterResource(R.drawable.ic_online_blue),
                        contentDescription = ""
                    )
                    ItemTitle(
                        modifier = Modifier
                            .weight(1.0f)
                            .align(Alignment.CenterVertically),
                        text = item.serveTypeTitle.removeUnderlines(),
                        color = LINK_BLUE
                    )
                }

                Box(modifier = Modifier.weight(1.0f)) {
                    IconButton(
                        onClick = { onCardArrowClick() },
                        modifier = Modifier
                            .align(Alignment.CenterEnd),
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_d_d),
                            contentDescription = "",
                            modifier = Modifier.rotate(arrowRotationDegree),
                        )
                    }
                }
            }

            ExpandableContent(
                visible = expanded,
                initialVisibility = expanded,
                type = type,
                item = item,
                onClick = { onActionClick(it) }
            )
        }
    }

    Spacer(Modifier.height(12.dp))
}

@SuppressLint("UnusedTransitionTargetStateParameter")
@ExperimentalAnimationApi
@Composable
fun HistoryItem(
    modifier: Modifier,
    item: IncomingResponse.Content,
    expanded: Boolean,
    type: OrderContentType,
    onCardArrowClick: () -> Unit,
    onActionClick: (type: OrderActionType) -> Unit
) {

    val transitionState = remember {
        MutableTransitionState(expanded).apply {
            targetState = !expanded
        }
    }
    val transition = updateTransition(transitionState, label = "")

    val arrowRotationDegree by transition.animateFloat({
        tween(durationMillis = 200)
    }, label = "") {
        if (expanded) 0f else 180f
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.clickable {
            onCardArrowClick()
        },
        elevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
            ) {

                ItemTitle(
                    modifier = Modifier
                        .weight(1.0f)
                        .align(Alignment.CenterVertically),
                    text = item.trackingNumber.toString(),
                    color = MaterialTheme.colors.onSecondary
                )
                ItemTitle(
                    modifier = Modifier
                        .weight(1.0f)
                        .align(Alignment.CenterVertically),
                    text = item.customer.fullName,
                    color = MaterialTheme.colors.onSecondary
                )

                ItemTitle(
                    text = item.preparedTime.toFullDate(),
                    modifier = Modifier
                        .weight(1.0f)
                        .align(Alignment.CenterVertically),
                    color = MaterialTheme.colors.onSecondary
                )

                ItemTitle(
                    modifier = Modifier
                        .weight(1.0f)
                        .align(Alignment.CenterVertically),
                    text = item.serveTypeTitle ?: stringResource(R.string.pick_up_by_customer),
                    color = MaterialTheme.colors.onSecondary
                )

                Box(modifier = Modifier.weight(1.0f)) {
                    IconButton(
                        onClick = { onCardArrowClick() },
                        modifier = Modifier
                            .align(Alignment.CenterEnd),
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_d_d),
                            contentDescription = "",
                            modifier = Modifier.rotate(arrowRotationDegree),
                        )
                    }
                }
            }

            ExpandableContent(
                visible = expanded,
                initialVisibility = expanded,
                type = type,
                item = item,
                onClick = { onActionClick(it) }
            )
        }
    }

    Spacer(Modifier.height(12.dp))
}

@OptIn(ExperimentalAnimationApi::class)
@ExperimentalAnimationApi
@Composable
fun ExpandableContent(
    visible: Boolean,
    initialVisibility: Boolean,
    type: OrderContentType,
    item: IncomingResponse.Content,
    onClick: (type: OrderActionType) -> Unit
) {

    val enterExpand = remember {
        expandVertically(animationSpec = tween(100))
    }
    val exitCollapse = remember {
        shrinkVertically(animationSpec = tween(100))
    }

    AnimatedVisibility(
        visible = visible,
        initiallyVisible = initialVisibility,
        modifier = Modifier,
        enter = enterExpand,
        exit = exitCollapse
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        )
        {
            if (type == OrderContentType.DELIVER) {
                DeliverOderDetails(
                    modifier = Modifier
                        .weight(5f)
                        .padding(horizontal = 8.dp),
                    content = item,
                )

                Spacer(
                    modifier = Modifier
                        .weight(2f)
                )
            } else {
                OderDetails(
                    modifier = Modifier
                        .weight(7f)
                        .padding(horizontal = 8.dp),
                    content = item,
                    total = item.invoice?.total
                )
            }

            OrderActions(
                modifier = Modifier
                    .weight(3f)
                    .align(Alignment.Bottom)
                    .padding(horizontal = 8.dp),
                item = item,
                type = type
            ) { onClick(it) }
        }
    }
}

@Composable
fun OrderActions(
    modifier: Modifier,
    item: IncomingResponse.Content,
    type: OrderContentType,
    onClick: (type: OrderActionType) -> Unit
) {
    Column(modifier = modifier) {
        when (type) {
            OrderContentType.INCOMING -> {
                ContactButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start)
                        .padding(vertical = 8.dp),
                    text = item.customer.mobileNo.toString(),
                    onClick = { onClick(OrderActionType.CONTACT) }
                )

                PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    text = stringResource(R.string.accept_order),
                    onClick = { onClick(OrderActionType.ACCEPT) }
                )

                SecondaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    text = stringResource(R.string.reject_order),
                    onClick = { onClick(OrderActionType.REJECT) }
                )
            }

            OrderContentType.IN_PROGRESS -> {
                ContactButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start)
                        .padding(vertical = 8.dp),
                    text = item.customer.mobileNo.toString(),
                    onClick = { onClick(OrderActionType.CONTACT) }
                )

                BorderButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    text = stringResource(R.string.price_adjustment),
                    icon = R.drawable.ic_money,
                    onClick = { onClick(OrderActionType.PRICE_ADJ) }
                )
                BorderButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    text = stringResource(R.string.delay_order),
                    icon = R.drawable.ic_time_filled,
                    onClick = { onClick(OrderActionType.OPEN_DELAY) }
                )

                PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    text = stringResource(R.string.prepared),
                    onClick = { onClick(OrderActionType.PREPARE) }
                )

            }

            OrderContentType.DELIVER -> {
                PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    text =
                    if (item.serveType == DeliverType.DELIVER) stringResource(R.string.pick_up_by_customer)
                    else stringResource(R.string.deliver_up_by_courier),
                    onClick = { onClick(OrderActionType.DELIVER) }
                )
            }

            else -> {}
        }


    }
}

@Composable
fun DeliverOderDetails(
    modifier: Modifier,
    content: IncomingResponse.Content,
) {
    val items = content.orderItems
    Column(
        modifier = modifier
            .padding(vertical = 16.dp)
    ) {
        ItemTitle(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(8.dp),
            text = stringResource(R.string.order_details),
        )

        Column(
            modifier = Modifier
                .border(
                    BorderStroke(1.dp, GRAY_50),
                    shape = RoundedCornerShape(10.dp)
                )
        ) {

            Spacer(
                modifier =
                Modifier.height(8.dp)
            )

            EqualRowTexts(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                texts =
                listOf(
                    stringResource(R.string.serial_number),
                    stringResource(R.string.order_name),
                    stringResource(R.string.quantity),
                ),
                isHeader = true,
                isLastOneAction = true
            )

            items.forEachIndexed { i, item ->
                EqualRowTexts(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 8.dp),
                    texts = listOf(
                        (i + 1).toString(),
                        item.food.name,
                        item.quantity.toString(),
                    ),
                    isLastOneAction = true
                )

            }

            Spacer(
                modifier = Modifier
                    .height(8.dp)
            )

            content.deliveryNote?.let {

                Spacer(
                    modifier = Modifier
                        .height(1.dp)
                        .padding(
                            horizontal = 16.dp
                        )
                        .fillMaxWidth()
                        .background(
                            color = GRAY_50,
                            shape = DottedShape(step = 10.dp)
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    MultiStyleText(
                        modifier = Modifier
                            .padding(
                                horizontal = 16.dp,
                                vertical = 16.dp
                            ),
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    color = GRAY_800
                                )
                            ) {
                                append(stringResource(R.string.delivery_note))
                            }

                            withStyle(
                                SpanStyle(
                                    color = MaterialTheme.colors.onSecondary
                                )
                            ) {
                                append(it)
                            }

                        },
                        style = MaterialTheme.typography.caption,
                    )
                }
            }
        }
    }

}

@Composable
fun OderDetails(
    modifier: Modifier,
    content: IncomingResponse.Content,
    total: Double?
) {
    val items = content.orderItems
    Column(
        modifier = modifier
            .padding(vertical = 16.dp)
    ) {
        ItemTitle(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(8.dp),
            text = stringResource(R.string.order_details),
        )

        Column(
            modifier = Modifier
                .border(
                    BorderStroke(1.dp, GRAY_50),
                    shape = RoundedCornerShape(10.dp)
                )
        ) {

            Spacer(
                modifier =
                Modifier.height(16.dp)
            )

            OrderItemsRowTexts(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                texts =
                listOf(
                    stringResource(R.string.serial_number),
                    stringResource(R.string.order_name),
                    stringResource(R.string.price),
                    stringResource(R.string.quantity),
                    stringResource(R.string.price),
                ),
                isHeader = true
            )



            items.forEachIndexed { i, item ->
                OrderItemsRowTexts(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    texts = listOf(
                        (i + 1).toString(),
                        item.food.name,
                        "$${item.itemPrice}",
                        item.quantity.toString(),
                        "$${(item.itemPrice * item.quantity)}"
                    ),
                )

            }

            Spacer(
                modifier = Modifier
                    .height(16.dp)
            )

            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .padding(
                        horizontal = 16.dp
                    )
                    .fillMaxWidth()
                    .background(
                        color = GRAY_50,
                        shape = DottedShape(step = 10.dp)
                    )
            )

            content.deliveryNote?.let {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    MultiStyleText(
                        modifier = Modifier
                            .padding(
                                horizontal = 16.dp,
                                vertical = 16.dp
                            ),
                        text = buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    color = GRAY_800
                                )
                            ) {
                                append(stringResource(R.string.delivery_note))
                            }

                            withStyle(
                                SpanStyle(
                                    color = MaterialTheme.colors.onSecondary
                                )
                            ) {
                                append(it)
                            }

                        },
                        style = MaterialTheme.typography.caption,
                    )

                    Spacer(
                        modifier = Modifier
                            .height(1.dp)
                            .padding(
                                horizontal = 16.dp
                            )
                            .fillMaxWidth()
                            .background(
                                color = GRAY_50,
                                shape = DottedShape(step = 10.dp)
                            )
                    )

                }
            }

            OrderItemsRowTexts(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                texts = listOf(
                    stringResource(R.string.total_price),
                    "", "", "",
                    total?.let {
                        "$$total"
                    } ?: "$${items.calcTotalPrice()}",
                ),
                isHeader = true
            )

        }
    }

}

@Composable
fun MenuFoodItem(
    modifier: Modifier,
    item: MenuResponse.Food,
    index: Int,
    onPreparationTimeClick: () -> Unit,
    onPriceClick: () -> Unit,
    onAvailableClick: (Boolean) -> Unit,
) {
    var checkedState by remember {
        mutableStateOf(item.active)
    }

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier,
        elevation = 2.dp
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
        ) {

            ItemTitle(
                modifier = Modifier
                    .weight(1.0f)
                    .align(Alignment.CenterVertically),
                text = index.toString(),
                color = MaterialTheme.colors.onSecondary
            )
            ItemTitle(
                modifier = Modifier
                    .weight(1.0f)
                    .align(Alignment.CenterVertically),
                text = item.name,
                color = MaterialTheme.colors.onSecondary
            )

            Row(modifier = Modifier
                .weight(1.0f)
                .align(Alignment.CenterVertically)
                .fillMaxHeight()
                .clickable { onPreparationTimeClick() }
            ) {
                ItemTitle(
                    text = item.preparationTime.toReadableDuration(),
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    color = MaterialTheme.colors.onSecondary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    painter = painterResource(R.drawable.ic_edit_blue),
                    contentDescription = "",
                    tint = LINK_BLUE
                )
            }

            Row(modifier = Modifier
                .weight(1.0f)
                .fillMaxHeight()
                .align(Alignment.CenterVertically)
                .clickable { onPriceClick() }
            ) {
                ItemTitle(
                    text = "$${item.price}",
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    color = MaterialTheme.colors.onSecondary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    painter = painterResource(R.drawable.ic_edit_blue),
                    contentDescription = "",
                    tint = LINK_BLUE
                )
            }

            Box(
                modifier = Modifier
                    .weight(1.0f)
            ) {
                Switch(
                    checked = checkedState,
                    onCheckedChange = {
                        checkedState = it
                        onAvailableClick(it)
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd),
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colors.primary,
                        checkedTrackColor = GREEN_100,
                        uncheckedThumbColor = GRAY_300,
                        uncheckedTrackColor = GRAY_200
                    )
                )
            }

        }
    }

    Spacer(Modifier.height(12.dp))
}


@Composable
fun DeliverCostItem(
    modifier: Modifier,
    item: DeliverCostResponse.Embedded.Cost,
    index: Int,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Box(
        modifier = modifier,
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                )
        ) {

            ItemTitle(
                modifier = Modifier
                    .weight(1.0f)
                    .align(Alignment.CenterVertically),
                text = index.toString(),
                color = MaterialTheme.colors.onSecondary
            )
            ItemTitle(
                modifier = Modifier
                    .weight(1.0f)
                    .align(Alignment.CenterVertically),
                text = item.cost.toPrice(),
                color = MaterialTheme.colors.onSecondary
            )

            Row(modifier = Modifier
                .weight(1.0f)
                .align(Alignment.CenterVertically)
                .fillMaxHeight()
                .clickable { onEditClick() }
            ) {
                ItemTitle(
                    text = "${item.lowerRadius} to ${item.upperRadius}km",
                    modifier = Modifier
                        .align(Alignment.CenterVertically),
                    color = MaterialTheme.colors.onSecondary
                )

                Spacer(modifier = Modifier.width(8.dp))


                Icon(
                    painter = painterResource(R.drawable.ic_edit_blue),
                    contentDescription = "",
                    tint = LINK_BLUE,
                )
            }

            Column(
                modifier = Modifier
                    .weight(1.0f)
                    .align(Alignment.CenterVertically)
                    .fillMaxHeight()
                    .clickable { onDeleteClick() },
            ) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.End),
                    painter = painterResource(R.drawable.ic_delete),
                    tint = MaterialTheme.colors.error,
                    contentDescription = ""
                )
            }
        }
    }

    Spacer(Modifier.height(12.dp))
}
