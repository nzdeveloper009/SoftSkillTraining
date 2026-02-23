package com.cmp.community.healers.softskilltraining.presentation.feature.payment.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.qrose.options.*
import io.github.alexzhirkevich.qrose.rememberQrCodePainter

/**
 * Renders a QR code for [content] at [size] dp.
 * Styled to match the site: white background, primary green dots, rounded frame.
 */
@Composable
fun QrCodeBox(
    content: String,
    size:    Dp    = 180.dp,
    primary: Color = Color(0xFF2D6A4F)
) {
    val painter = rememberQrCodePainter(content) {
        shapes {
            // Rounded dark modules
            darkPixel = QrPixelShape.roundCorners(50f)
            // Rounded eye frames
            ball      = QrBallShape.roundCorners(50f)
            frame     = QrFrameShape.roundCorners(50f)
        }
        colors {
            dark  = QrBrush.solid(primary)
            light = QrBrush.solid(Color.White)
        }
    }

    Box(
        modifier = Modifier
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter            = painter,
            contentDescription = "Payment QR Code",
            modifier           = Modifier.size(size)
        )
    }
}