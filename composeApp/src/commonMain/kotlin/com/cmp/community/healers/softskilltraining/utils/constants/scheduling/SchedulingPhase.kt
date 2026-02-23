package com.cmp.community.healers.softskilltraining.utils.constants.scheduling

enum class SchedulingPhase {
    /** Calendar shown, no date selected yet */
    SELECT_DATE,
    /** Date chosen — side panel visible with Schedule Training button */
    DATE_SELECTED,
    /** Training scheduled — congratulations screen shown */
    COMPLETE
}