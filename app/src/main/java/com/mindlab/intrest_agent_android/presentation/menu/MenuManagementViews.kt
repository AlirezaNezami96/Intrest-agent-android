package com.mindlab.intrest_agent_android.presentation.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.mindlab.intrest_agent_android.R
import com.mindlab.intrest_agent_android.data.network.response.MenuResponse
import com.mindlab.intrest_agent_android.presentation.components.*
import com.mindlab.intrest_agent_android.presentation.components.events.ViewModelState
import com.mindlab.intrest_agent_android.presentation.components.theme.GRAY_150
import com.mindlab.intrest_agent_android.utils.events.MenuEvents
import kotlinx.coroutines.flow.collect
import timber.log.Timber

/**
 * Created by Alireza Nezami on 1/9/2022.
 */
@Composable
fun MenuManagementViews(
    viewModel: MenuViewModel = hiltViewModel()
) {
    val state = viewModel.state().collectAsState(initial = ViewModelState.Loading())

    val categories = viewModel.categories.collectAsState(listOf())
    val subCategories = viewModel.subCategories.collectAsState(listOf())
    val foods = viewModel.foods.collectAsState(listOf())

    var subCategoryLoading by remember { mutableStateOf(false) }
    var foodsLoading by remember { mutableStateOf(false) }
    var categoryLoading by remember { mutableStateOf(false) }

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
                is MenuEvents.CategoryLoading ->
                    categoryLoading = event.show
                is MenuEvents.SubCategoryLoading ->
                    subCategoryLoading = event.show
                is MenuEvents.FoodLoading ->
                    foodsLoading = event.show
//                is MenuEvents.FoodUpdated ->


            }
        }
    }

    var showPreparationTimeDialog by remember { mutableStateOf(-1) }
    var showAdjustPriceDialog by remember { mutableStateOf(-1) }

    Timber.d("categories size is: ${foods.value.size}")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colors.secondary
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 40.dp, vertical = 16.dp)
        ) {

            Row(Modifier.fillMaxWidth()) {

                ChooseItemDropDown(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    enabled = !categoryLoading,
                    label = stringResource(id = R.string.menu),
                    placeHolder = stringResource(id = R.string.choose_your_menu),
                    items = categories.value
                ) {
                    viewModel.onCategorySelection(it.id)
                }

                Spacer(modifier = Modifier.width(16.dp))

                ChooseItemDropDown(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    enabled = !subCategoryLoading,
                    canBeEmpty = true,
                    label = stringResource(id = R.string.categories),
                    placeHolder = stringResource(id = R.string.choose_your_menu),
                    items = subCategories.value,
                    onParentSelect = { viewModel.onCategorySelection(it) },
                    onSelect = { viewModel.onSubCategorySelection(it.id, it.parentId) }
                )

                Spacer(
                    modifier = Modifier
                        .weight(0.6f)
                )

            }

            Spacer(modifier = Modifier.height(35.dp))

            Content(
                modifier = Modifier.fillMaxSize(),
                foods = foods.value,
                onPreparationTimeClick = { showPreparationTimeDialog = it.id },
                onPriceClick = { showAdjustPriceDialog = it.id },
                onAvailableClick = { item, availability ->
                    viewModel.onFoodAvailabilityChange(item, availability)
                }
            )
        }

        AdjustPriceDialog(
            modifier = Modifier.fillMaxWidth(),
            itemId = showAdjustPriceDialog,
            onDismiss = {
                viewModel.clearDialogInputs()
                showAdjustPriceDialog = -1
            },
            viewModel = viewModel
        )

        PreparationTimeDialog(
            modifier = Modifier.fillMaxWidth(),
            itemId = showPreparationTimeDialog,
            viewModel = viewModel,
            onDismiss = {
                viewModel.clearDialogInputs()
                showPreparationTimeDialog = -1
            },
        )

        CustomNotification(
            modifier = Modifier.fillMaxWidth(),
            state = state.value
        )
    }
}

@Composable
fun Content(
    modifier: Modifier,
    foods: List<MenuResponse.Food>,
    onPreparationTimeClick: (MenuResponse.Food) -> Unit,
    onPriceClick: (MenuResponse.Food) -> Unit,
    onAvailableClick: (MenuResponse.Food, Boolean) -> Unit,
) {

    LazyColumn(modifier = modifier) {
        item {
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
                    stringResource(R.string.food_name),
                    stringResource(R.string.preparation_time),
                    stringResource(R.string.price),
                    stringResource(R.string.available),
                ),
                isHeader = true,
                isLastOneAction = true
            )
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            if (foods.isEmpty()) {
                EmptyOrderList(
                    text = stringResource(R.string.menu_is_empty),
                    modifier = Modifier.fillParentMaxSize()
                )
            }
        }
        itemsIndexed(foods) { index, item ->
            MenuFoodItem(
                modifier = Modifier.fillMaxWidth(),
                item = item,
                index = index + 1,
                onPreparationTimeClick = {
                    onPreparationTimeClick(item)
                },
                onAvailableClick = { availability ->
                    onAvailableClick(item, availability)
                },
                onPriceClick = {
                    onPriceClick(item)
                }
            )
        }


    }

}