package com.cmp.community.healers.softskilltraining.data.repository

import com.cmp.community.healers.softskilltraining.core.network.NetworkResult
import com.cmp.community.healers.softskilltraining.data.remote.api.CandidateApi
import com.cmp.community.healers.softskilltraining.data.remote.dto.request.UpdateProfileRequest
import com.cmp.community.healers.softskilltraining.data.remote.dto.response.CandidateProfileData
import com.cmp.community.healers.softskilltraining.domain.model.City
import com.cmp.community.healers.softskilltraining.domain.repository.CandidateRepository

class CandidateRepositoryImpl(
    private val api: CandidateApi
) : CandidateRepository {

    override suspend fun getCities(accessToken: String): NetworkResult<List<City>> =
        when (val result = api.getCities(accessToken)) {
            is NetworkResult.Success -> NetworkResult.Success(result.data.map { City(id = it.id, name = it.name) })
            is NetworkResult.Error   -> result
        }

    override suspend fun getProfile(accessToken: String): NetworkResult<CandidateProfileData> =
        api.getProfile(accessToken)

    override suspend fun updateProfile(
        accessToken: String,
        cnic: String,
        fatherName: String,
        dob: String,
        address: String,
        city: String?,
        has16YearsEducation: Boolean
    ): NetworkResult<Unit> = api.updateProfile(
        accessToken,
        UpdateProfileRequest(cnic, fatherName, dob, address, city, has16YearsEducation)
    )

    override suspend fun uploadDocument(
        accessToken: String,
        type: String,
        fileBytes: ByteArray,
        fileName: String
    ): NetworkResult<Unit> = api.uploadDocument(accessToken, type, fileBytes, fileName)

    override suspend fun scheduleExam(
        accessToken: String,
        examDate: String
    ): NetworkResult<Unit> = api.scheduleExam(accessToken, examDate)
}