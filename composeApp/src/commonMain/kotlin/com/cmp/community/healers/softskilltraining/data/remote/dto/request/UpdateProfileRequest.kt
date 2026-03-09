package com.cmp.community.healers.softskilltraining.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(
    @SerialName("cnic")                val cnic: String,
    @SerialName("fatherName")          val fatherName: String,
    @SerialName("dob")                 val dob: String,
    @SerialName("address")             val address: String,
    // city is a UUID from the backend; null when no city has been set yet
    @SerialName("city")                val city: String? = null,
    @SerialName("has16YearsEducation") val has16YearsEducation: Boolean
)