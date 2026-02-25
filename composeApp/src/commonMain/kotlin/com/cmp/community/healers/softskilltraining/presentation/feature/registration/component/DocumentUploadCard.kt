package com.cmp.community.healers.softskilltraining.presentation.feature.registration.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.presentation.components.card.ExpandCard
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeState
import com.cmp.community.healers.softskilltraining.utils.constants.document.DocumentType
import com.cmp.community.healers.softskilltraining.theme.Amber
import com.cmp.community.healers.softskilltraining.theme.AmberBg
import com.cmp.community.healers.softskilltraining.theme.AmberBorder
import com.cmp.community.healers.softskilltraining.theme.AmberText
import com.cmp.community.healers.softskilltraining.theme.Destructive

@Composable
fun DocumentUploadCard(
    state: CandidateHomeState,
    onToggle: () -> Unit,
    onPick: (DocumentType) -> Unit     // fires RequestPickDocument event
) {
    ExpandCard(
        Icons.Outlined.Upload,
        "Document Upload",
        "Upload high-quality scans of your original documents",
        state.documentsExpanded,
        onToggle
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            DocumentType.entries.chunked(2).forEach { pair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    pair.forEach { type ->
                        DocTile(
                            type,
                            type in state.uploadedDocs,
                            Modifier.weight(1f)
                        ) { onPick(type) }
                    }
                    if (pair.size == 1) Spacer(Modifier.weight(1f))
                }
            }
            state.errors["docs"]?.let { Text(it, color = Destructive, fontSize = 11.sp) }
            Surface(
                color = AmberBg,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, AmberBorder)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Outlined.Info, null, tint = Amber, modifier = Modifier.size(16.dp))
                    Text(
                        "Please ensure all documents are clearly legible. Blurry or incorrect documents may lead to registration rejection. Max file size: 5MB.",
                        style = TextStyle(fontSize = 11.sp, color = AmberText, lineHeight = 16.sp)
                    )
                }
            }
        }
    }
}