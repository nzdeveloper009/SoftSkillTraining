package com.cmp.community.healers.softskilltraining.presentation.components.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.presentation.components.icon.IconBox
import com.cmp.community.healers.softskilltraining.theme.Border
import com.cmp.community.healers.softskilltraining.theme.CardColor
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.TextFg

/** Expandable section card: header always visible, body animated */
@Composable
fun ExpandCard(icon: ImageVector, title: String, subtitle: String, expanded: Boolean, onToggle: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), color = CardColor,
        border = BorderStroke(1.dp, Border.copy(0.4f)), shadowElevation = 1.dp) {
        Column {
            Row(modifier = Modifier.fillMaxWidth().clickable(onClick = onToggle).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                IconBox(icon)
                Column(modifier = Modifier.weight(1f)) {
                    Text(title,    style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextFg))
                    Text(subtitle, style = TextStyle(fontSize = 12.sp, color = MutedFg, lineHeight = 16.sp))
                }
                val rot by animateFloatAsState(if (expanded) 180f else 0f, label = "chevron")
                Icon(Icons.Outlined.KeyboardArrowDown, null, tint = MutedFg,
                    modifier = Modifier.size(20.dp).graphicsLayer { rotationZ = rot })
            }
            AnimatedVisibility(visible = expanded, enter = expandVertically() + fadeIn(), exit = shrinkVertically() + fadeOut()) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp), content = content)
            }
        }
    }
}