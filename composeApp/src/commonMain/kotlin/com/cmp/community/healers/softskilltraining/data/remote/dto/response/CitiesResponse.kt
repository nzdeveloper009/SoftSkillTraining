package com.cmp.community.healers.softskilltraining.data.remote.dto.response

import kotlinx.serialization.Serializable

// Outer envelope: { "statusCode": 200, "data": { ... }, ... }
@Serializable
data class CitiesApiResponse(
    val statusCode: Int,
    val data:       CitiesInnerData,
    val message:    String? = null,
    val error:      String? = null
)

// Inner envelope: { "statusCode": 201, "message": "...", "data": [...] }
@Serializable
data class CitiesInnerData(
    val statusCode: Int,
    val message:    String? = null,
    val data:       List<CityData> = emptyList()
)

@Serializable
data class CityData(
    val id:   String,
    val name: String
)