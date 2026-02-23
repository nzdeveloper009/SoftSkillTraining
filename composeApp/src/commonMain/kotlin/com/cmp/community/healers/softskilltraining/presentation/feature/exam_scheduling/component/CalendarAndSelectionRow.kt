package com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.mvi.SchedulingEvent
import com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.mvi.SchedulingState
import com.cmp.community.healers.softskilltraining.theme.BorderColor
import com.cmp.community.healers.softskilltraining.theme.CardColor
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.Secondary
import com.cmp.community.healers.softskilltraining.utils.constants.scheduling.SchedulingPhase


@Composable
fun CalendarAndSelectionRow(state: SchedulingState, onEvent: (SchedulingEvent) -> Unit) {
    Surface(
        modifier        = Modifier.fillMaxWidth(),
        shape           = RoundedCornerShape(14.dp),
        color           = CardColor,
        border          = BorderStroke(1.dp, BorderColor.copy(alpha = 0.4f)),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

            // Calendar + optional side panel in a Row
            AnimatedContent(
                targetState = state.phase == SchedulingPhase.DATE_SELECTED,
                transitionSpec = {
                    fadeIn(tween(250)) togetherWith fadeOut(tween(150))
                },
                label = "layout"
            ) { dateSelected ->
                if (dateSelected) {
                    // Side-by-side: calendar (left) + selection panel (right)
                    Row(
                        modifier              = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment     = Alignment.Top
                    ) {
                        CalendarWidget(
                            state   = state,
                            onEvent = onEvent,
                            modifier = Modifier.weight(1f)
                        )
                        DateSelectionPanel(
                            state   = state,
                            onEvent = onEvent,
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else {
                    // Calendar only — centred
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CalendarWidget(
                            state    = state,
                            onEvent  = onEvent,
                            modifier = Modifier.widthIn(max = 360.dp)
                        )
                    }
                }
            }

            // Info hint
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Secondary)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Outlined.Info, null, tint = MutedFg, modifier = Modifier.size(14.dp))
                Text("Select any date within the next 30 days",
                    style = TextStyle(fontSize = 11.sp, color = MutedFg)
                )
            }
        }
    }
}