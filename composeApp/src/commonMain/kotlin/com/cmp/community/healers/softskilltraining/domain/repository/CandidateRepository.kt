package com.cmp.community.healers.softskilltraining.domain.repository

import com.cmp.community.healers.softskilltraining.core.network.NetworkResult
import com.cmp.community.healers.softskilltraining.data.remote.dto.response.CandidateProfileData
import com.cmp.community.healers.softskilltraining.data.remote.dto.response.PaymentInitiateData
import com.cmp.community.healers.softskilltraining.domain.model.City

interface CandidateRepository {

    suspend fun getCities(accessToken: String): NetworkResult<List<City>>

    suspend fun getProfile(accessToken: String): NetworkResult<CandidateProfileData>

    suspend fun updateProfile(
        accessToken: String,
        cnic: String,
        fatherName: String,
        dob: String,
        address: String,
        city: String?,
        has16YearsEducation: Boolean
    ): NetworkResult<Unit>

    suspend fun uploadDocument(
        accessToken: String,
        type: String,
        fileBytes: ByteArray,
        fileName: String
    ): NetworkResult<Unit>

    suspend fun scheduleExam(
        accessToken: String,
        examDate: String
    ): NetworkResult<Unit>

    suspend fun initiatePayment(accessToken: String): NetworkResult<PaymentInitiateData>
}