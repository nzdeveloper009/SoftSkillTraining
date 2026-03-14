package com.cmp.community.healers.softskilltraining.presentation.feature.registration.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.cmp.community.healers.softskilltraining.utils.constants.AMOUNT_FEE

@Composable
fun ApplicationProgressCard(currentStep: Int, totalSteps: Int) {
    val steps = listOf("Registration" to "Complete profile", "Payment" to "Pay PKR $AMOUNT_FEE", "Training" to "Pick training date")
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
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            // Each of the 3 equal columns is maxWidth/3 wide.
            // 40dp circle centred in column → centre = maxWidth/6 from each edge.
            val lineHPad = maxWidth / 6
            // Number of completed segments = currentStep - 1 (clamped to [0, steps.size-1])
            val doneSegments = (currentStep - 1).coerceIn(0, steps.size - 1)

            // Full track (grey)
            Box(
                modifier = Modifier
                    .fillMaxWidth().height(2.dp)
                    .align(Alignment.TopCenter)
                    .padding(horizontal = lineHPad)
                    .offset(y = 20.dp)
                    .background(Border.copy(alpha = 0.4f))
            )
            // Active/filled portion (Primary)
            if (doneSegments > 0) {
                // Each segment = 1/(steps.size-1) of the track
                // Track width = maxWidth - 2*lineHPad = maxWidth * 2/3
                // Filled end = lineHPad + (maxWidth * 2/3) * (doneSegments / (steps.size-1))
                val endPad = maxWidth - lineHPad -
                        (maxWidth * 2f / 3f) * (doneSegments.toFloat() / (steps.size - 1))
                Box(
                    modifier = Modifier
                        .fillMaxWidth().height(2.dp)
                        .align(Alignment.TopStart)
                        .padding(start = lineHPad, end = endPad)
                        .offset(y = 20.dp)
                        .background(Primary)
                )
            }
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
}
