package com.cmp.community.healers.softskilltraining.presentation.feature.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
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
import com.cmp.community.healers.softskilltraining.theme.SuccessBg
import com.cmp.community.healers.softskilltraining.theme.SuccessBorder
import com.cmp.community.healers.softskilltraining.theme.SuccessText

@Composable
fun ApplicationStatusCard(state: CandidateHomeState) {
    ProfileCard {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            SectionTitle(Icons.Outlined.Assignment, "Application Status")

            // Height(IntrinsicSize.Max) makes all 3 child tiles the same height
            // as the tallest one — no more uneven cards when text wraps.
            Row(
                modifier              = Modifier.fillMaxWidth().height(IntrinsicSize.Max),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatusTile(
                    icon       = Icons.Outlined.CheckCircle,
                    iconTint   = SuccessText,
                    bg         = SuccessBg,
                    border     = SuccessBorder,
                    title      = "Registration",
                    subtitle   = state.registrationStatus.ifBlank { "Completed" },
                    modifier   = Modifier.weight(1f).fillMaxHeight()
                )
                StatusTile(
                    icon       = Icons.Outlined.CalendarMonth,
                    iconTint   = BlueText,
                    bg         = BlueBg,
                    border     = BlueBorder,
                    title      = "Training Status",
                    subtitle   = state.trainingStatusLabel.ifBlank { "Not Scheduled" },
                    modifier   = Modifier.weight(1f).fillMaxHeight()
                )
                StatusTile(
                    icon       = Icons.Outlined.EmojiEvents,
                    iconTint   = Amber,
                    bg         = AmberBg,
                    border     = AmberBorder,
                    title      = "Certificate",
                    subtitle   = if (state.scheduledTrainingDate.isBlank()) "Pending Training" else "Pending",
                    modifier   = Modifier.weight(1f).fillMaxHeight()
                )
            }
        }
    }
}
