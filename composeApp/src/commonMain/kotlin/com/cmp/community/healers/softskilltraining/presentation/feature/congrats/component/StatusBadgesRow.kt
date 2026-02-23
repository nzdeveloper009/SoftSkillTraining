package com.cmp.community.healers.softskilltraining.presentation.feature.congrats.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// ─────────────────────────────────────────────────────────────────────────────
// 3 STATUS BADGES  (Registration Complete | Payment Received | Training Scheduled)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun StatusBadgesRow() {
    val badges = listOf(
        "Registration" to "Complete",
        "Payment"      to "Received",
        "Training"     to "Scheduled"
    )
    Row(
        modifier              = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        badges.forEach { (title, sub) ->
            StatusBadge(title = title, sub = sub, modifier = Modifier.weight(1f))
        }
    }
}