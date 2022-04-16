package com.mindlab.intrest_agent_android.presentation.menu

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mindlab.intrest_agent_android.data.model.Menu
import com.mindlab.intrest_agent_android.data.network.response.FoodEdibleResponse
import com.mindlab.intrest_agent_android.data.network.response.MenuResponse
import com.mindlab.intrest_agent_android.domain.menu.MenuRepo
import com.mindlab.intrest_agent_android.presentation.base.BaseViewModel
import com.mindlab.intrest_agent_android.presentation.components.events.ViewModelState
import com.mindlab.intrest_agent_android.presentation.components.util.InputValidator
import com.mindlab.intrest_agent_android.presentation.components.util.InputWrapper
import com.mindlab.intrest_agent_android.utils.Status
import com.mindlab.intrest_agent_android.utils.events.MenuEvents
import com.mindlab.intrest_agent_android.utils.extention.getStateFlow
import com.mindlab.intrest_agent_android.utils.mapper.MenuMapper.categoryMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

/**
 * Created by Alireza Nezami on 1/9/2022.
 */
@HiltViewModel
class MenuViewModel
@Inject constructor(
    private val repo: MenuRepo,
    handle: SavedStateHandle,
) : BaseViewModel<ViewModelState<List<MenuResponse>>>(
    ViewModelState.Loading(
        true
    )
) {

    val dialogTime = handle.getStateFlow(viewModelScope, "time", InputWrapper())
    val dialogPrice = handle.getStateFlow(viewModelScope, "price", InputWrapper())

    private val _events = Channel<MenuEvents>(Channel.UNLIMITED)
    val events =
        _events.consumeAsFlow().shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    private var menus = repo.getMenu()

    private lateinit var menuList: List<MenuResponse>

    val categories: MutableStateFlow<List<Menu>> = MutableStateFlow(listOf())
    val subCategories: MutableStateFlow<List<Menu>> = MutableStateFlow(listOf())
    val foods: MutableStateFlow<List<MenuResponse.Food>> = MutableStateFlow(listOf())

    init {
        observe()
        getEvents()
    }

    private fun getEvents() {

    }

    private fun observe() {
        viewModelScope.launch {
            _events.send(MenuEvents.CategoryLoading(true))

            menus.collect { response ->
                if (response.status == Status.SUCCESS && response.data != null) {
                    menuList = response.data
                    filterCategories(menuList)
                } else {
                    handleError(response)
                }
            }
        }
    }

    private suspend fun filterCategories(response: List<MenuResponse>) {
        categories.value =
            response.map {
                Menu(
                    id = it.id,
                    title = it.title
                )
            }

        _events.send(MenuEvents.CategoryLoading(false))
    }

    fun onCategorySelection(id: Int) {
        viewModelScope.launch {
            _events.send(MenuEvents.FoodLoading(true))
            _events.send(MenuEvents.SubCategoryLoading(true))

            launch {
                subCategories.value =
                    categoryMapper(menuList.first { it.id == id }.categories.orEmpty())
                _events.send(MenuEvents.SubCategoryLoading(false))
            }
            foods.value = menuList.first { it.id == id }.foods ?: foods.value
            _events.send(MenuEvents.FoodLoading(false))

        }
    }

    fun onSubCategorySelection(id: Int, parentId: Int) {
        viewModelScope.launch {
            _events.send(MenuEvents.FoodLoading(true))
            menuList.first { it.id == parentId }.categories?.let { categories ->
                foods.value = categories.first { it.id == id }.foods.orEmpty()
            }
            _events.send(MenuEvents.FoodLoading(false))
        }
    }

    fun onFoodAvailabilityChange(item: MenuResponse.Food, availability: Boolean) {
        viewModelScope.launch {
            repo.changeFoodAvailability(item.id, availability).collect { response ->
                if (response.status == Status.SUCCESS) {
                    updateState(ViewModelState.SuccessAction(null, Random.nextInt()))
                } else {
                    updateState(ViewModelState.Error(response.message))
                }
            }
        }
    }

    fun onAdjustPrice( id: Int) {
        viewModelScope.launch {
            repo.adjustFoodPrice(dialogPrice.value.value.toInt(), id).collect { response ->
                if (response.status == Status.SUCCESS) {
                    response.data?.let {
                        updateFoodPrice(id, response.data.price)
                    }
                    updateState(ViewModelState.SuccessAction(null, Random.nextInt()))
                } else {
                    updateState(ViewModelState.Error(response.message))
                }
            }
        }
    }

    fun onPreparationTime(id: Int) {
        viewModelScope.launch {
            repo.adjustPreparationTime(dialogTime.value.value.toInt(), id).collect { response ->
                if (response.status == Status.SUCCESS) {
                    updateState(ViewModelState.SuccessAction(null, Random.nextInt()))
                    clearDialogInputs()
                    response.data?.let {
                        updatePreparationTime(response.data)
                    }
                } else {
                    updateState(ViewModelState.Error(response.message))
                }
            }
        }
    }

    private fun updatePreparationTime(item: FoodEdibleResponse) {
        viewModelScope.launch {
            foods.updateAndGet { list ->
                list.map { food ->
                    food.takeIf { it.id == item.id }?.copy(preparationTime = item.preparationTime)
                        ?: food
                }
            }
        }
    }

    private fun updateFoodPrice(id: Int, price: Double) {
        viewModelScope.launch {
            foods.updateAndGet { list ->
                list.map { food ->
                    food.takeIf { it.id == id }?.copy(price = price)
                        ?: food
                }
            }
        }
    }

    fun onTimeEnter(input: String) {
        val errorId = InputValidator.getTimeErrorIdOrNull(input)
        dialogTime.value = dialogTime.value.copy(value = input, error = errorId)
    }

    fun onPriceEnter(input: String) {
        val errorId = InputValidator.getPriceAdjErrorIdOrNull(input)
        dialogPrice.value = dialogPrice.value.copy(value = input, error = errorId)
    }

    fun clearDialogInputs() {
        dialogTime.value = InputWrapper()
        dialogPrice.value = InputWrapper()
    }

}