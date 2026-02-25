package com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.Primary
import com.cmp.community.healers.softskilltraining.theme.TextFg

@Composable
fun SchedulingProgressCard() {
    val steps = listOf(
        StepInfo("Registration",      "Complete profile",   isDone = true,  isActive = false),
        StepInfo("Payment",           "Pay PKR 3000",       isDone = true,  isActive = false),
        StepInfo("Schedule Training", "Pick training date", isDone = false, isActive = true),
    )
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
                    Text("Application Progress",
                        style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextFg))
                    Text("Complete all steps to receive your certification",
                        style = TextStyle(fontSize = 12.sp, color = MutedFg))
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Current Step", style = TextStyle(fontSize = 11.sp, color = MutedFg))
                    Text("3/3", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Primary))
                }
            }
            Spacer(Modifier.height(20.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                // Full track
                Box(
                    modifier = Modifier
                        .fillMaxWidth().height(2.dp)
                        .align(Alignment.TopCenter)
                        .padding(horizontal = 28.dp)
                        .offset(y = 20.dp)
                        .background(Border.copy(alpha = 0.4f))
                )
                // Both steps done → full track filled
                Box(
                    modifier = Modifier
                        .fillMaxWidth().height(2.dp)
                        .align(Alignment.TopStart)
                        .padding(horizontal = 28.dp)
                        .offset(y = 20.dp)
                        .background(Primary)
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    steps.forEach { StepCircle(it, Modifier.weight(1f)) }
                }
            }
        }
    }
}