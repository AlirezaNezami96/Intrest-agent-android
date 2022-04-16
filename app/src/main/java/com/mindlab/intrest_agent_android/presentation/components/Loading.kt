package com.mindlab.intrest_agent_android.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mindlab.intrest_agent_android.presentation.components.events.ViewModelState
import com.mindlab.intrest_agent_android.presentation.components.theme.TRANSPARENT_DARK

/**
 * Created by Alireza Nezami on 12/15/2021.
 */
@Composable
fun FullScreenLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = TRANSPARENT_DARK
            )
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
fun StateLoading(
    state: ViewModelState<*>,
    modifier: Modifier = Modifier
) {
    if (state.loading()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(
                    color = TRANSPARENT_DARK
                )
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun LoadingStateRowItem(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .then(
                Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.Transparent
                    )
            )
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 16.dp)
        )
    }
}

@Composable
fun SmallLoading(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}