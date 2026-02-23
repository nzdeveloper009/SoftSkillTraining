package com.cmp.community.healers.softskilltraining.presentation.feature.congrats.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.theme.SuccessBg

// ─────────────────────────────────────────────────────────────────────────────
// PARTY POPPER ICON  (animated scale-in)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun PartyPopperIcon() {
    val scale = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue  = 1f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMedium)
        )
    }
    Box(
        modifier = Modifier
            .size(80.dp)
            .scale(scale.value)
            .clip(CircleShape)
            .background(SuccessBg),
        contentAlignment = Alignment.Center
    ) {
        Text("🎉", fontSize = 36.sp)
    }
}