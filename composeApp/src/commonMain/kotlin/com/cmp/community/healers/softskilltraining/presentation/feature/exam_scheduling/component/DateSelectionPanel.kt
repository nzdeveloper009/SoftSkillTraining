package com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.component

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.mvi.SchedulingEvent
import com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.mvi.SchedulingState
import com.cmp.community.healers.softskilltraining.theme.CardColor
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.Primary
import com.cmp.community.healers.softskilltraining.theme.SuccessBg
import com.cmp.community.healers.softskilltraining.theme.SuccessBorder
import com.cmp.community.healers.softskilltraining.theme.SuccessText
import com.cmp.community.healers.softskilltraining.theme.TextFg

// ─────────────────────────────────────────────────────────────────────────────
// DATE SELECTION SIDE PANEL  (shown after picking a date)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun DateSelectionPanel(
    state:    SchedulingState,
    onEvent:  (SchedulingEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape    = RoundedCornerShape(12.dp),
        color    = SuccessBg.copy(alpha = 0.5f),
        border   = BorderStroke(1.dp, SuccessBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            // ── Header row ────────────────────────────────────────────────────
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    Icons.Outlined.CheckCircle, null,
                    tint     = SuccessText,
                    modifier = Modifier.size(18.dp)
                )
                Column {
                    Text(
                        "Date Selected",
                        style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SuccessText)
                    )
                    Text(
                        "Confirm to schedule your training",
                        style = TextStyle(fontSize = 11.sp, color = SuccessText.copy(alpha = 0.75f), lineHeight = 14.sp)
                    )
                }
            }

            // ── Date box ──────────────────────────────────────────────────────
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape    = RoundedCornerShape(8.dp),
                color    = CardColor,
                border   = BorderStroke(1.dp, SuccessBorder)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                    Text(
                        "Training Date",
                        style = TextStyle(fontSize = 10.sp, color = MutedFg)
                    )
                    Spacer(Modifier.height(3.dp))
                    Text(
                        state.selectedDateLabel,
                        style = TextStyle(
                            fontSize   = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color      = TextFg,
                            lineHeight = 18.sp
                        )
                    )
                }
            }

            // ── Schedule Training button — always full width, never clips ─────
            Button(
                onClick  = { onEvent(SchedulingEvent.ScheduleTraining) },
                enabled  = !state.isScheduling,
                shape    = RoundedCornerShape(10.dp),
                colors   = ButtonDefaults.buttonColors(
                    containerColor         = Primary,
                    contentColor           = Color.White,
                    disabledContainerColor = Primary.copy(alpha = 0.55f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                if (state.isScheduling) {
                    CircularProgressIndicator(
                        modifier    = Modifier.size(16.dp),
                        color       = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Scheduling...",
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                } else {
                    Text(
                        "Schedule Training",
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
