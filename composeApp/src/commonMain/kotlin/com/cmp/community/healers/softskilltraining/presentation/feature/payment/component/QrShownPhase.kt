package com.cmp.community.healers.softskilltraining.presentation.feature.payment.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.presentation.feature.payment.mvi.PaymentEvent
import com.cmp.community.healers.softskilltraining.presentation.feature.payment.mvi.PaymentState
import com.cmp.community.healers.softskilltraining.theme.LocalAppStrings
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.utils.decodeBase64Bitmap
import io.github.alexzhirkevich.qrose.options.QrBallShape
import io.github.alexzhirkevich.qrose.options.QrFrameShape
import io.github.alexzhirkevich.qrose.options.QrPixelShape
import io.github.alexzhirkevich.qrose.options.roundCorners
import io.github.alexzhirkevich.qrose.rememberQrCodePainter

// ─────────────────────────────────────────────────────────────────────────────
// PHASE 2: QR shown + Confirm Payment
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun QrShownPhase(state: PaymentState, onEvent: (PaymentEvent) -> Unit) {
    val s = LocalAppStrings.current

    val qrBitmap = remember(state.qrCodeBase64) {
        decodeBase64Bitmap(state.qrCodeBase64)
    }

    Column(
        modifier            = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // QR Code image from API base64, or demo QR when API unavailable
        Box(
            modifier = Modifier
                .background(Color.White, RoundedCornerShape(16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (qrBitmap != null) {
                Image(
                    painter            = BitmapPainter(qrBitmap),
                    contentDescription = "Payment QR Code",
                    modifier           = Modifier.size(180.dp)
                )
            } else {
                val demoQrPainter = rememberQrCodePainter(
                    data = "https://payment.softskilltraining.pk/pay?amount=5000&ref=${state.paymentId}"
                ) {
                    shapes {
                        ball      = QrBallShape.roundCorners(0.25f)
                        frame     = QrFrameShape.roundCorners(0.25f)
                        darkPixel = QrPixelShape.roundCorners()
                    }
                }
                Image(
                    painter            = demoQrPainter,
                    contentDescription = "Payment QR Code",
                    modifier           = Modifier.size(180.dp)
                )
            }
        }

        Text(
            s.scanQrToPay,
            style = TextStyle(fontSize = 13.sp, color = MutedFg)
        )

        // Confirm Payment button
        Button(
            onClick  = { onEvent(PaymentEvent.ConfirmPayment) },
            enabled  = !state.isConfirming,
            shape    = RoundedCornerShape(10.dp),
            colors   = ButtonDefaults.buttonColors(
                containerColor         = Color(0xFF111111),
                contentColor           = Color.White,
                disabledContainerColor = Color(0xFF555555)
            ),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            if (state.isConfirming) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                Spacer(Modifier.width(8.dp))
                Text(s.verifying, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            } else {
                Text(s.confirmPayment, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Outlined.ArrowForward, null, modifier = Modifier.size(16.dp))
            }
        }
    }
}