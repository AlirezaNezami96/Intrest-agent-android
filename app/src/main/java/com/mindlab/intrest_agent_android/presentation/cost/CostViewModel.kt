package com.mindlab.intrest_agent_android.presentation.cost

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mindlab.intrest_agent_android.data.network.response.DeliverCostResponse
import com.mindlab.intrest_agent_android.domain.cost.DeliverCostRepo
import com.mindlab.intrest_agent_android.presentation.base.BaseViewModel
import com.mindlab.intrest_agent_android.presentation.components.events.ViewModelState
import com.mindlab.intrest_agent_android.presentation.components.util.InputValidator
import com.mindlab.intrest_agent_android.presentation.components.util.InputWrapper
import com.mindlab.intrest_agent_android.utils.Status
import com.mindlab.intrest_agent_android.utils.events.DeliverCostEvents
import com.mindlab.intrest_agent_android.utils.extention.getStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Alireza Nezami on 1/15/2022.
 */
@HiltViewModel
class CostViewModel
@Inject constructor(
    private val repo: DeliverCostRepo,
    handle: SavedStateHandle,
) : BaseViewModel<ViewModelState<List<DeliverCostResponse.Embedded.Cost>>>(
    ViewModelState.Loading(
        true
    )
) {

    val dialogCost = handle.getStateFlow(viewModelScope, "cost", InputWrapper())
    val dialogLower = handle.getStateFlow(viewModelScope, "lower", InputWrapper())
    val dialogUpper = handle.getStateFlow(viewModelScope, "upper", InputWrapper())

    val areInputsValid = combine(dialogCost, dialogLower, dialogUpper) { cost, lower, upper ->
        cost.value.isNotEmpty() && cost.error == null &&
                lower.value.isNotEmpty() && lower.error == null &&
                upper.value.isNotEmpty() && upper.error == null
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val costsList: MutableStateFlow<List<DeliverCostResponse.Embedded.Cost>> =
        MutableStateFlow(listOf())

    private val _events = Channel<DeliverCostEvents>(Channel.UNLIMITED)
    val events =
        _events.consumeAsFlow().shareIn(viewModelScope, SharingStarted.WhileSubscribed())


    init {
        observe()
    }

    private fun observe() {
        async {
            updateState(ViewModelState.Loading(true))
            repo.getDeliverCost().collect { response ->
                if (response.status == Status.SUCCESS && response.data != null) {
                    costsList.value = response.data.embedded.costs
                    updateState(ViewModelState.Loaded(costsList.value))
                } else {
                    updateState(ViewModelState.Error(response.message))
                }
            }
        }

    }

    fun deleteCost(id: Int) {
        viewModelScope.launch {
            costsList.updateAndGet { list ->
                list.filter { it.id != id }
            }
        }
//        costsList.value = costsList.value.filter { it.id != id }
//        repo.deleteCost(costsList.value)
    }

    fun updateItem(cost: String, lower: String, upper: String, id: Int) {
        viewModelScope.launch {
            costsList.updateAndGet { list ->
                list.map { oldCost ->
                    oldCost.takeIf { it.id == id || it.tempId == id }?.copy(
                        id = id,
                        cost = cost.toDouble(),
                        lowerRadius = lower.toInt(),
                        upperRadius = upper.toInt()
                    ) ?: oldCost
                }
            }

            clearDialogInputs()
        }
    }

    fun addCost(id: Int) {
        viewModelScope.launch {
            costsList.updateAndGet { list ->
                list.toMutableList().apply {
                    add(
                        list.size,
                        DeliverCostResponse.Embedded.Cost(
                            tempId = id,
                            cost = dialogCost.value.value.toDouble(),
                            lowerRadius = dialogLower.value.value.toInt(),
                            upperRadius = dialogUpper.value.value.toInt(),
                        )
                    )
                }
            }

            clearDialogInputs()
        }
    }

    fun showEditDialog(item: DeliverCostResponse.Embedded.Cost) {
        viewModelScope.launch {
            _events.send(DeliverCostEvents.ShowEditDialog(true, item))
        }
    }

    fun showAddDialog() {
        viewModelScope.launch {
            _events.send(DeliverCostEvents.ShowAddDialog(true))
        }
    }


    fun onCostEnter(input: String) {
        val errorId = InputValidator.getPriceAdjErrorIdOrNull(input)
        dialogCost.value = dialogCost.value.copy(value = input, error = errorId)
    }

    fun onLowerEnter(input: String) {
        val errorId = InputValidator.getLowerRadiusErrorIdOrNull(input)
        dialogLower.value = dialogLower.value.copy(value = input, error = errorId)
    }

    fun onUpperEnter(input: String) {
        val errorId = InputValidator.getLowerUpperErrorIdOrNull(input)
        dialogUpper.value = dialogLower.value.copy(value = input, error = errorId)
    }

    fun clearDialogInputs() {
        dialogUpper.value = InputWrapper()
        dialogLower.value = InputWrapper()
        dialogCost.value = InputWrapper()
    }

    fun sync() {
        viewModelScope.launch {
            repo.sync(costsList.value).collect { response ->
                if (response.status == Status.SUCCESS) {
                    updateState(
                        ViewModelState.SuccessAction(null)
                    )
                } else {
                    updateState(ViewModelState.Error(response.message))
                }
            }
        }
    }

}