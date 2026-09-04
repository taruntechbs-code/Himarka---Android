package com.example.himarka.core.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val NunitoHeadingFamily = FontFamily.SansSerif
val DMSansBodyFamily = FontFamily.Default

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = NunitoHeadingFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        color = HimarkaTextMain
    ),
    headlineMedium = TextStyle(
        fontFamily = NunitoHeadingFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        color = HimarkaTextMain
    ),
    titleLarge = TextStyle(
        fontFamily = NunitoHeadingFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        color = HimarkaTextMain
    ),
    titleMedium = TextStyle(
        fontFamily = NunitoHeadingFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        color = HimarkaTextMain
    ),
    bodyLarge = TextStyle(
        fontFamily = DMSansBodyFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        color = HimarkaTextMain
    ),
    bodyMedium = TextStyle(
        fontFamily = DMSansBodyFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.sp,
        color = HimarkaTextMuted
    ),
    labelLarge = TextStyle(
        fontFamily = NunitoHeadingFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        color = HimarkaTextMain
    ),
    labelSmall = TextStyle(
        fontFamily = NunitoHeadingFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        color = HimarkaTextMuted
    )
)
