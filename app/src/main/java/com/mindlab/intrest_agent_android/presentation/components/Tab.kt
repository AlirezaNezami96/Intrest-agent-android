package com.mindlab.intrest_agent_android.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Created by Alireza Nezami on 12/18/2021.
 */
@ExperimentalMaterialApi
@Composable
fun CustomTab(
    modifier: Modifier,
    text: String,
    enabled: Boolean,
    count: Int? = 0,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        elevation = 8.dp,
        onClick = onClick
    ) {
        Row {

            ButtonTitle(
                modifier = Modifier
                    .padding(
                        vertical = 16.dp,
                        horizontal = 32.dp
                    ),
                text = text,
                color =
                if (enabled) MaterialTheme.colors.onSecondary
                else MaterialTheme.colors.secondaryVariant,
                fontWeight =
                if (enabled) FontWeight.ExtraBold
                else FontWeight.Medium
            )


            CircleCountText(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(end = 8.dp),
                count ?: 0
            )


        }
    }
}