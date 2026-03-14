package com.cmp.community.healers.softskilltraining.presentation.feature.profile.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeState
import com.cmp.community.healers.softskilltraining.theme.Border
import com.cmp.community.healers.softskilltraining.theme.LocalAppStrings
import com.cmp.community.healers.softskilltraining.theme.labelFor
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.Primary
import com.cmp.community.healers.softskilltraining.theme.Secondary
import com.cmp.community.healers.softskilltraining.theme.SuccessBg
import com.cmp.community.healers.softskilltraining.theme.SuccessBorder
import com.cmp.community.healers.softskilltraining.theme.SuccessText
import com.cmp.community.healers.softskilltraining.theme.TextFg
import com.cmp.community.healers.softskilltraining.utils.constants.document.DocumentType

@Composable
fun UploadedDocumentsCard(state: CandidateHomeState) {
    val s = LocalAppStrings.current
    ProfileCard {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                SectionTitle(Icons.Outlined.Description, s.uploadedDocs)
                val uploadedCount = state.uploadedDocs.size
                val total         = DocumentType.entries.size
                Surface(
                    shape  = RoundedCornerShape(20.dp),
                    color  = if (uploadedCount > 0) SuccessBg else Secondary,
                    border = BorderStroke(1.dp, if (uploadedCount > 0) SuccessBorder else Border)
                ) {
                    Text(
                        "$uploadedCount of $total ${s.uploaded}",
                        style    = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.SemiBold,
                            color = if (uploadedCount > 0) SuccessText else MutedFg),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            if (state.uploadedDocs.isEmpty()) {
                Column(
                    modifier            = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Outlined.Description, null, tint = MutedFg.copy(0.3f), modifier = Modifier.size(44.dp))
                    Text(s.noDocs, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = MutedFg))
                    Text(s.completeRegForDocs, style = TextStyle(fontSize = 11.sp, color = MutedFg.copy(0.6f)))
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.uploadedDocs.forEach { (docType, uri) ->
                        ProfileDocRow(
                            name      = s.labelFor(docType),
                            uri       = uri,
                            viewLabel = s.view,
                            uploadedLabel = s.uploaded
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileDocRow(name: String, uri: String, viewLabel: String, uploadedLabel: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(10.dp),
        color    = SuccessBg,
        border   = BorderStroke(1.dp, SuccessBorder)
    ) {
        Row(
            modifier              = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(Icons.Outlined.CheckCircle, null, tint = SuccessText, modifier = Modifier.size(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(name, style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = TextFg))
                val fileName = uri.substringAfterLast("/").take(30)
                if (fileName.isNotBlank())
                    Text(fileName, style = TextStyle(fontSize = 10.sp, color = MutedFg), maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Surface(shape = RoundedCornerShape(6.dp), color = Color.White, border = BorderStroke(1.dp, SuccessBorder)) {
                Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Outlined.Visibility, null, tint = Primary, modifier = Modifier.size(12.dp))
                    Text(viewLabel, style = TextStyle(fontSize = 11.sp, color = Primary, fontWeight = FontWeight.SemiBold))
                }
            }
            Surface(shape = RoundedCornerShape(6.dp), color = SuccessBg, border = BorderStroke(1.dp, SuccessBorder)) {
                Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Icon(Icons.Outlined.CheckCircle, null, tint = SuccessText, modifier = Modifier.size(11.dp))
                    Text(uploadedLabel, style = TextStyle(fontSize = 11.sp, color = SuccessText, fontWeight = FontWeight.SemiBold))
                }
            }
        }
    }
}