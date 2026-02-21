package com.cmp.community.healers.softskilltraining.presentation.components.field

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.theme.TextFg

/** Field label text */
@Composable
fun FieldLabel(text: String) {
    Text(text, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Medium, color = TextFg))
}