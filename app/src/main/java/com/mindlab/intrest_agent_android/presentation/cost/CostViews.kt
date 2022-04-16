package com.mindlab.intrest_agent_android.presentation.cost

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.mindlab.intrest_agent_android.R
import com.mindlab.intrest_agent_android.data.network.response.DeliverCostResponse
import com.mindlab.intrest_agent_android.presentation.components.*
import com.mindlab.intrest_agent_android.presentation.components.events.ViewModelState
import com.mindlab.intrest_agent_android.presentation.components.theme.GRAY_150
import com.mindlab.intrest_agent_android.presentation.components.theme.LINK_BLUE
import com.mindlab.intrest_agent_android.utils.events.DeliverCostEvents
import kotlinx.coroutines.flow.collect
import timber.log.Timber

/**
 * Created by Alireza Nezami on 1/15/2022.
 */
@Composable
fun DeliverViews(
    viewModel: CostViewModel = hiltViewModel()
) {
    val state = viewModel.state().collectAsState(initial = ViewModelState.Loading())

    val costsList = viewModel.costsList.collectAsState()
    var showDeleteCostDialog by remember { mutableStateOf(-1) }
    var showEditDialog by remember { mutableStateOf(DeliverCostResponse.Embedded.Cost()) }
    var showAddDialog by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

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
                is DeliverCostEvents.ShowEditDialog -> {
                    Timber.d("ShowEditDialog ${event.item}")
                    if (event.show)
                        showEditDialog = event.item
                }
                is DeliverCostEvents.ShowAddDialog -> {
                    showAddDialog = event.show
                }

            }
        }
    }

    Box(
        modifier = Modifier
            .padding(
                horizontal = 40.dp,
                vertical = 32.dp
            )
            .background(
                color = MaterialTheme.colors.secondary
            )
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)

        ) {

            EqualRowTexts(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(5.dp))
                    .background(
                        color = GRAY_150
                    ),
                texts =
                listOf(
                    stringResource(R.string.hashtag_sign),
                    stringResource(R.string.costs),
                    stringResource(R.string.range_radius),
                    "",
                ),
                isHeader = true
            )

            if (costsList.value.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()){
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.CenterHorizontally)
                        ) {

                            Image(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(16.dp),
                                painter = painterResource(R.drawable.ic_group),
                                contentDescription = ""
                            )

                            ItemTitle(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(bottom = 16.dp),
                                text = stringResource(R.string.no_item_here),
                            )
                        }
                    }


                    Box(
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colors.background,
                                shape = MaterialTheme.shapes.large
                            )
                            .fillMaxWidth()
                            .clickable {
                                viewModel.showAddDialog()
                            },
                    ) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(vertical = 16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_plus),
                                contentDescription = "",
                                modifier = Modifier.align(Alignment.CenterVertically),
                                tint = LINK_BLUE
                            )

                            ButtonTitle(
                                modifier = Modifier,
                                text = stringResource(R.string.add_new_item),
                                color = LINK_BLUE
                            )
                        }
                    }
                }

            } else {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(
                                horizontal = 16.dp,
                                vertical = 16.dp
                            )
                    ) {
                        costsList.value.forEachIndexed { index, item ->
                            DeliverCostItem(
                                modifier = Modifier.fillMaxSize(),
                                item = item,
                                index = index + 1,
                                onEditClick = {
                                    viewModel.showEditDialog(item)
                                },
                                onDeleteClick = {
                                    showDeleteCostDialog = item.id
                                }
                            )
                        }

                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colors.secondary,
                                    shape = MaterialTheme.shapes.large
                                )
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.showAddDialog()
                                },
                        ) {
                            Row(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .padding(vertical = 16.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_plus),
                                    contentDescription = "",
                                    modifier = Modifier.align(Alignment.CenterVertically),
                                    tint = LINK_BLUE
                                )

                                ButtonTitle(
                                    modifier = Modifier,
                                    text = stringResource(R.string.add_new_item),
                                    color = LINK_BLUE
                                )
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                PrimaryButton(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .align(Alignment.CenterEnd),
                    text = stringResource(R.string.apply_changes),
                    onClick = {
                        viewModel.sync()
                    }
                )
            }
        }

        DeleteCostDialog(
            modifier = Modifier,
            itemId = showDeleteCostDialog,
            onSubmit = viewModel::deleteCost,
            onDismiss = { showDeleteCostDialog = -1 }
        )

        EditCostDialog(
            modifier = Modifier.fillMaxWidth(),
            item = showEditDialog,
            viewModel = viewModel,
            onDismiss = {
                viewModel.clearDialogInputs()
                showEditDialog = DeliverCostResponse.Embedded.Cost()
            },
            onPositiveDismiss = {
                showEditDialog = DeliverCostResponse.Embedded.Cost()
            }
        )

        AddCostDialog(
            modifier = Modifier.fillMaxWidth(),
            viewModel = viewModel,
            show = showAddDialog,
            onDismiss = {
                viewModel.clearDialogInputs()
                showAddDialog = false
            },
            onPositiveDismiss = {
                showAddDialog = false
            }
        )

        StateLoading(
            state = state.value,
            modifier = Modifier.fillMaxSize()
        )

        CustomNotification(
            modifier = Modifier.fillMaxWidth(),
            state = state.value
        )
    }
}