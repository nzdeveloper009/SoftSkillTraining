package com.cmp.community.healers.softskilltraining.presentation.feature.congrats.component

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
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
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
import com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.mvi.SchedulingState
import com.cmp.community.healers.softskilltraining.theme.BorderColor
import com.cmp.community.healers.softskilltraining.theme.CardColor
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.Primary
import com.cmp.community.healers.softskilltraining.theme.TextFg

@Composable
fun TrainingScheduleCard(state: SchedulingState) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(12.dp),
        color    = CardColor,
        border   = BorderStroke(1.dp, BorderColor.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp))
                        .background(Primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.CalendarMonth, null, tint = Primary, modifier = Modifier.size(18.dp))
                }
                Column {
                    Text("Training Schedule",
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextFg
                        )
                    )
                    Text("Your scheduled training details",
                        style = TextStyle(fontSize = 11.sp, color = MutedFg))
                }
            }

            // 3 info tiles: Date | Time | Center
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ScheduleInfoTile(
                    icon  = Icons.Outlined.CalendarToday,
                    label = "Date",
                    value = state.trainingDate.ifBlank { "Feb 25, 2026" },
                    modifier = Modifier.weight(1f)
                )
                ScheduleInfoTile(
                    icon  = Icons.Outlined.Schedule,
                    label = "Time",
                    value = state.trainingTime,
                    modifier = Modifier.weight(1f)
                )
                ScheduleInfoTile(
                    icon  = Icons.Outlined.LocationOn,
                    label = "Center",
                    value = state.trainingCenter,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}