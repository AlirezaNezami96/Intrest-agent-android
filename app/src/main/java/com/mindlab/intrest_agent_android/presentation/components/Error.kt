package com.mindlab.intrest_agent_android.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mindlab.intrest_agent_android.R

/**
 * Created by Alireza Nezami on 12/29/2021.
 */

@Composable
fun EmptyOrderList(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {

        Column(
            modifier = Modifier
                .align(Alignment.Center)
        ) {

            Image(
                painter = painterResource(R.drawable.ic_empty),
                contentDescription = "",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            H6Title(
                modifier = Modifier,
                text = text,
            )
        }

    }
}

@Composable
fun ErrorStateFullPage(
    onRetryClicked: () -> Unit
) {
    //TODO: For example network connection dialog with retry button
}

@Composable
fun ErrorStateRowItem(
    onRetryClicked: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable { onRetryClicked() }) {

        Icon(
            imageVector = Icons.Filled.Refresh,
            contentDescription = "",
            Modifier
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        CaptionTitle(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            text = stringResource(R.string.error_accrued_try_again)
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}