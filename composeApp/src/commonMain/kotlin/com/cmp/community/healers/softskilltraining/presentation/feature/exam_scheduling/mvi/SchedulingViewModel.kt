package com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.mvi

import androidx.lifecycle.viewModelScope
import com.cmp.community.healers.softskilltraining.core.base.BaseViewModel
import com.cmp.community.healers.softskilltraining.domain.model.CalendarDate
import com.cmp.community.healers.softskilltraining.utils.constants.scheduling.SchedulingPhase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import kotlin.time.Clock

class SchedulingViewModel : BaseViewModel<SchedulingState, SchedulingEvent, SchedulingEffect>(
    SchedulingState()
) {
    private val today: LocalDate = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault()).date

    // Max selectable date — 30 days from today
    private val maxDate: LocalDate = today.plus(30, DateTimeUnit.DAY)

    init {
        setState {
            copy(
                displayMonth = today.monthNumber,
                displayYear  = today.year,
                calendarDays = buildCalendar(today.monthNumber, today.year)
            )
        }
    }

    override fun handleEvent(event: SchedulingEvent) {
        when (event) {
            SchedulingEvent.PrevMonth          -> navigateMonth(-1)
            SchedulingEvent.NextMonth          -> navigateMonth(+1)
            is SchedulingEvent.DateTapped      -> selectDate(event.date)
            SchedulingEvent.ScheduleTraining   -> scheduleTraining()
            SchedulingEvent.CompleteRegistration -> completeRegistration()
            SchedulingEvent.BackToPayment      -> setEffect(SchedulingEffect.NavigateBackToPayment)
            SchedulingEvent.GoToProfile        -> setEffect(SchedulingEffect.NavigateToProfile)
        }
    }

    // ── Calendar navigation ───────────────────────────────────────────────────

    private fun navigateMonth(delta: Int) {
        val s = state.value
        var m = s.displayMonth + delta
        var y = s.displayYear
        if (m < 1)  { m = 12; y-- }
        if (m > 12) { m = 1;  y++ }
        setState { copy(displayMonth = m, displayYear = y, calendarDays = buildCalendar(m, y)) }
    }

    // ── Date selection ────────────────────────────────────────────────────────

    private fun selectDate(date: CalendarDate) {
        if (date.isPast || date.isOutside) return
        val selected = LocalDate(date.year, date.month, date.day)
        if (selected > maxDate) {
            setEffect(SchedulingEffect.ShowSnackbar("Please select a date within the next 30 days"))
            return
        }
        val label = formatDateLabel(selected)
        setState {
            copy(
                phase             = SchedulingPhase.DATE_SELECTED,
                selectedDay       = date.day,
                selectedMonth     = date.month,
                selectedYear      = date.year,
                selectedDateLabel = label
            )
        }
    }

    // ── Schedule training (fake API) ──────────────────────────────────────────

    private fun scheduleTraining() {
        val s = state.value
        if (s.selectedDay == null) {
            setEffect(SchedulingEffect.ShowSnackbar("Please select a training date"))
            return
        }
        viewModelScope.launch {
            setState { copy(isScheduling = true) }
            delay(1200)   // TODO: replace with real API call
            val confirmedDate = LocalDate(s.selectedYear!!, s.selectedMonth!!, s.selectedDay!!)
            setState {
                copy(
                    isScheduling  = false,
                    phase         = SchedulingPhase.COMPLETE,
                    trainingDate  = formatShortDate(confirmedDate)
                )
            }
        }
    }

    // ── Complete registration (from bottom bar) ────────────────────────────────

    private fun completeRegistration() {
        if (state.value.phase == SchedulingPhase.DATE_SELECTED) {
            scheduleTraining()
        } else {
            setEffect(SchedulingEffect.ShowSnackbar("Please select and schedule a training date"))
        }
    }

    // ── Calendar builder ──────────────────────────────────────────────────────

    private fun buildCalendar(month: Int, year: Int): List<CalendarDate> {
        val firstDay   = LocalDate(year, month, 1)
        // In kotlinx-datetime, dayOfWeek is 1=Mon..7=Sun; we want 0=Sun..6=Sat
        val startOffset = (firstDay.dayOfWeek.ordinal % 7)   // Sun=0
        val daysInMonth = daysInMonth(month, year)
        val daysInPrev  = daysInMonth(if (month == 1) 12 else month - 1, if (month == 1) year - 1 else year)

        val cells = mutableListOf<CalendarDate>()

        // Previous month filler
        for (i in startOffset downTo 1) {
            val prevMonth = if (month == 1) 12 else month - 1
            val prevYear  = if (month == 1) year - 1 else year
            cells.add(CalendarDate(daysInPrev - i + 1, prevMonth, prevYear, isOutside = true, isPast = true))
        }

        // Current month
        for (d in 1..daysInMonth) {
            val date  = LocalDate(year, month, d)
            cells.add(CalendarDate(
                day      = d,
                month    = month,
                year     = year,
                isToday  = date == today,
                isPast   = date < today
            ))
        }

        // Next month filler to complete a 6-row grid (42 cells max)
        val remaining = 42 - cells.size
        val nextMonth = if (month == 12) 1 else month + 1
        val nextYear  = if (month == 12) year + 1 else year
        for (d in 1..remaining) {
            cells.add(CalendarDate(d, nextMonth, nextYear, isOutside = true))
        }

        return cells
    }

    private fun daysInMonth(month: Int, year: Int): Int {
        return when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11            -> 30
            2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
            else -> 30
        }
    }

    // ── Date formatting ───────────────────────────────────────────────────────

    private fun formatDateLabel(date: LocalDate): String {
        val dayName  = date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }
        val monthName = monthName(date.monthNumber)
        return "$dayName, $monthName ${date.dayOfMonth}, ${date.year}"
    }

    private fun formatShortDate(date: LocalDate): String {
        return "${monthName(date.monthNumber)} ${date.dayOfMonth}, ${date.year}"
    }

    private fun monthName(m: Int) = listOf(
        "January","February","March","April","May","June",
        "July","August","September","October","November","December"
    )[m - 1]
}