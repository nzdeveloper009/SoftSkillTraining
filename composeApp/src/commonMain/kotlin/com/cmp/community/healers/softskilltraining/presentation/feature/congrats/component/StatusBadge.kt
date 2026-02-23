package com.cmp.community.healers.softskilltraining.presentation.feature.congrats.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.theme.SuccessBg
import com.cmp.community.healers.softskilltraining.theme.SuccessBorder
import com.cmp.community.healers.softskilltraining.theme.SuccessText
import com.cmp.community.healers.softskilltraining.theme.TextFg

@Composable
fun StatusBadge(title: String, sub: String, modifier: Modifier) {
    Surface(
        modifier = modifier,
        shape    = RoundedCornerShape(12.dp),
        color    = SuccessBg,
        border   = BorderStroke(1.dp, SuccessBorder)
    ) {
        Column(
            modifier            = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(Icons.Outlined.CheckCircle, null, tint = SuccessText, modifier = Modifier.size(20.dp))
            Text(title, style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextFg
            ),
                textAlign = TextAlign.Center)
            Text(sub, style = TextStyle(fontSize = 11.sp, color = SuccessText, fontWeight = FontWeight.Medium),
                textAlign = TextAlign.Center)
        }
    }
}