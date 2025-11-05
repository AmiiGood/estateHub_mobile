package com.oscar.estatehubcompose.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.oscar.estatehubcompose.R

// Set of Material typography styles to start with


val Parkinsans =  FontFamily(
    Font(R.font.parkinsans_regular, FontWeight.Normal),
    Font(R.font.parkinsans_medium, FontWeight.Medium),
    Font(R.font.parkinsans_semibold, FontWeight.SemiBold),
    Font(R.font.parkinsans_bold, FontWeight.Bold),
    Font(R.font.parkinsans_extrabold, FontWeight.ExtraBold),
    Font(R.font.parkinsans_light, FontWeight.Light),
)

val Typography = Typography(

    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)