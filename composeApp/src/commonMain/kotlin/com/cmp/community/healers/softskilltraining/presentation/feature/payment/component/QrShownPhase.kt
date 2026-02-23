package com.cmp.community.healers.softskilltraining.presentation.feature.payment.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Wallet
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.presentation.feature.payment.mvi.PaymentEvent
import com.cmp.community.healers.softskilltraining.presentation.feature.payment.mvi.PaymentState
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.Primary
import com.cmp.community.healers.softskilltraining.utils.constants.payment.PaymentMethod

// ─────────────────────────────────────────────────────────────────────────────
// PHASE 2: QR shown + Confirm Payment
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun QrShownPhase(state: PaymentState, onEvent: (PaymentEvent) -> Unit) {
    Column(
        modifier            = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Same method tiles — read-only visual reference
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PaymentMethodTile(
                Icons.Outlined.CreditCard, "Credit/Debit",
                selected = state.selectedMethod == PaymentMethod.CREDIT_DEBIT,
                modifier = Modifier.weight(1f), onClick = {})
            PaymentMethodTile(Icons.Outlined.Wallet, "Mobile Wallet",
                selected = state.selectedMethod == PaymentMethod.MOBILE_WALLET,
                modifier = Modifier.weight(1f), onClick = {})
        }

        // QR Code (qrose library — pure Kotlin)
        QrCodeBox(content = state.qrContent, size = 180.dp, primary = Primary)

        Text("Scan the QR code to pay PKR 3,000",
            style = TextStyle(fontSize = 13.sp, color = MutedFg)
        )

        // Confirm Payment — black button matching website
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
                Text("Verifying...", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            } else {
                Text("Confirm Payment", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Outlined.ArrowForward, null, modifier = Modifier.size(16.dp))
            }
        }
    }
}