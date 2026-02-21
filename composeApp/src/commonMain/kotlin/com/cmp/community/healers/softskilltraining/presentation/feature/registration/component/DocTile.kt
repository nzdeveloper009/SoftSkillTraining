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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.presentation.feature.registration.document_upload.helper.DocumentType
import com.cmp.community.healers.softskilltraining.theme.BorderColor
import com.cmp.community.healers.softskilltraining.theme.CardColor
import com.cmp.community.healers.softskilltraining.theme.Destructive
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.Primary
import com.cmp.community.healers.softskilltraining.theme.Secondary
import com.cmp.community.healers.softskilltraining.theme.TextFg


/*
@Composable
fun DocTile(type: DocumentType, uploaded: Boolean, modifier: Modifier, onPick: () -> Unit) {
    Surface(modifier = modifier, shape = RoundedCornerShape(12.dp),
        color  = if (uploaded) Primary.copy(0.04f) else CardColor,
        border = BorderStroke(1.dp, if (uploaded) Primary.copy(0.25f) else BorderColor.copy(0.6f))
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1f)) {
                Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(if (uploaded) Primary.copy(0.1f) else Secondary), contentAlignment = Alignment.Center) {
                    Icon(if (uploaded) Icons.Outlined.CheckCircle else Icons.Outlined.Description, null, tint = if (uploaded) Primary else MutedFg, modifier = Modifier.size(20.dp))
                }
                Column {
                    Text(type.label, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextFg, lineHeight = 14.sp)
                    Text("MANDATORY", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Destructive)
                    Text(type.format, fontSize = 10.sp, color = MutedFg)
                }
            }
            OutlinedButton(onClick = onPick, shape = RoundedCornerShape(7.dp), border = BorderStroke(1.5.dp, Primary),
                contentPadding = PaddingValues(horizontal = 8.dp), modifier = Modifier.height(32.dp)) {
                Icon(if (uploaded) Icons.Outlined.Edit else Icons.Outlined.Upload, null, tint = Primary, modifier = Modifier.size(13.dp))
                Spacer(Modifier.width(4.dp))
                Text(if (uploaded) "Change" else "Upload", fontSize = 12.sp, color = Primary, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}*/


@Composable
fun DocTile(
    type:     DocumentType,
    uploaded: Boolean,
    modifier: Modifier = Modifier,
    onPick:   () -> Unit
) {
    Surface(
        modifier = modifier,
        shape    = RoundedCornerShape(12.dp),
        color    = if (uploaded) Primary.copy(alpha = 0.04f) else CardColor,
        border   = BorderStroke(1.dp, if (uploaded) Primary.copy(alpha = 0.25f) else BorderColor.copy(alpha = 0.6f))
    ) {
        // ── Vertical layout: icon+text on top, button on bottom ───────────────
        // This prevents the button from stealing width from the text column,
        // which caused single-character-per-line wrapping.
        Column(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            // ── Top row: icon + text labels ───────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Status icon box
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (uploaded) Primary.copy(alpha = 0.12f) else Secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (uploaded) Icons.Outlined.CheckCircle else Icons.Outlined.Description,
                        contentDescription = null,
                        tint     = if (uploaded) Primary else MutedFg,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Text labels — weight(1f) gives all remaining width to this column
                // so text wraps normally instead of becoming 1 character wide.
                Column(
                    modifier            = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text       = type.label,
                        fontSize   = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = TextFg,
                        maxLines   = 2,
                        overflow   = TextOverflow.Ellipsis,
                        lineHeight = 15.sp
                    )
                    Text(
                        text       = "MANDATORY",
                        fontSize   = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color      = Destructive
                    )
                    Text(
                        text     = type.format,
                        fontSize = 10.sp,
                        color    = MutedFg
                    )
                }
            }

            // ── Bottom: full-width upload button ─────────────────────────────
            OutlinedButton(
                onClick        = onPick,
                shape          = RoundedCornerShape(7.dp),
                border         = BorderStroke(1.5.dp, Primary),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                modifier       = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
            ) {
                Icon(
                    imageVector        = if (uploaded) Icons.Outlined.Edit else Icons.Outlined.Upload,
                    contentDescription = null,
                    tint               = Primary,
                    modifier           = Modifier.size(13.dp)
                )
                Spacer(Modifier.width(5.dp))
                Text(
                    text       = if (uploaded) "Change" else "Upload",
                    fontSize   = 12.sp,
                    color      = Primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}