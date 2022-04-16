package com.mindlab.intrest_agent_android.presentation.home

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mindlab.intrest_agent_android.data.network.response.DeliverCostResponse
import com.mindlab.intrest_agent_android.domain.settings.SettingsRepo
import com.mindlab.intrest_agent_android.domain.user.UserLocalSource
import com.mindlab.intrest_agent_android.presentation.base.BaseViewModel
import com.mindlab.intrest_agent_android.presentation.components.events.ViewModelState
import com.mindlab.intrest_agent_android.utils.Status
import com.mindlab.intrest_agent_android.utils.extention.fromBase64toBitmap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/**
 * Created by Alireza Nezami on 1/19/2022.
 */
@HiltViewModel
class HomeViewModel
@Inject constructor(
    private val userLocalSource: UserLocalSource,
    private val repo: SettingsRepo
) : BaseViewModel<ViewModelState<List<DeliverCostResponse.Embedded.Cost>>>(
    ViewModelState.Loading(
        true
    )
) {


    val logo: MutableLiveData<Bitmap> = MutableLiveData()
    val name: MutableLiveData<String> = MutableLiveData()
    val receivingOrders = MutableLiveData(false)

    val shouldLogout = MutableLiveData(false)


    private val user = userLocalSource.user().map { user ->
        if (user == null) logout.value = true
        else {
            name.value = user.restaurantName
            if (user.image.isNotEmpty()) {
                logo.value = user.image.fromBase64toBitmap()
            }

        }
    }.launchIn(viewModelScope)

    init {
        getSettings()
    }

    fun onStopCheckChange() {
        viewModelScope.launch {
            repo.changeReceivingOrder(receivingOrders.value!!).collect { resource ->
                if (resource.status == Status.SUCCESS && resource.data != null) {
                    receivingOrders.value = resource.data.availableService
                } else {
                    receivingOrders.value = !receivingOrders.value!!
                    handleError(resource)
                }
            }
        }
    }

    private fun getSettings() {
        viewModelScope.launch {
            repo.getSettings().collect { resource ->
                if (resource.status == Status.SUCCESS && resource.data != null) {
                    receivingOrders.value = resource.data.availableService
                } else {
                    receivingOrders.value = !receivingOrders.value!!
                    handleError(resource)
                }
            }
        }
    }

    fun logOut() {
        runBlocking {
            userLocalSource.clear()
        }
        shouldLogout.value = true
    }

}