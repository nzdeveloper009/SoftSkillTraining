package com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.mvi

import com.cmp.community.healers.softskilltraining.core.base.UiState
import com.cmp.community.healers.softskilltraining.domain.model.CalendarDate
import com.cmp.community.healers.softskilltraining.utils.constants.scheduling.SchedulingPhase


data class SchedulingState(
    val phase: SchedulingPhase = SchedulingPhase.SELECT_DATE,

    // Calendar navigation
    val displayMonth:     Int             = 0,   // populated in VM init
    val displayYear:      Int             = 0,
    val calendarDays:     List<CalendarDate> = emptyList(),

    // Selected date
    val selectedDay:      Int?            = null,
    val selectedMonth:    Int?            = null,
    val selectedYear:     Int?            = null,
    val selectedDateLabel:String          = "",  // e.g. "Wednesday, February 25, 2026"

    // Confirmation result (from backend / fake)
    val trainingDate:     String          = "",  // e.g. "February 27, 2026"
    val trainingTime:     String          = "10:00 AM",
    val trainingCenter:   String          = "ITC",
    val trainingCenterFull: String        = "Islamabad Test Center",
    val trainingCenterAddress: String     = "Your assigned training center",

    // Loading
    val isScheduling:     Boolean         = false,
) : UiState