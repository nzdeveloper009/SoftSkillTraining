package com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.mvi.SchedulingEvent
import com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.mvi.SchedulingState
import com.cmp.community.healers.softskilltraining.theme.Border
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.Primary
import com.cmp.community.healers.softskilltraining.theme.Secondary
import com.cmp.community.healers.softskilltraining.theme.TextFg
import com.cmp.community.healers.softskilltraining.utils.constants.DAY_HEADERS
import com.cmp.community.healers.softskilltraining.utils.constants.MONTH_NAMES


@Composable
fun CalendarWidget(
    state:    SchedulingState,
    onEvent:  (SchedulingEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {

        // ── Month navigation ──────────────────────────────────────────────────
        // Use Box + clickable instead of IconButton — IconButton has a hardcoded
        // 48dp min touch target that renders as a large grey circle on screen.
        Row(
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Secondary)
                    .border(1.dp, Border, RoundedCornerShape(8.dp))
                    .clickable { onEvent(SchedulingEvent.PrevMonth) },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.ChevronLeft, null, tint = TextFg, modifier = Modifier.size(15.dp))
            }

            Text(
                "${MONTH_NAMES.getOrElse(state.displayMonth - 1) { "" }} ${state.displayYear}",
                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextFg)
            )

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Secondary)
                    .border(1.dp, Border, RoundedCornerShape(8.dp))
                    .clickable { onEvent(SchedulingEvent.NextMonth) },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.ChevronRight, null, tint = TextFg, modifier = Modifier.size(15.dp))
            }
        }

        // ── Day headers ───────────────────────────────────────────────────────
        Row(modifier = Modifier.fillMaxWidth()) {
            DAY_HEADERS.forEach { d ->
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(d, style = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.Medium, color = MutedFg))
                }
            }
        }

        // ── Date grid ─────────────────────────────────────────────────────────
        val weeks = state.calendarDays.chunked(7)
        weeks.forEach { week ->
            Row(modifier = Modifier.fillMaxWidth()) {
                week.forEach { date ->
                    val isSelected = !date.isOutside &&
                            date.day   == state.selectedDay   &&
                            date.month == state.selectedMonth &&
                            date.year  == state.selectedYear

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(when {
                                isSelected  -> Primary
                                date.isToday && !isSelected -> Primary.copy(alpha = 0.12f)
                                else        -> Color.Transparent
                            })
                            .let {
                                if (!date.isPast && !date.isOutside)
                                    it.clickable { onEvent(SchedulingEvent.DateTapped(date)) }
                                else it
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text  = date.day.toString(),
                            style = TextStyle(
                                fontSize   = 12.sp,
                                fontWeight = if (isSelected || date.isToday) FontWeight.SemiBold else FontWeight.Normal,
                                color      = when {
                                    isSelected   -> Color.White
                                    date.isOutside || date.isPast -> MutedFg.copy(alpha = 0.35f)
                                    date.isToday -> Primary
                                    else         -> TextFg
                                }
                            )
                        )
                    }
                }
            }
        }
    }
}