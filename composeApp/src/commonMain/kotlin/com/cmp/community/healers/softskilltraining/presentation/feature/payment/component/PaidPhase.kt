package com.cmp.community.healers.softskilltraining.presentation.feature.payment.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.presentation.feature.payment.mvi.PaymentEvent
import com.cmp.community.healers.softskilltraining.presentation.feature.payment.mvi.PaymentState
import com.cmp.community.healers.softskilltraining.theme.Border
import com.cmp.community.healers.softskilltraining.theme.Primary

// ─────────────────────────────────────────────────────────────────────────────
// PHASE 3: Paid — receipt details + Download Receipt
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun PaidPhase(state: PaymentState, onEvent: (PaymentEvent) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        // Transaction details
        HorizontalDivider(color = Border.copy(alpha = 0.4f))

        ReceiptRow("Transaction ID", state.transactionId, isGreen = false)
        ReceiptRow("Payment Date",   state.paymentDate,   isGreen = false)

        HorizontalDivider(color = Border.copy(alpha = 0.4f))

        ReceiptRow("Status", "Success", isGreen = true)

        HorizontalDivider(color = Border.copy(alpha = 0.4f))

        // Download Receipt button — outlined style from screenshot
        OutlinedButton(
            onClick        = { onEvent(PaymentEvent.DownloadReceipt) },
            enabled        = !state.isDownloading,
            shape          = RoundedCornerShape(10.dp),
            border         = BorderStroke(1.5.dp, Primary),
            modifier       = Modifier.fillMaxWidth().height(50.dp),
            colors         = ButtonDefaults.outlinedButtonColors(contentColor = Primary)
        ) {
            if (state.isDownloading) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Primary, strokeWidth = 2.dp)
                Spacer(Modifier.width(8.dp))
                Text("Downloading...", fontSize = 14.sp, color = Primary)
            } else {
                Icon(Icons.Outlined.Download, null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Download Receipt", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
        }
    }
}