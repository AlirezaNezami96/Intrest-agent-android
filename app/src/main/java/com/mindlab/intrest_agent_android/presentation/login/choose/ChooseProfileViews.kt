package com.mindlab.intrest_agent_android.presentation.login.choose

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.mindlab.intrest_agent_android.R
import com.mindlab.intrest_agent_android.data.model.Menu
import com.mindlab.intrest_agent_android.presentation.components.ChooseItemDropDown
import com.mindlab.intrest_agent_android.presentation.components.FullScreenLoading
import com.mindlab.intrest_agent_android.presentation.components.PrimaryButton
import com.mindlab.intrest_agent_android.presentation.components.Toolbar
import com.mindlab.intrest_agent_android.presentation.components.util.ScreenEvent
import com.mindlab.intrest_agent_android.presentation.login.LoginAction
import com.mindlab.intrest_agent_android.presentation.login.LoginViewModel
import com.mindlab.intrest_agent_android.utils.Display
import kotlinx.coroutines.flow.collect

/**
 * Created by Alireza Nezami on 12/11/2021.
 */

@Composable
fun ChooseProfileViews(
    viewModel: LoginViewModel,
    profiles: List<Menu>,
    onBack: () -> Unit
) {

    val isProfileSelected by viewModel.isRestaurantSelected.collectAsState()
    val contentWidth = Display.getScreenWidth() / 4

    val lifecycleOwner = LocalLifecycleOwner.current
    var loading by remember { mutableStateOf(false) }

    val events = remember(viewModel.events, lifecycleOwner) {
        viewModel.events.flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }
    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is ScreenEvent.Loading -> loading = event.show
            }
        }
    }

Box(modifier = Modifier.fillMaxSize()){

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Toolbar(
            modifier = Modifier
                .fillMaxWidth(),
            title = stringResource(R.string.choose_your_profile),
            showBack = true,
            onBack = { onBack() }
        )

        Spacer(Modifier.height(120.dp))

        ChooseItemDropDown(
            modifier = Modifier
                .width(contentWidth.dp)
                .align(Alignment.CenterHorizontally),
            label = stringResource(id = R.string.resturant_profile),
            placeHolder = stringResource(id = R.string.choose_your_profile),
            items = profiles,
            backgroundColor = MaterialTheme.colors.secondary
        ) {
            viewModel.onRestaurantSelected(it)
        }

        Spacer(Modifier.height(48.dp))

        PrimaryButton(
            modifier = Modifier
                .width(contentWidth.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.login),
            enabled = isProfileSelected,
            onClick = { viewModel.dispatch(LoginAction.ChooseRestaurant) }
        )


    }
    if (loading) {
        FullScreenLoading(
            modifier = Modifier.fillMaxSize()
        )
    }
}

}