package com.cmp.community.healers.softskilltraining.presentation.feature.payment.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.theme.Border
import com.cmp.community.healers.softskilltraining.theme.CardColor
import com.cmp.community.healers.softskilltraining.theme.Primary
import com.cmp.community.healers.softskilltraining.theme.Secondary
import com.cmp.community.healers.softskilltraining.theme.TextFg

@Composable
fun PaymentMethodTile(
    icon: ImageVector,
    label:    String,
    selected: Boolean,
    modifier: Modifier,
    onClick:  () -> Unit
) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape    = RoundedCornerShape(12.dp),
        color    = if (selected) Primary.copy(alpha = 0.06f) else Secondary.copy(alpha = 0.3f),
        border   = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) Primary.copy(alpha = 0.4f) else Border.copy(alpha = 0.6f)
        )
    ) {
        Column(
            modifier            = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier.size(44.dp).clip(CircleShape)
                    .background(CardColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = Primary, modifier = Modifier.size(22.dp))
            }
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextFg)
        }
    }
}