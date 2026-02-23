package com.cmp.community.healers.softskilltraining.presentation.components.banner

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.theme.Amber
import com.cmp.community.healers.softskilltraining.theme.AmberBg
import com.cmp.community.healers.softskilltraining.theme.AmberBorder
import com.cmp.community.healers.softskilltraining.theme.AmberText

@Composable
fun WarningBanner() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(12.dp),
        color    = AmberBg,
        border   = BorderStroke(1.dp, AmberBorder)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment     = Alignment.Top
        ) {
            Icon(Icons.Outlined.Warning, null, tint = Amber, modifier = Modifier.size(16.dp))
            Text(
                "Please complete the payment to proceed to training scheduling",
                style = TextStyle(fontSize = 13.sp, color = AmberText, lineHeight = 18.sp)
            )
        }
    }
}