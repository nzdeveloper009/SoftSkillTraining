package com.cmp.community.healers.softskilltraining.presentation.feature.payment.component

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.domain.model.StepInfo
import com.cmp.community.healers.softskilltraining.theme.BorderColor
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.Primary
import com.cmp.community.healers.softskilltraining.theme.Secondary
import com.cmp.community.healers.softskilltraining.theme.TextFg

@Composable
fun StepCircle(step: StepInfo, modifier: Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.15f, targetValue = 0.4f,
        animationSpec = infiniteRepeatable(tween(900), RepeatMode.Reverse),
        label = "alpha"
    )
    Column(
        modifier            = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    when {
                        step.isDone   -> Primary
                        step.isActive -> Primary.copy(alpha = if (step.isActive) pulseAlpha else 0.15f)
                        else          -> Secondary
                    }
                )
                .border(2.dp, if (step.isDone || step.isActive) Primary else BorderColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (step.isDone) {
                Icon(Icons.Outlined.Check, null, tint = Color.White, modifier = Modifier.size(16.dp))
            } else {
                Box(
                    modifier = Modifier.size(10.dp).clip(CircleShape)
                        .background(if (step.isActive) Primary else MutedFg.copy(0.35f))
                )
            }
        }
        Text(step.label, fontSize = 10.sp,
            fontWeight = if (step.isActive) FontWeight.SemiBold else FontWeight.Normal,
            color      = if (step.isActive) Primary else if (step.isDone) TextFg else MutedFg,
            maxLines   = 1, overflow = TextOverflow.Ellipsis)
        Text(step.sub, fontSize = 9.sp, color = MutedFg, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}