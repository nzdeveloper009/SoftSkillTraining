package com.cmp.community.healers.softskilltraining.presentation.feature.profile.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
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
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeState
import com.cmp.community.healers.softskilltraining.theme.BlueBg
import com.cmp.community.healers.softskilltraining.theme.BlueBorder
import com.cmp.community.healers.softskilltraining.theme.BlueText
import com.cmp.community.healers.softskilltraining.theme.Border
import com.cmp.community.healers.softskilltraining.theme.CardColor
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.Primary
import com.cmp.community.healers.softskilltraining.theme.Secondary
import com.cmp.community.healers.softskilltraining.theme.TextFg

@Composable
fun TrainingScheduledCard(state: CandidateHomeState) {
    ProfileCard {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier.size(36.dp).clip(RoundedCornerShape(9.dp))
                        .background(Primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.CalendarMonth,
                        null,
                        tint = Primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Column {
                    Text(
                        "Training Scheduled",
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextFg
                        )
                    )
                    Text(
                        "Your training has been scheduled",
                        style = TextStyle(fontSize = 11.sp, color = MutedFg)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InfoBox("Training Date", state.scheduledTrainingDate, Modifier.weight(1f))
                InfoBox("Training Time", state.scheduledTrainingTime, Modifier.weight(1f))
            }
            InfoBox("Training Center", state.scheduledTrainingCenter, Modifier.fillMaxWidth())
            InfoBox("Center Address", state.scheduledTrainingAddress, Modifier.fillMaxWidth())
            InfoBox("City", state.scheduledTrainingCity, Modifier.fillMaxWidth())
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                color = BlueBg,
                border = BorderStroke(1.dp, BlueBorder)
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        "Note:",
                        style = TextStyle(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = BlueText
                        )
                    )
                    Text(
                        "Please arrive at the training center 15 minutes before your scheduled time. Bring a valid ID and your training confirmation.",
                        style = TextStyle(fontSize = 11.sp, color = MutedFg, lineHeight = 16.sp)
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoBox(label: String, value: String, modifier: Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = Secondary,
        border = BorderStroke(1.dp, Border.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            Text(label, style = TextStyle(fontSize = 10.sp, color = MutedFg))
            Spacer(Modifier.height(3.dp))
            Text(
                value.ifBlank { "N/A" },
                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TextFg)
            )
        }
    }
}
