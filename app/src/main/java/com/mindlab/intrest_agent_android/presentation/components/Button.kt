package com.mindlab.intrest_agent_android.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mindlab.intrest_agent_android.R
import com.mindlab.intrest_agent_android.presentation.components.theme.GRAY_150
import com.mindlab.intrest_agent_android.presentation.components.theme.GRAY_200
import com.mindlab.intrest_agent_android.presentation.components.theme.LINK_BLUE

/**
 * Created by Alireza Nezami on 12/11/2021.
 */
@Composable
fun PrimaryButton(
    modifier: Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .defaultMinSize(minWidth = 200.dp),
        enabled = enabled,
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 5.dp
        ),
        colors = ButtonDefaults.buttonColors(
            backgroundColor =
            if (enabled) MaterialTheme.colors.primary
            else MaterialTheme.colors.secondary
        ),
        shape = RoundedCornerShape(50)
    ) {
        ButtonTitle(
            modifier = Modifier
                .padding(vertical = 5.dp, horizontal = 8.dp),
            text = text,
            color =
            if (enabled) MaterialTheme.colors.onPrimary
            else MaterialTheme.colors.secondaryVariant,
        )
    }
}

@Composable
fun BorderButton(
    modifier: Modifier,
    text: String,
    @DrawableRes icon: Int? = null,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .defaultMinSize(minWidth = 200.dp),
        enabled = enabled,
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 5.dp
        ),
        colors = ButtonDefaults.buttonColors(
            backgroundColor =
            if (enabled) MaterialTheme.colors.background
            else MaterialTheme.colors.secondary
        ),
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, MaterialTheme.colors.secondaryVariant),
    ) {
        icon?.let {
            Image(
                painter = painterResource(icon),
                contentDescription = ""
            )
        }

        ButtonTitle(
            modifier = Modifier
                .padding(vertical = 5.dp, horizontal = 8.dp),
            text = text,
            color = MaterialTheme.colors.secondaryVariant
        )
    }
}

@Composable
fun SecondaryButton(
    modifier: Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            backgroundColor =
            if (enabled) MaterialTheme.colors.background
            else MaterialTheme.colors.secondary
        ),
        shape = RoundedCornerShape(50)
    ) {
        ButtonTitle(
            modifier = Modifier
                .padding(vertical = 5.dp),
            text = text,
            color =
            if (enabled) MaterialTheme.colors.error
            else MaterialTheme.colors.error
        )
    }
}

@Composable
fun ContactButton(
    modifier: Modifier,
    text: String,
    onClick: () -> Unit,
) {

    var contactVisibility by remember { mutableStateOf(false) }

    Row(
//        onClick = {
//            onClick()
//            contactVisibility = !contactVisibility
//        },
        modifier = modifier
            .height(IntrinsicSize.Min)
            .background(
                color = MaterialTheme.colors.secondary,
                shape = RoundedCornerShape(50)
            )
            .clickable {
                onClick()
                contactVisibility = !contactVisibility
            },
//        colors = ButtonDefaults.buttonColors(
//            backgroundColor = MaterialTheme.colors.secondary
//        ),
//        shape = RoundedCornerShape(50)
    ) {
//        Row(modifier = Modifier) {
        Icon(
            painter =
            if (contactVisibility)
                painterResource(R.drawable.ic_baseline_visibility_off)
            else
                painterResource(R.drawable.ic_baseline_visibility),
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(16.dp),
            tint = LINK_BLUE
        )

        Divider(
            color = GRAY_200,
            modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
                .padding(
                    top = 10.dp,
                    bottom = 10.dp,
                )
        )

        Spacer(modifier = Modifier.width(16.dp))

        CaptionTitle(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(vertical = 16.dp),
            text = if (contactVisibility)
                text
            else
                stringResource(R.string.customer_contact),
            color = MaterialTheme.colors.onSecondary
        )


//        }

    }
}

