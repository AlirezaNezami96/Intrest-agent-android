package com.mindlab.intrest_agent_android.presentation.login.sign_in

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.mindlab.intrest_agent_android.R
import com.mindlab.intrest_agent_android.presentation.components.*
import com.mindlab.intrest_agent_android.presentation.components.events.ViewModelState
import com.mindlab.intrest_agent_android.presentation.components.util.FocusedTextFieldKey
import com.mindlab.intrest_agent_android.presentation.components.util.ScreenEvent
import com.mindlab.intrest_agent_android.presentation.login.LoginAction
import com.mindlab.intrest_agent_android.presentation.login.LoginViewModel
import com.mindlab.intrest_agent_android.utils.Constants
import com.mindlab.intrest_agent_android.utils.Display.getScreenWidth
import com.mindlab.intrest_agent_android.utils.extention.toast
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import timber.log.Timber

/**
 * Created by Alireza Nezami on 12/7/2021.
 */
@InternalCoroutinesApi
@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalAnimationApi
@Composable
fun SignInViews(
    viewModel: LoginViewModel,
) {
    Timber.d("View Model Name 2: $viewModel")

    val state = viewModel.state().collectAsState(initial = ViewModelState.Loading(false))

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val events = remember(viewModel.events, lifecycleOwner) {
        viewModel.events.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }

    val username by viewModel.username.collectAsState()
    val password by viewModel.password.collectAsState()
    val areInputsValid by viewModel.areInputsValid.collectAsState()

    val usernameFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }
    var loding by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is ScreenEvent.ShowToast -> context.toast(event.messageId)
                is ScreenEvent.UpdateKeyboard -> {
                    if (event.show) keyboardController?.show() else keyboardController?.hide()
                }
                is ScreenEvent.ClearFocus -> focusManager.clearFocus()
                is ScreenEvent.RequestFocus -> {
                    when (event.textFieldKey) {
                        FocusedTextFieldKey.USERNAME -> usernameFocusRequester.requestFocus()
                        FocusedTextFieldKey.PASSWORD -> passwordFocusRequester.requestFocus()
                    }
                }
                is ScreenEvent.MoveFocus -> focusManager.moveFocus(event.direction)
                is ScreenEvent.Loading -> loding = event.show
            }
        }
    }

    val contentWidth = getScreenWidth() / 4


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {

            Toolbar(
                modifier = Modifier
                    .fillMaxWidth(),
                title = stringResource(R.string.sign_in),
                showBack = false
            ) {

            }

            Spacer(Modifier.height(16.dp))

            Image(
                painter = painterResource(R.drawable.ic_logo),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(32.dp))

            CustomInput(
                modifier = Modifier
                    .width(contentWidth.dp)
                    .align(Alignment.CenterHorizontally)
                    .focusRequester(usernameFocusRequester)
                    .onFocusChanged { focusState ->
                        viewModel.onTextFieldFocusChanged(
                            key = FocusedTextFieldKey.USERNAME,
                            isFocused = focusState.isFocused
                        )
                    },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = VisualTransformation.None,
                label = stringResource(id = R.string.email_label),
                placeHolderText = stringResource(id = R.string.email_placeholder),
                maxLength = Constants.MAX_USERNAME_CHAR_LENGTH,
                inputWrapper = username,
                onValueChange = viewModel::onNameEntered,
                onImeKeyAction = viewModel::onNameImeActionClick
            )

            Spacer(Modifier.height(16.dp))

            CustomInput(
                modifier = Modifier
                    .width(contentWidth.dp)
                    .align(Alignment.CenterHorizontally)
                    .focusRequester(usernameFocusRequester)
                    .onFocusChanged { focusState ->
                        viewModel.onTextFieldFocusChanged(
                            key = FocusedTextFieldKey.PASSWORD,
                            isFocused = focusState.isFocused
                        )
                    },

                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = PasswordVisualTransformation(),
                label = stringResource(id = R.string.password_label),
                placeHolderText = stringResource(id = R.string.password_placeholder),
                inputWrapper = password,
                maxLength = Constants.MAX_USERNAME_CHAR_LENGTH,
                onValueChange = viewModel::onPasswordNumberEntered,
                onImeKeyAction = viewModel::onContinueClick
            )

            Spacer(Modifier.height(32.dp))

            PrimaryButton(
                modifier = Modifier
                    .width(contentWidth.dp)
                    .align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.login),
                enabled = areInputsValid,
                onClick = { viewModel.dispatch(LoginAction.Login) }
            )
        }


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

//@InternalCoroutinesApi
//@ExperimentalAnimationApi
//@Preview(device = Devices.AUTOMOTIVE_1024p, widthDp = 1600, heightDp = 900, showBackground = true)
//@Composable
//fun DefaultPreview() {
//    IntRestagentandroidTheme(false) {
//        SignInViews()
//    }
//}