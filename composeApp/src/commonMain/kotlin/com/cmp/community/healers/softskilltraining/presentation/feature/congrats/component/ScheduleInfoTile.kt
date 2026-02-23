package com.cmp.community.healers.softskilltraining.presentation.feature.congrats.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.theme.BorderColor
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.Secondary
import com.cmp.community.healers.softskilltraining.theme.TextFg

@Composable
fun ScheduleInfoTile(
    icon: ImageVector,
    label:    String,
    value:    String,
    modifier: Modifier
) {
    Surface(
        modifier = modifier,
        shape    = RoundedCornerShape(10.dp),
        color    = Secondary,
        border   = BorderStroke(1.dp, BorderColor.copy(alpha = 0.5f))
    ) {
        Column(
            modifier            = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, null, tint = MutedFg, modifier = Modifier.size(18.dp))
            Text(label, style = TextStyle(fontSize = 10.sp, color = MutedFg))
            Text(value, style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextFg),
                textAlign = TextAlign.Center)
        }
    }
}