package com.cmp.community.healers.softskilltraining.domain.model

data class CalendarDate(
    val day:       Int,
    val month:     Int,    // 1-based
    val year:      Int,
    val isToday:   Boolean = false,
    val isPast:    Boolean = false,       // before today — not selectable
    val isOutside: Boolean = false,       // belongs to prev/next month
)
