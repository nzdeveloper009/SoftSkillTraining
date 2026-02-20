package com.cmp.community.healers.softskilltraining.presentation.feature.auth.component

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp


// ── Shared UI components (used by SignIn, SignUp) ──────────────────────────────
@Composable
internal fun AuthLabel(text: String) {
    Text(
        text = text,
        style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
    )
}