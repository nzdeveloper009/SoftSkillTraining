package com.cmp.community.healers.softskilltraining.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentInitiateApiResponse(
    @SerialName("statusCode") val statusCode: Int,
    @SerialName("data")       val data: PaymentInitiateData,
    @SerialName("message")    val message: String,
    @SerialName("error")      val error: String? = null
)

@Serializable
data class PaymentInitiateData(
    @SerialName("paymentId")     val paymentId: String,
    @SerialName("qrCodeBase64")  val qrCodeBase64: String,
    @SerialName("amount")        val amount: Int,
    @SerialName("expiresAt")     val expiresAt: String,
    @SerialName("transactionId") val transactionId: String,
    @SerialName("orderId")       val orderId: String,
    @SerialName("status")        val status: String
)
