package com.cmp.community.healers.softskilltraining.presentation.feature.congrats.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.mvi.SchedulingState
import com.cmp.community.healers.softskilltraining.theme.BorderColor
import com.cmp.community.healers.softskilltraining.theme.CardColor
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.SuccessBg
import com.cmp.community.healers.softskilltraining.theme.SuccessBorder
import com.cmp.community.healers.softskilltraining.theme.SuccessText
import com.cmp.community.healers.softskilltraining.theme.TextFg

@Composable
fun CelebrationCard(state: SchedulingState, onGoToProfile: () -> Unit) {
    Surface(
        modifier        = Modifier.fillMaxWidth(),
        shape           = RoundedCornerShape(20.dp),
        color           = CardColor,
        border          = BorderStroke(1.dp, SuccessBorder),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier            = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ── Party popper icon ─────────────────────────────────────────────
            PartyPopperIcon()

            // ── "Registration Complete" pill ──────────────────────────────────
            Surface(
                shape  = RoundedCornerShape(20.dp),
                color  = SuccessBg,
                border = BorderStroke(1.dp, SuccessBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Outlined.CheckCircle, null, tint = SuccessText, modifier = Modifier.size(14.dp))
                    Text("Registration Complete",
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SuccessText
                        )
                    )
                }
            }

            // ── Title ─────────────────────────────────────────────────────────
            Text(
                "Congratulations! 🎉",
                style = TextStyle(
                    fontSize   = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color      = TextFg,
                    textAlign  = TextAlign.Center
                )
            )
            Text(
                "Your registration has been completed and your training has been scheduled successfully",
                style = TextStyle(
                    fontSize  = 13.sp,
                    color     = MutedFg,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
            )

            HorizontalDivider(color = BorderColor.copy(alpha = 0.5f))

            // ── Training Schedule card ────────────────────────────────────────
            TrainingScheduleCard(state)

            // ── Training Center card ──────────────────────────────────────────
            TrainingCenterCard(state)

            // ── What's Next card ──────────────────────────────────────────────
            WhatsNextCard()

            // ── 3 status badges ───────────────────────────────────────────────
            StatusBadgesRow()

            // ── Go to Profile button ──────────────────────────────────────────
            OutlinedButton(
                onClick  = onGoToProfile,
                shape    = RoundedCornerShape(12.dp),
                border   = BorderStroke(1.5.dp, BorderColor),
                colors   = ButtonDefaults.outlinedButtonColors(contentColor = TextFg),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Icon(Icons.Outlined.Person, null, tint = MutedFg, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Go to Profile", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextFg)
            }
        }
    }
}