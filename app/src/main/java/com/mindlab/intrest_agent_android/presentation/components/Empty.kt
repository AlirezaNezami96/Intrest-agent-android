package com.mindlab.intrest_agent_android.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mindlab.intrest_agent_android.R

/**
 * Created by Alireza Nezami on 12/29/2021.
 */

@Composable
fun EmptyOrderList(
    text: String
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center)
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