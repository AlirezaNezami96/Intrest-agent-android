package com.mindlab.intrest_agent_android.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mindlab.intrest_agent_android.R
import com.mindlab.intrest_agent_android.presentation.components.events.ViewModelState
import com.mindlab.intrest_agent_android.utils.Display
import kotlinx.coroutines.delay
import timber.log.Timber

/**
 * Created by Alireza Nezami on 1/24/2022.
 */
@Composable
fun BoxScope.CustomNotification(
    modifier: Modifier,
    state: ViewModelState<*>,
) {
    val message = state.message()

    if (!message.isNullOrEmpty() || state.isSuccessful()) {
        val contentWidth = Display.getScreenWidth() / 5

        val animVisibleState = MutableTransitionState(false)
            .apply {
                targetState = state.containAnyError() or state.hasContent() or state.isSuccessful()
            }

        LaunchedEffect(animVisibleState) {
            Timber.d("notification ${animVisibleState.targetState} ${animVisibleState.currentState}")
            if (animVisibleState.targetState
            ) {
                delay(5000)
                Timber.d("notification exited")
                animVisibleState.targetState = false
            }
        }

        AnimatedVisibility(
            visibleState = animVisibleState,
            modifier = Modifier
                .padding(32.dp)
                .align(Alignment.BottomEnd),
            enter = slideInHorizontally(
                animationSpec = tween(300),
                initialOffsetX = {
                    it
                }
            ) + fadeIn(
                initialAlpha = 0.3f
            ),
            exit = slideOutHorizontally(
                animationSpec = tween(300),
                targetOffsetX = {
                    it
                }
            ) + fadeOut()
        ) {
            Row(
                Modifier
                    .width(contentWidth.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        color =
                        when (state) {
                            is ViewModelState.SuccessAction -> MaterialTheme.colors.primary
                            else -> MaterialTheme.colors.error
                        }
                    )

            ) {

                Image(
                    painter = painterResource(
                        when (state) {
                            is ViewModelState.SuccessAction -> R.drawable.ic_success
                            else -> R.drawable.ic_info
                        }),
                        contentDescription = "",
                        modifier = Modifier
                            .padding(16.dp)
                            .align(Alignment.CenterVertically)
                    )

                            Column (modifier = Modifier.align(Alignment.CenterVertically)) {

                        HeaderTitle(
                            text = when (state) {
                                is ViewModelState.SuccessAction -> stringResource(R.string.success)
                                else -> stringResource(R.string.error)
                            },
                            color = MaterialTheme.colors.onError,
                            modifier = Modifier.padding(end = 16.dp, top = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        CaptionTitle(
                            text = message ?: stringResource(R.string.action_succedd),
                            color = MaterialTheme.colors.onError,
                            modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)
                        )

                    }
            }
        }
    }
}