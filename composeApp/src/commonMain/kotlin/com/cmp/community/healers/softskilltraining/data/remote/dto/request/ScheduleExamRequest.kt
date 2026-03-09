package com.cmp.community.healers.softskilltraining.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleExamRequest(
    @SerialName("examDate") val examDate: String   // "YYYY-MM-DD"
)