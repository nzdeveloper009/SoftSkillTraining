package com.cmp.community.healers.softskilltraining.presentation.feature.registration.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileOpen
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.presentation.components.card.Card14
import com.cmp.community.healers.softskilltraining.presentation.components.icon.IconBox
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.TextFg

@Composable
fun CompleteRegistrationHeader() {
    Card14 {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            IconBox(Icons.Outlined.FileOpen)
            Column {
                Text("Complete Your Registration", style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextFg))
                Text("Fill in your personal information, upload required documents, and declare your education",
                    style = TextStyle(fontSize = 12.sp, color = MutedFg, lineHeight = 16.sp))
            }
        }
    }
}