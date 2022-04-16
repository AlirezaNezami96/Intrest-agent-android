package com.mindlab.intrest_agent_android.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mindlab.intrest_agent_android.presentation.components.theme.TRANSPARENT_DARK

/**
 * Created by Alireza Nezami on 12/15/2021.
 */
@Composable
fun CustomLoading(
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .then(
                Modifier
                    .background(
                        color = TRANSPARENT_DARK
                    )
            )
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}