package com.cmp.community.healers.softskilltraining.presentation.components.icon

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.cmp.community.healers.softskilltraining.theme.Primary

/** Green rounded icon box — matches site's w-10 h-10 rounded-xl bg-primary/10 */
@Composable
fun IconBox(icon: ImageVector) {
    Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(Primary.copy(0.1f)), contentAlignment = Alignment.Center) {
        Icon(icon, null, tint = Primary, modifier = Modifier.size(20.dp))
    }
}