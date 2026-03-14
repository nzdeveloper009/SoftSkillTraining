package com.cmp.community.healers.softskilltraining.presentation.feature.payment.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.domain.model.StepInfo
import com.cmp.community.healers.softskilltraining.presentation.components.progress.circle.StepCircle
import com.cmp.community.healers.softskilltraining.theme.Border
import com.cmp.community.healers.softskilltraining.theme.CardColor
import com.cmp.community.healers.softskilltraining.theme.LocalAppStrings
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.Primary
import com.cmp.community.healers.softskilltraining.theme.TextFg
import com.cmp.community.healers.softskilltraining.utils.constants.AMOUNT_FEE

// ─────────────────────────────────────────────────────────────────────────────
// APPLICATION PROGRESS CARD (step 2 active)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun PaymentProgressCard() {
    val s = LocalAppStrings.current
    Surface(
        modifier        = Modifier.fillMaxWidth(),
        shape           = RoundedCornerShape(14.dp),
        color           = CardColor,
        border          = BorderStroke(1.dp, Border.copy(alpha = 0.6f)),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.Top
            ) {
                Column {
                    Text(s.appProgress, style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextFg
                    )
                    )
                    Text(s.appProgressSub, style = TextStyle(fontSize = 12.sp, color = MutedFg))
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(s.currentStepLabel, style = TextStyle(fontSize = 11.sp, color = MutedFg), maxLines = 1, softWrap = false)
                    Text("2/3", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Primary))
                }
            }
            Spacer(Modifier.height(20.dp))

            // Steps row — BoxWithConstraints so we can pin line exactly to circle centres
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                // Each of the 3 equal columns is maxWidth/3 wide.
                // The 40dp circle is centred in its column → centre = column/2 = maxWidth/6.
                val lineHPad = maxWidth / 6

                // Full track: circle-1-centre → circle-3-centre
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .align(Alignment.TopCenter)
                        .padding(horizontal = lineHPad)
                        .offset(y = 20.dp)
                        .background(Border.copy(alpha = 0.4f))
                )
                // Active segment: circle-1-centre → circle-2-centre (Registration done)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .align(Alignment.TopStart)
                        .padding(start = lineHPad, end = maxWidth / 2)
                        .offset(y = 20.dp)
                        .background(Primary)
                )
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf(
                        StepInfo(s.paymentProgressDone,    s.completeProfile,              isDone = true,  isActive = false),
                        StepInfo(s.paymentProgressActive,  s.payFee,      isDone = false, isActive = true),
                        StepInfo(s.paymentProgressPending, s.pickDate,                     isDone = false, isActive = false),
                    ).forEach { step ->
                        StepCircle(step, Modifier.weight(1f))
                    }
                }
            }
        }
    }
}