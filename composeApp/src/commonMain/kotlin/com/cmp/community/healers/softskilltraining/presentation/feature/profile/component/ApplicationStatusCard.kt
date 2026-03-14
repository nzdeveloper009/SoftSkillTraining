package com.cmp.community.healers.softskilltraining.presentation.feature.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeState
import com.cmp.community.healers.softskilltraining.theme.Amber
import com.cmp.community.healers.softskilltraining.theme.AmberBg
import com.cmp.community.healers.softskilltraining.theme.AmberBorder
import com.cmp.community.healers.softskilltraining.theme.BlueBg
import com.cmp.community.healers.softskilltraining.theme.BlueBorder
import com.cmp.community.healers.softskilltraining.theme.BlueText
import com.cmp.community.healers.softskilltraining.theme.LocalAppStrings
import com.cmp.community.healers.softskilltraining.theme.SuccessBg
import com.cmp.community.healers.softskilltraining.theme.SuccessBorder
import com.cmp.community.healers.softskilltraining.theme.SuccessText

@Composable
fun ApplicationStatusCard(state: CandidateHomeState) {
    val s = LocalAppStrings.current
    ProfileCard {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            SectionTitle(Icons.Outlined.Assignment, s.appStatus)

            Row(
                modifier              = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusTile(
                    icon     = Icons.Outlined.CheckCircle,
                    iconTint = SuccessText,
                    bg       = SuccessBg,
                    border   = SuccessBorder,
                    title    = s.registrationLabel,
                    subtitle = state.registrationStatus.ifBlank { s.completed },
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                StatusTile(
                    icon     = Icons.Outlined.CalendarMonth,
                    iconTint = BlueText,
                    bg       = BlueBg,
                    border   = BlueBorder,
                    title    = s.trainingStatus,
                    subtitle = state.trainingStatusLabel.ifBlank { s.notScheduled },
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                StatusTile(
                    icon     = Icons.Outlined.EmojiEvents,
                    iconTint = Amber,
                    bg       = AmberBg,
                    border   = AmberBorder,
                    title    = s.certificate,
                    subtitle = if (state.scheduledTrainingDate.isBlank()) s.pendingTraining else s.pending,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
            }
        }
    }
}