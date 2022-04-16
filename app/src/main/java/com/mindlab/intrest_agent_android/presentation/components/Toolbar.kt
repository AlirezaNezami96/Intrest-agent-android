package com.mindlab.intrest_agent_android.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mindlab.intrest_agent_android.R

/**
 * Created by Alireza Nezami on 12/7/2021.
 */
@Composable
fun Toolbar(
    modifier: Modifier,
    title: String,
    showBack: Boolean,
    onBack: () -> Unit
) {
    Card(
        modifier = modifier
            .then(
                Modifier
            ),
        elevation = 4.dp
    ) {

        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {

            if (showBack) {
                Image(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = "",
                    Modifier.clickable {
                        onBack()
                    }
                )
            }

            Spacer(Modifier.width(16.dp))

            HeaderTitle(
                modifier = Modifier,
                text = title,
                color = MaterialTheme.colors.onBackground
            )
        }

    }
}