package com.mindlab.intrest_agent_android.presentation.orders.order

import androidx.compose.ui.focus.FocusDirection
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mindlab.intrest_agent_android.data.network.body.AdjustPriceBody
import com.mindlab.intrest_agent_android.data.network.response.DeliverCostResponse
import com.mindlab.intrest_agent_android.data.network.response.IncomingResponse
import com.mindlab.intrest_agent_android.domain.orders.remote.OrdersRepo
import com.mindlab.intrest_agent_android.presentation.base.BaseViewModel
import com.mindlab.intrest_agent_android.presentation.components.events.ViewModelState
import com.mindlab.intrest_agent_android.presentation.components.util.FocusedTextFieldKey
import com.mindlab.intrest_agent_android.presentation.components.util.InputValidator
import com.mindlab.intrest_agent_android.presentation.components.util.InputWrapper
import com.mindlab.intrest_agent_android.presentation.components.util.ScreenEvent
import com.mindlab.intrest_agent_android.utils.DeliverType
import com.mindlab.intrest_agent_android.utils.Status
import com.mindlab.intrest_agent_android.utils.events.OrderEvents
import com.mindlab.intrest_agent_android.utils.extention.getStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber.Forest.d
import javax.inject.Inject
import kotlin.random.Random

/**
 * Created by Alireza Nezami on 12/19/2021.
 */


