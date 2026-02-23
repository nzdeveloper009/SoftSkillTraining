package com.cmp.community.healers.softskilltraining.presentation.feature.congrats.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.theme.BlueBg
import com.cmp.community.healers.softskilltraining.theme.BlueBorder
import com.cmp.community.healers.softskilltraining.theme.BlueText
import com.cmp.community.healers.softskilltraining.theme.SuccessText
import com.cmp.community.healers.softskilltraining.theme.TextFg

@Composable
fun WhatsNextCard() {
    val items = listOf(
        Icons.Outlined.CheckCircle  to "Visit your assigned center on the scheduled date",
        Icons.Outlined.Badge        to "Bring your original CNIC for identity verification",
        Icons.Outlined.Shield       to "The center admin will initiate your training",
        Icons.Outlined.Timer        to "20 questions in 20 minutes - be prepared!"
    )

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(12.dp),
        color    = BlueBg,
        border   = BorderStroke(1.dp, BlueBorder)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Outlined.Info, null, tint = BlueText, modifier = Modifier.size(18.dp))
                Text("What's Next?",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlueText
                    )
                )
            }

            // List items
            items.forEach { (icon, text) ->
                Row(
                    verticalAlignment     = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(icon, null, tint = SuccessText, modifier = Modifier.size(16.dp).padding(top = 1.dp))
                    Text(text,
                        style = TextStyle(fontSize = 12.sp, color = TextFg, lineHeight = 18.sp))
                }
            }
        }
    }
}