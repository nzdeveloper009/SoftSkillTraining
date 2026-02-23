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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.presentation.feature.payment.mvi.PaymentEvent
import com.cmp.community.healers.softskilltraining.presentation.feature.payment.mvi.PaymentState
import com.cmp.community.healers.softskilltraining.theme.Primary
import com.cmp.community.healers.softskilltraining.utils.constants.payment.PaymentMethod

// ─────────────────────────────────────────────────────────────────────────────
// PHASE 1: Select method + Generate QR
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun SelectMethodPhase(state: PaymentState, onEvent: (PaymentEvent) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

        // 2-column method selector
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PaymentMethodTile(
                icon       = Icons.Outlined.CreditCard,
                label      = "Credit/Debit",
                selected   = state.selectedMethod == PaymentMethod.CREDIT_DEBIT,
                modifier   = Modifier.weight(1f),
                onClick    = { onEvent(PaymentEvent.SelectMethod(PaymentMethod.CREDIT_DEBIT)) }
            )
            PaymentMethodTile(
                icon       = Icons.Outlined.Wallet,
                label      = "Mobile Wallet",
                selected   = state.selectedMethod == PaymentMethod.MOBILE_WALLET,
                modifier   = Modifier.weight(1f),
                onClick    = { onEvent(PaymentEvent.SelectMethod(PaymentMethod.MOBILE_WALLET)) }
            )
        }

        // Generate QR button
        Button(
            onClick  = { onEvent(PaymentEvent.GenerateQr) },
            enabled  = !state.isGeneratingQr,
            shape    = RoundedCornerShape(10.dp),
            colors   = ButtonDefaults.buttonColors(
                containerColor         = Primary,
                contentColor           = Color.White,
                disabledContainerColor = Primary.copy(alpha = 0.55f)
            ),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            if (state.isGeneratingQr) {
                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                Spacer(Modifier.width(8.dp))
                Text("Generating...", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            } else {
                Text("Generate QR", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                Spacer(Modifier.width(8.dp))
                Icon(Icons.Outlined.ArrowForward, null, modifier = Modifier.size(16.dp))
            }
        }
    }
}