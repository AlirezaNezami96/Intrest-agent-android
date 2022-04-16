package com.mindlab.intrest_agent_android.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mindlab.intrest_agent_android.presentation.components.theme.GRAY_800
import com.mindlab.intrest_agent_android.utils.extention.showExceeded

/**
 * Created by Alireza Nezami on 12/7/2021.
 */
@Composable
fun HeaderTitle(
    modifier: Modifier,
    text: String,
    color: Color = MaterialTheme.colors.onBackground,
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.subtitle1,
        fontWeight = FontWeight.Medium,
        color = color,
    )
}

@Composable
fun MultiStyleText(
    modifier: Modifier,
    text: AnnotatedString,
    style: TextStyle = MaterialTheme.typography.body1,
) {
    Text(
        modifier = modifier,
        text = text,
        style = style,
        fontWeight = FontWeight.Medium,
    )
}

@Composable
fun H6Title(
    modifier: Modifier,
    text: String,
    color: Color = MaterialTheme.colors.secondaryVariant,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.h6,
        fontWeight = FontWeight.Medium,
        color = color,
        textAlign = textAlign,
    )
}

@Composable
fun CaptionTitle(
    modifier: Modifier,
    text: String,
    color: Color = MaterialTheme.colors.secondaryVariant,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        style = MaterialTheme.typography.caption,
        fontWeight = fontWeight,
        color = color,
    )
}

@Composable
fun ItemTitle(
    modifier: Modifier,
    text: String,
    color: Color = MaterialTheme.colors.secondaryVariant,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        style = MaterialTheme.typography.caption,
        fontWeight = fontWeight,
        color = color,
    )
}

@Composable
fun ButtonTitle(
    modifier: Modifier,
    text: String,
    color: Color = MaterialTheme.colors.onSecondary,
    fontWeight: FontWeight = FontWeight.Medium,
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.button,
        fontWeight = FontWeight.Medium,
        color = color,
    )
}

@Composable
fun CircleCountText(
    modifier: Modifier,
    count: Int,
    color: Color = MaterialTheme.colors.onPrimary,
) {
    if (count > 0) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.then(
                Modifier
                    .background(
                        MaterialTheme.colors.primary,
                        shape = CircleShape
                    )
                    .layout() { measurable, constraints ->
                        // Measure the composable
                        val placeable = measurable.measure(constraints)

                        //get the current max dimension to assign width=height
                        val currentHeight = placeable.height
                        var heightCircle = currentHeight
                        if (placeable.width > heightCircle)
                            heightCircle = placeable.width

                        //assign the dimension and the center position
                        layout(heightCircle, heightCircle) {
                            // Where the composable gets placed
                            placeable.placeRelative(5, (heightCircle - currentHeight) / 2)
                        }
                    }
            )
        ) {

            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(5.dp),
                text = count.showExceeded(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.subtitle2,
                fontWeight = FontWeight.Normal,
                color = color,
            )
        }
    }
}

@Composable
fun OrderItemsRowTexts(
    modifier: Modifier,
    texts: List<String>,
    isHeader: Boolean = false,
) {
    Row(modifier = modifier) {
        texts.forEachIndexed { i, item ->
            ItemTitle(
                modifier = Modifier.weight(
                    if (i == 1) 3.0f
                    else 1.0f
                ),
                text = item,
                fontWeight =
                if (isHeader) FontWeight.SemiBold
                else FontWeight.Normal,
                color =
                if (isHeader) MaterialTheme.colors.onSecondary
                else GRAY_800,
//                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun EqualRowTexts(
    modifier: Modifier,
    texts: List<String>,
    isHeader: Boolean = false,
    isLastOneAction: Boolean = false
) {
    Box(modifier = modifier) {
        Row(modifier = Modifier.padding(horizontal = 24.dp)) {
            texts.forEachIndexed { index, item ->
                if (isLastOneAction && index == texts.size - 1) {
                    Box(
                        modifier = Modifier
                            .weight(1.0f)
                    ) {
                        ItemTitle(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(vertical = 8.dp),
                            text = item,
                            fontWeight =
                            if (isHeader) FontWeight.Medium
                            else FontWeight.Normal,
                            color =
                            if (isHeader) MaterialTheme.colors.onSecondary
                            else GRAY_800,
                        )
                    }
                } else {
                    ItemTitle(
                        modifier = Modifier
                            .weight(1.0f)
                            .padding(vertical = 8.dp),
                        text = item,
                        fontWeight =
                        if (isHeader) FontWeight.Medium
                        else FontWeight.Normal,
                        color =
                        if (isHeader) MaterialTheme.colors.onSecondary
                        else GRAY_800,
                    )
                }

            }
        }
    }
}