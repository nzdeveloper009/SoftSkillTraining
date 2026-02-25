package com.cmp.community.healers.softskilltraining.presentation.feature.profile.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeState
import com.cmp.community.healers.softskilltraining.theme.Border
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.Primary
import com.cmp.community.healers.softskilltraining.theme.Secondary
import com.cmp.community.healers.softskilltraining.theme.TextFg

@Composable
fun ProfileHeaderCard(state: CandidateHomeState) {
    ProfileCard {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

            // Avatar + name/ID
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Primary.copy(alpha = 0.08f))
                        .border(1.5.dp, Primary.copy(alpha = 0.2f), RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.Person, null, tint = Primary.copy(0.6f), modifier = Modifier.size(34.dp))
                }
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        state.profileName,
                        style    = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextFg),
                        maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Outlined.Badge, null, tint = MutedFg, modifier = Modifier.size(11.dp))
                        Text(
                            "ID: ${state.profileCandidateId}",
                            style    = TextStyle(fontSize = 11.sp, color = MutedFg),
                            maxLines = 1, overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            HorizontalDivider(color = Border.copy(alpha = 0.5f))

            // Contact rows — stacked vertically so nothing is ever clipped
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ContactRow(Icons.Outlined.Email,      "Email",    state.profileEmail.ifBlank { "N/A" })
                ContactRow(Icons.Outlined.Phone,      "Phone",    state.profilePhone.ifBlank { "N/A" })
                ContactRow(Icons.Outlined.LocationOn, "Location", state.profileLocation.ifBlank { "N/A" })
            }
        }
    }
}

@Composable
private fun ContactRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier              = Modifier.fillMaxWidth(),
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp)).background(Secondary),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Primary, modifier = Modifier.size(15.dp))
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = TextStyle(fontSize = 10.sp, color = MutedFg))
            Text(
                value,
                style    = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextFg),
                maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }
    }
}
