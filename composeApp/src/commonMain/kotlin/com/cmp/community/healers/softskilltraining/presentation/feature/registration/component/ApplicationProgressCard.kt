package com.cmp.community.healers.softskilltraining.presentation.feature.registration.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.presentation.components.card.Card14
import com.cmp.community.healers.softskilltraining.theme.Border
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.Primary
import com.cmp.community.healers.softskilltraining.theme.Secondary
import com.cmp.community.healers.softskilltraining.theme.TextFg

@Composable
fun ApplicationProgressCard(currentStep: Int, totalSteps: Int) {
    val steps = listOf("Registration" to "Complete profile", "Payment" to "Pay PKR 3000", "Training" to "Pick training date")
    Card14 {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
            Column {
                Text("Application Progress", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextFg))
                Text("Complete all steps to receive your certification", style = TextStyle(fontSize = 12.sp, color = MutedFg))
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("Current Step", style = TextStyle(fontSize = 11.sp, color = MutedFg))
                Text("$currentStep/$totalSteps", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Primary))
            }
        }
        Spacer(Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            steps.forEachIndexed { i, (title, sub) ->
                val done   = i + 1 < currentStep
                val active = i + 1 == currentStep
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier.size(40.dp).clip(CircleShape)
                            .background(when { done -> Primary; active -> Primary.copy(0.15f); else -> Secondary })
                            .border(2.dp, if (active || done) Primary else Border, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (done) Icon(Icons.Outlined.Check, null, tint = Color.White, modifier = Modifier.size(16.dp))
                        else Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(if (active) Primary else MutedFg.copy(0.35f)))
                    }
                    Text(title, fontSize = 10.sp, fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal, color = if (active) Primary else MutedFg, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(sub,   fontSize = 9.sp, color = MutedFg, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}
