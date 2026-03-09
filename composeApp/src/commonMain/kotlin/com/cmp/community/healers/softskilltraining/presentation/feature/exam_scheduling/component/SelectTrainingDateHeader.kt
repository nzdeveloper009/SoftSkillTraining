package com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EditCalendar
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.theme.Border
import com.cmp.community.healers.softskilltraining.theme.CardColor
import com.cmp.community.healers.softskilltraining.theme.Destructive
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.Primary
import com.cmp.community.healers.softskilltraining.theme.TextFg

@Composable
fun SelectTrainingDateHeader(daysLeft: Int = -1) {
    Surface(
        modifier        = Modifier.fillMaxWidth(),
        shape           = RoundedCornerShape(14.dp),
        color           = CardColor,
        border          = BorderStroke(1.dp, Border.copy(alpha = 0.6f)),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {

            // ── Title row ─────────────────────────────────────────────────────
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp))
                        .background(Primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.EditCalendar, null, tint = Primary, modifier = Modifier.size(20.dp))
                }
                Column {
                    Text(
                        "Select Training Date",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextFg)
                    )
                    Text(
                        "Choose your preferred training date",
                        style = TextStyle(fontSize = 12.sp, color = MutedFg)
                    )
                }
            }

            // ── 7-day deadline banner ─────────────────────────────────────────
            if (daysLeft >= 0) {
                val (deadlineColor, deadlineText) = when {
                    daysLeft == 0 -> Destructive to "Last day to schedule your exam!"
                    daysLeft <= 2 -> Destructive to "$daysLeft day${if (daysLeft == 1) "" else "s"} left to schedule your exam"
                    else          -> Primary      to "$daysLeft days remaining to schedule your exam"
                }
                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(deadlineColor.copy(alpha = 0.08f))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Outlined.Timer, null, tint = deadlineColor, modifier = Modifier.size(16.dp))
                    Text(
                        deadlineText,
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium, color = deadlineColor)
                    )
                }
            }
        }
    }
}