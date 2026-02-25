package com.cmp.community.healers.softskilltraining.presentation.feature.registration.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.presentation.feature.registration.document_upload.helper.DocumentType
import com.cmp.community.healers.softskilltraining.theme.Border
import com.cmp.community.healers.softskilltraining.theme.CardColor
import com.cmp.community.healers.softskilltraining.theme.Destructive
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.Primary
import com.cmp.community.healers.softskilltraining.theme.Secondary
import com.cmp.community.healers.softskilltraining.theme.TextFg

@Composable
fun DocRow(type: DocumentType, uploaded: Boolean, modifier: Modifier, onPick: () -> Unit) {
    Surface(
        modifier = modifier,
        shape    = RoundedCornerShape(12.dp),
        color    = if (uploaded) Primary.copy(alpha = 0.04f) else CardColor,
        border   = BorderStroke(1.dp, if (uploaded) Primary.copy(alpha = 0.25f) else Border.copy(alpha = 0.6f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            // Icon + labels
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier              = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (uploaded) Primary.copy(alpha = 0.1f) else Secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (uploaded) Icons.Outlined.CheckCircle else Icons.Outlined.Description,
                        null,
                        tint     = if (uploaded) Primary else MutedFg,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(type.label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextFg, lineHeight = 14.sp)
                    Text("MANDATORY", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Destructive)
                    Text(type.format, fontSize = 10.sp, color = MutedFg)
                }
            }

            // Upload button
            OutlinedButton(
                onClick        = onPick,
                shape          = RoundedCornerShape(7.dp),
                border         = BorderStroke(1.5.dp, Primary),
                contentPadding = PaddingValues(horizontal = 8.dp),
                modifier       = Modifier.height(32.dp)
            ) {
                Icon(
                    if (uploaded) Icons.Outlined.Edit else Icons.Outlined.Upload,
                    null, tint = Primary, modifier = Modifier.size(13.dp)
                )
                Spacer(Modifier.width(4.dp))
                Text(if (uploaded) "Change" else "Upload", fontSize = 12.sp, color = Primary, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}