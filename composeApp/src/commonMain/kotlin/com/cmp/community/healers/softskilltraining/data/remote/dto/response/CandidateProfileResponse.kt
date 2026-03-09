package com.cmp.community.healers.softskilltraining.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CandidateProfileApiResponse(
    @SerialName("statusCode") val statusCode: Int,
    @SerialName("data")       val data: CandidateProfileData,
    @SerialName("message")    val message: String,
    @SerialName("error")      val error: String? = null
)

@Serializable
data class CandidateProfileData(
    @SerialName("userId")               val userId: String,
    @SerialName("cnic")                 val cnic: String? = null,
    @SerialName("fatherName")           val fatherName: String? = null,
    @SerialName("dob")                  val dob: String? = null,
    @SerialName("cityId")               val cityId: String? = null,
    @SerialName("address")              val address: String? = null,
    @SerialName("has16YearsEducation")  val has16YearsEducation: Boolean = false,
    @SerialName("certificateIssued")    val certificateIssued: Boolean = false,
    @SerialName("createdAt")            val createdAt: String,
    @SerialName("updatedAt")            val updatedAt: String,
    @SerialName("user")                 val user: CandidateUserData? = null,
    @SerialName("documents")            val documents: List<CandidateDocumentData> = emptyList(),
    @SerialName("payment")              val payment: CandidatePaymentData? = null,
    // Populated after POST /candidates/me/schedule succeeds
    @SerialName("examDate")             val examDate: String? = null
)

@Serializable
data class CandidateUserData(
    @SerialName("id")          val id: String,
    @SerialName("firstName")   val firstName: String,
    @SerialName("lastName")    val lastName: String,
    @SerialName("email")       val email: String,
    @SerialName("phoneNumber") val phoneNumber: String,
    @SerialName("status")      val status: String,
    @SerialName("role")        val role: String
)

@Serializable
data class CandidateDocumentData(
    @SerialName("type")         val type: String,
    @SerialName("reviewStatus") val reviewStatus: String,
    @SerialName("fileUrl")      val fileUrl: String? = null,
    @SerialName("candidateId")  val candidateId: String
)

@Serializable
data class CandidatePaymentData(
    @SerialName("isPaid")        val isPaid: Boolean,
    @SerialName("status")        val status: String? = null,
    @SerialName("paidAt")        val paidAt: String? = null,
    @SerialName("transactionId") val transactionId: String? = null
)