@HiltViewModel
class OrderViewModel
@Inject constructor(
    private val handle: SavedStateHandle,
    private val repo: OrdersRepo,
) : BaseViewModel<ViewModelState<List<DeliverCostResponse.Embedded.Cost>>>(
    ViewModelState.Loading(
        true
    )
) {

    val incomingList: Flow<PagingData<IncomingResponse.Content>> =
        repo.getIncoming().cachedIn(viewModelScope)
    val inProgressList: Flow<PagingData<IncomingResponse.Content>> =
        repo.getInProgress().cachedIn(viewModelScope)
    val deliverList: Flow<PagingData<IncomingResponse.Content>> =
        repo.getDeliver().cachedIn(viewModelScope)

    val tabIndex = MutableStateFlow(0)

    val adjustmentPrice = handle.getStateFlow(viewModelScope, "price_adj", InputWrapper())
    val adjustTaxRate = handle.getStateFlow(viewModelScope, "tax_rate", InputWrapper())
    val adjustDescription =
        handle.getStateFlow(viewModelScope, "adjust_description", InputWrapper())


    val loading = handle.getStateFlow(viewModelScope, "loading", false)

    val areInputsValid = combine(adjustmentPrice, adjustTaxRate) { price, rate ->
        price.value.isNotEmpty() && price.error == null &&
                rate.value.isNotEmpty() && rate.error == null
    }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _expandedCardIdsList = MutableStateFlow(mutableListOf<Int>())
    val expandedCardIdsList: StateFlow<MutableList<Int>> get() = _expandedCardIdsList

    private val _screenEvents = Channel<ScreenEvent>(Channel.UNLIMITED)
    val screenEvents =  _screenEvents.consumeAsFlow().shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    private val _events = Channel<OrderEvents>(Channel.UNLIMITED)
    val events =
        _events.consumeAsFlow().shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    private var _orderState: MutableStateFlow<OrderViewState> =
        MutableStateFlow(OrderViewState.Nothing)
    val orderState: StateFlow<OrderViewState> = _orderState

    private lateinit var refreshJob: Job
    private fun getEvents() {
        events
            .filterIsInstance<OrderEvents.ChangeTab>()
            .map {
                tabIndex.value = it.index
                d("tab changed to ${it.index}")
            }
            .launchIn(viewModelScope)

        events
            .filterIsInstance<OrderEvents.AcceptOrder>()
            .map {
                repo.acceptOrderFlow(it.id)
                    .collect { response ->
                        if (response.status == Status.SUCCESS) {
                            updateState(ViewModelState.SuccessAction(null, Random.nextInt()))
                            refreshIncoming()
                            refreshInProgress()
                        } else {
                            updateState(ViewModelState.Error(response.message))
                        }

                    }
            }
            .launchIn(viewModelScope)

        events
            .filterIsInstance<OrderEvents.RejectOrder>()
            .map {
                repo.rejectOrder(it.id, it.rejectReason)
                    .collect { response ->
                        if (response.status == Status.SUCCESS) {
                            updateState(ViewModelState.SuccessAction(null, Random.nextInt()))
                            refreshIncoming()
                        } else {
                            updateState(ViewModelState.Error(response.message))
                        }
                    }
            }
            .launchIn(viewModelScope)

        events
            .filterIsInstance<OrderEvents.DelayOrder>()
            .map {
                repo.delayOrder(it.id, it.time)
                    .collect { response ->
                        if (response.status == Status.SUCCESS) {
                            refreshInProgress()
                            updateState(ViewModelState.SuccessAction(null, Random.nextInt()))
                        } else {
                            updateState(ViewModelState.Error(response.message))
                        }
                    }
            }
            .launchIn(viewModelScope)

        events
            .filterIsInstance<OrderEvents.PreparedOrder>()
            .map {
                repo.prepareOrder(it.id)
                    .collect { response ->
                        if (response.status == Status.SUCCESS) {
                            refreshInProgress()
                            refreshDeliver()
                            updateState(ViewModelState.SuccessAction(null, Random.nextInt()))
                        } else {
                            updateState(ViewModelState.Error(response.message))
                        }
                    }
            }
            .launchIn(viewModelScope)

        events
            .filterIsInstance<OrderEvents.DeliverByCourier>()
            .map {
                repo.deliverByCourier(it.id)
                    .collect { response ->
                        if (response.status == Status.SUCCESS) {
                            refreshDeliver()
                            updateState(ViewModelState.SuccessAction(null, Random.nextInt()))
                        } else {
                            updateState(ViewModelState.Error(response.message))
                        }
                    }
            }
            .launchIn(viewModelScope)

        events
            .filterIsInstance<OrderEvents.PickupByCustomer>()
            .map {
                repo.deliverByCourier(it.id)
                    .collect { response ->
                        if (response.status == Status.SUCCESS) {
                            refreshDeliver()
                            updateState(ViewModelState.SuccessAction(null, Random.nextInt()))
                        } else {
                            updateState(ViewModelState.Error(response.message))
                        }
                    }
            }
            .launchIn(viewModelScope)

        events
            .filterIsInstance<OrderEvents.PriceAdjustment>()
            .map { order ->
                repo.adjustPrice(
                    AdjustPriceBody(
                        invoiceId = order.id,
                        adjustedAmount = adjustmentPrice.value.value.toInt(),
                        adjustedTax = adjustTaxRate.value.value.toInt(),
                        description = adjustDescription.value.value
                    )

                )
                    .collect { response ->
                        if (response.status == Status.SUCCESS) {
                            refreshInProgress()
                            updateState(ViewModelState.SuccessAction(null, Random.nextInt()))
                            adjustDescription.value = InputWrapper()
                            adjustmentPrice.value = InputWrapper()
                            adjustTaxRate.value = InputWrapper()
                        } else {
                            updateState(ViewModelState.Error(response.message))
                        }
                    }
            }
            .launchIn(viewModelScope)
    }

   private fun updateOrderState(newState: OrderViewState) {
        _orderState.value = newState
    }

    fun changeTab(selected: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            _events.send(OrderEvents.ChangeTab(selected))
//            tabIndex.value = selected
        }
    }

    fun onExpandCard(id: Int) {
        _expandedCardIdsList.value = _expandedCardIdsList.value.toMutableList().also {
            if (it.contains(id)) it.remove(id) else it.add(id)
        }
    }

    fun rejectOrder(
        id: Int,
        reason: String
    ) {
        viewModelScope.launch {
            _events.send(OrderEvents.RejectOrder(id, reason))
        }
    }

    fun acceptOrder(item: IncomingResponse.Content) {
        viewModelScope.launch {
            _events.send(OrderEvents.AcceptOrder(item.id))
        }
    }

    fun deliverOrder(item: IncomingResponse.Content) {
        viewModelScope.launch {
            if (item.serveType == DeliverType.PICKUP)
                _events.send(OrderEvents.DeliverByCourier(item.id))
            else _events.send(OrderEvents.PickupByCustomer(item.id))
        }
    }

    fun delayOrder(id: Int, time: String) {
        viewModelScope.launch {
            val newTime = time.replace(" Minutes", "").trim().toInt()
            _events.send(OrderEvents.DelayOrder(id, newTime))
        }
    }

    fun prepareOrder(item: IncomingResponse.Content) {
        viewModelScope.launch {
            _events.send(OrderEvents.PreparedOrder(item.id))
        }
    }

    fun onPriceAdjustmentSubmit(invoice: IncomingResponse.Content.Invoice) {
        viewModelScope.launch {
            _events.send(OrderEvents.PriceAdjustment(invoice.id))
        }
    }

    /**
     * Input validators
     */
    fun onAdjustmentAmountEnter(input: String) {
        val errorId = InputValidator.getPriceAdjErrorIdOrNull(input)
        adjustmentPrice.value = adjustmentPrice.value.copy(value = input, error = errorId)
    }

    fun onTaxRateEnter(input: String) {
        val errorId = InputValidator.getTaxRateErrorIdOrNull(input)
        adjustTaxRate.value = adjustTaxRate.value.copy(value = input, error = errorId)
    }

    fun onAdjustmentDescriptionEnter(input: String) {
        adjustDescription.value = adjustDescription.value.copy(value = input, error = null)
    }

    private suspend fun refreshIncoming() {
        _events.send(OrderEvents.IncomingRefresh())
    }

    private suspend fun refreshInProgress() {
        _events.send(OrderEvents.InProgressRefresh())
    }

    private suspend fun refreshDeliver() {
        _events.send(OrderEvents.DeliverRefresh())
    }

    private fun startRefreshTimer() {
        refreshJob = viewModelScope.launch {
            delay(10000)
            if (tabIndex.value == INCOMING_TAB) {
                repo.isReceivingOrder().collect {
                    if (it) refreshIncoming()
                }
            }
            startRefreshTimer()
        }
    }

    private suspend fun clearFocusAndHideKeyboard() {
        _screenEvents.send(ScreenEvent.ClearFocus)
    }


    init {
        getEvents()
        startRefreshTimer()
    }

    companion object {
        const val INCOMING_TAB = 0
        const val IN_PROGRESS_TAB = 1
        const val DELIVER_TAB = 2
    }

    override fun onCleared() {
        super.onCleared()
        refreshJob.cancel()
    }

    fun onShowPriceAdjustment(item: IncomingResponse.Content.Invoice, trackingNumber: Int) {
        updateOrderState(
            OrderViewState.PriceAdjustment(
                item = item,
                trackingNumber = trackingNumber,
                show = true
            )
        )
    }

    fun onHidePriceAdjustment() {
        updateOrderState(
            OrderViewState.Nothing
        )
    }

    fun onNextImeActionClick() {
        viewModelScope.launch(Dispatchers.Default) {
            _screenEvents.send(ScreenEvent.MoveFocus(FocusDirection.Right))
        }
    }

    fun onDoneImeActionClick() {
        viewModelScope.launch(Dispatchers.Default) {
            if (areInputsValid.value) clearFocusAndHideKeyboard()
        }
    }

}