package com.cmp.community.healers.softskilltraining.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import softskilltraining.composeapp.generated.resources.Res

val Inter
    @Composable get() = FontFamily(
//        Font(
//            resource = Res.font.inter_regular,
//            weight = FontWeight.Normal
//        ),
//        Font(
//            resource = Res.font.inter_medium,
//            weight = FontWeight.Medium
//        ),
//        Font(
//            resource = Res.font.inter_bold,
//            weight = FontWeight.Bold
//        ),
//        Font(
//            resource = Res.font.inter_semibold,
//            weight = FontWeight.SemiBold
//        ),
    )

val Typography: Typography
    @Composable get() = Typography(
        bodyLarge = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.Normal,
            fontSize = 17.sp,
            lineHeight = 24.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.Medium,
            fontSize = 15.sp,
            lineHeight = 20.sp
        ),
        bodySmall = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.Normal,
            fontSize = 15.sp,
            lineHeight = 20.sp
        ),
        titleLarge = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 36.sp
        ),
        titleSmall = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.Medium,
            fontSize = 17.sp,
            lineHeight = 24.sp
        ),
    )
