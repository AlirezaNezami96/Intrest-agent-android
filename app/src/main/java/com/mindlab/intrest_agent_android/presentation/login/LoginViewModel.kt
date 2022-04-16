package com.mindlab.intrest_agent_android.presentation.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mindlab.intrest_agent_android.data.model.Menu
import com.mindlab.intrest_agent_android.data.network.body.LoginBody
import com.mindlab.intrest_agent_android.data.network.response.DeliverCostResponse
import com.mindlab.intrest_agent_android.data.network.response.UserResponse
import com.mindlab.intrest_agent_android.domain.login.LoginRepo
import com.mindlab.intrest_agent_android.domain.orders.remote.OrdersRepoImpl
import com.mindlab.intrest_agent_android.presentation.base.BaseViewModel
import com.mindlab.intrest_agent_android.presentation.components.events.ViewModelState
import com.mindlab.intrest_agent_android.presentation.components.util.FocusedTextFieldKey
import com.mindlab.intrest_agent_android.presentation.components.util.InputValidator
import com.mindlab.intrest_agent_android.presentation.components.util.InputWrapper
import com.mindlab.intrest_agent_android.presentation.components.util.ScreenEvent
import com.mindlab.intrest_agent_android.utils.Status
import com.mindlab.intrest_agent_android.utils.extention.getStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.random.Random

sealed interface LoginAction {
    object Login : LoginAction
    object ChooseRestaurant : LoginAction
}


/**
 * Created by Alireza Nezami on 12/11/2021.
 */

@HiltViewModel
class LoginViewModel
@Inject constructor(
    private val handle: SavedStateHandle,
    private val loginRepo: LoginRepo,
    private val repo: OrdersRepoImpl,
) : BaseViewModel<ViewModelState<List<DeliverCostResponse.Embedded.Cost>>>(
    ViewModelState.Loading(
        true
    )
){

    val successLogin: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showChooseRestaurant: MutableStateFlow<UserResponse?> = MutableStateFlow(null)

    val onRestaurantLogin = MutableStateFlow(false)

    val selectedRestaurant = MutableStateFlow(Menu())

    val username = handle.getStateFlow(viewModelScope, "name", InputWrapper(value = "fazel.farnia@gmail.com"))

    val password = handle.getStateFlow(viewModelScope, "password", InputWrapper(value = "3"))

    val isRestaurantSelected = handle.getStateFlow(viewModelScope, "isProfileSelected", false)

    val areInputsValid = combine(username, password) { name, password ->
        name.value.isNotEmpty() && name.error == null &&
                password.value.isNotEmpty() && password.error == null
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private var focusedTextField = handle.get("focusedTextField") ?: FocusedTextFieldKey.USERNAME
        set(value) {
            field = value
            handle.set("focusedTextField", value)
        }

    private val _events = Channel<ScreenEvent>()
    private val _actions = Channel<LoginAction>(Channel.UNLIMITED)

    val events = _events.receiveAsFlow()
    private val actions =
        _actions.consumeAsFlow().shareIn(viewModelScope, SharingStarted.WhileSubscribed())

    init {
        if (focusedTextField != FocusedTextFieldKey.NONE) focusOnLastSelectedTextField()
        updateState(ViewModelState.Loading(false))

        actions
            .filterIsInstance<LoginAction.Login>()
            .map {
                _events.send(ScreenEvent.Loading(true))
                login()
            }
            .launchIn(viewModelScope)
        actions
            .filterIsInstance<LoginAction.ChooseRestaurant>()
            .map {
                _events.send(ScreenEvent.Loading(true))
                finishLogin()
            }
            .launchIn(viewModelScope)
    }

    fun onNameEntered(input: String) {
        val errorId = InputValidator.getNameErrorIdOrNull(input)
        username.value = username.value.copy(value = input, error = errorId)
    }


    fun onPasswordNumberEntered(input: String) {
        val errorId = InputValidator.getPasswordErrorIdOrNull(input)
        password.value = password.value.copy(value = input, error = errorId)
    }

    fun onTextFieldFocusChanged(key: FocusedTextFieldKey, isFocused: Boolean) {
        focusedTextField = if (isFocused) key else FocusedTextFieldKey.NONE
    }

    fun onNameImeActionClick() {
        viewModelScope.launch(Dispatchers.Default) {
            _events.send(ScreenEvent.MoveFocus())
        }
    }

    fun onContinueClick() {
        viewModelScope.launch(Dispatchers.Default) {
            if (areInputsValid.value) clearFocusAndHideKeyboard()
        }
    }

    fun onLogin() {
        viewModelScope.launch(Dispatchers.Default) {
            onRestaurantLogin.value = true
        }
    }

    fun onRestaurantSelected(menu: Menu) {
        selectedRestaurant.value = menu
        isRestaurantSelected.value = true
    }

    fun dispatch(action: LoginAction) {
        _actions.trySend(action)
    }

    private suspend fun clearFocusAndHideKeyboard() {
        _events.send(ScreenEvent.ClearFocus)
        _events.send(ScreenEvent.UpdateKeyboard(false))
        focusedTextField = FocusedTextFieldKey.NONE
    }

    private fun focusOnLastSelectedTextField() {
        viewModelScope.launch(Dispatchers.Default) {
            _events.send(ScreenEvent.RequestFocus(focusedTextField))
            delay(250)
            _events.send(ScreenEvent.UpdateKeyboard(true))
        }
    }

    private suspend fun login() {
        updateState(ViewModelState.Loading(true))
        loginRepo.login(
            LoginBody(
                username.value.value,
                password.value.value
            )
        ).collect { response ->
            if (response.status == Status.SUCCESS) {
                response.data?.let {
                    when (response.data) {
                        is UserResponse -> {
                            Timber.d("Restaurant list ${response.data}")
                            showChooseRestaurant.value = response.data
                        }
                        is Boolean -> successLogin.value = true
                    }
                }
            } else {
                updateState(ViewModelState.Error(response.message))
            }
//            when (response.status) {
//                Status.SUCCESS -> {
//                    response.data?.let {
//                        when (response.data) {
//                            is UserResponse -> {
//                                Timber.d("Restaurant list ${response.data}")
//                                showChooseRestaurant.value = response.data
//                            }
//                            is Boolean -> successLogin.value = true
//                        }
//                    }
//                }
//                else -> {
//                    handleError(response)
//                }
//            }
        }

    }

    private suspend fun finishLogin() {
        loginRepo.saveRestaurant(
            selectedRestaurant.value
        ).collect { resource ->
            if (resource.status == Status.SUCCESS){
                successLogin.value = true
            }
            else{

            }
//            when (resource.status) {
//                Status.SUCCESS -> {
//                    resource.data?.let {
//                        when (resource.data) {
//                            is Boolean -> successLogin.value = true
//                        }
//                    }
//                }
//                else -> {
//                    handleError(resource)
//                }
//            }

        }
    }

    private fun loginFailed(it: Throwable) {
        viewModelScope.launch(Dispatchers.Default) {
            _events.send(ScreenEvent.LoginFailed(it))
        }
    }

}