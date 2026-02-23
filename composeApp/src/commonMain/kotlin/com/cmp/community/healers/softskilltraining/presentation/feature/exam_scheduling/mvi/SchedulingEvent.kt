package com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.mvi

import com.cmp.community.healers.softskilltraining.core.base.UiEvent
import com.cmp.community.healers.softskilltraining.domain.model.CalendarDate


sealed interface SchedulingEvent : UiEvent {
    data object PrevMonth                           : SchedulingEvent
    data object NextMonth                           : SchedulingEvent
    data class  DateTapped(val date: CalendarDate)  : SchedulingEvent
    data object ScheduleTraining                    : SchedulingEvent   // "Schedule Training" button
    data object CompleteRegistration                : SchedulingEvent   // bottom bar CTA
    data object BackToPayment                       : SchedulingEvent
    data object GoToProfile                         : SchedulingEvent   // on congrats screen
}