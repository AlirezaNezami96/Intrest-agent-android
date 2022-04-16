package com.mindlab.intrest_agent_android.presentation.components.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mindlab.intrest_agent_android.presentation.components.util.rubikSansFamily

// Set of Material typography styles to start with
val Typography = Typography(
    h5 = TextStyle(
        fontFamily = rubikSansFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
    ),
    h3 = TextStyle(
        fontFamily = rubikSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 48.sp,
        letterSpacing = 0.sp
    ),
    h4 = TextStyle(
        fontFamily = rubikSansFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 34.sp,
        letterSpacing = 0.25.sp
    ),

    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)