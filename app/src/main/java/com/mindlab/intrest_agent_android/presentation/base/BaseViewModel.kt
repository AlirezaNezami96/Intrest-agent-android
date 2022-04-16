package com.mindlab.intrest_agent_android.presentation.base

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mindlab.intrest_agent_android.data.network.response.DeliverCostResponse
import com.mindlab.intrest_agent_android.presentation.components.events.ViewModelState
import com.mindlab.intrest_agent_android.utils.Resource
import com.mindlab.intrest_agent_android.utils.Status
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Alireza Nezami on 12/26/2021.
 */
open class BaseViewModel<S>(
    private val initialState: S,
) : ViewModel(), LifecycleObserver {

    companion object {
        val dispatcher: CoroutineDispatcher = Dispatchers.Default
    }

    private var _state: MutableStateFlow<S> = MutableStateFlow(initialState)
    fun state(): StateFlow<S> = _state

    fun currentState(): S = _state.value

    protected fun updateState(newState: S) {
        _state.value = newState
    }

    protected inline fun async(crossinline block: suspend () -> Unit) =
        viewModelScope.launch {
            block()
        }

    protected suspend inline fun <T> await(crossinline block: suspend () -> T): T =
        withContext(dispatcher) { block() }

    protected inline fun asyncFlow(crossinline block: suspend () -> Unit) = async {
        withContext(dispatcher) {
            block()
        }
    }

    val noNetwork = MutableStateFlow(false)

    val logout = MutableStateFlow(false)

    fun <T> handleError(response: Resource<T>) {

    }


    fun handleException(it: Throwable) {

    }
}