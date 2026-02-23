package com.cmp.community.healers.softskilltraining.domain.model

data class StepInfo(
    val label:    String,
    val sub:      String,
    val isDone:   Boolean,
    val isActive: Boolean
)