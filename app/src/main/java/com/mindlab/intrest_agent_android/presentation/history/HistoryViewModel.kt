package com.mindlab.intrest_agent_android.presentation.history

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mindlab.intrest_agent_android.data.network.response.DeliverCostResponse
import com.mindlab.intrest_agent_android.data.network.response.IncomingResponse
import com.mindlab.intrest_agent_android.domain.history.HistoryRepo
import com.mindlab.intrest_agent_android.presentation.base.BaseViewModel
import com.mindlab.intrest_agent_android.presentation.components.events.ViewModelState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

/**
 * Created by Alireza Nezami on 12/19/2021.
 */


sealed class NewEvents() {
    data class ChangeTab(val index: Int) : NewEvents()
    data class AcceptOrder(val id: Int) : NewEvents()
    data class RejectOrder(val id: Int, val rejectReason: String) : NewEvents()
    data class PriceAdjustment(val id: Int) : NewEvents()
    data class DelayOrder(val id: Int, val time: String) : NewEvents()
}


@HiltViewModel
class HistoryViewModel
@Inject constructor(
    repo: HistoryRepo,
) : BaseViewModel<ViewModelState<List<DeliverCostResponse.Embedded.Cost>>>(
    ViewModelState.Loading(
        true
    )
) {

    val historyList: Flow<PagingData<IncomingResponse.Content>> =
        repo.getHistory().cachedIn(viewModelScope)

    private val _expandedCardIdsList = MutableStateFlow(mutableListOf<Int>())
    val expandedCardIdsList: StateFlow<MutableList<Int>> get() = _expandedCardIdsList

    fun onExpandCard(id: Int) {
        _expandedCardIdsList.value = _expandedCardIdsList.value.toMutableList().also {
            if (it.contains(id)) it.remove(id) else it.add(id)
        }
    }
}