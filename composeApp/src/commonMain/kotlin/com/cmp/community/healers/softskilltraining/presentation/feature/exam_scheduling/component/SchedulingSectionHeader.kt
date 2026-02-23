package com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.theme.BorderColor
import com.cmp.community.healers.softskilltraining.theme.CardColor
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.Primary
import com.cmp.community.healers.softskilltraining.theme.TextFg

@Composable
fun SchedulingSectionHeader() {
    Surface(
        modifier        = Modifier.fillMaxWidth(),
        shape           = RoundedCornerShape(14.dp),
        color           = CardColor,
        border          = BorderStroke(1.dp, BorderColor.copy(alpha = 0.6f)),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp))
                    .background(Primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.CalendarMonth, null, tint = Primary, modifier = Modifier.size(20.dp))
            }
            Column {
                Text("Schedule Your Training",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextFg
                    )
                )
                Text("Select your preferred training date and complete the payment",
                    style = TextStyle(fontSize = 12.sp, color = MutedFg, lineHeight = 16.sp))
            }
        }
    }
}