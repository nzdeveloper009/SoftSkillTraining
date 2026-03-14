package com.cmp.community.healers.softskilltraining.presentation.feature.payment.mvi

import androidx.lifecycle.viewModelScope
import com.cmp.community.healers.softskilltraining.core.base.BaseViewModel
import com.cmp.community.healers.softskilltraining.core.network.NetworkResult
import com.cmp.community.healers.softskilltraining.core.storage.TokenStorage
import com.cmp.community.healers.softskilltraining.domain.repository.CandidateRepository
import com.cmp.community.healers.softskilltraining.utils.constants.payment.PaymentPhase
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class PaymentViewModel(
    private val candidateRepository: CandidateRepository
) : BaseViewModel<PaymentState, PaymentEvent, PaymentEffect>(
    PaymentState()
) {
    override fun handleEvent(event: PaymentEvent) {
        when (event) {
            PaymentEvent.GenerateQr           -> generateQr()
            PaymentEvent.ConfirmPayment        -> confirmPayment()
            PaymentEvent.DownloadReceipt       -> downloadReceipt()
            PaymentEvent.BackToRegistration    -> setEffect(PaymentEffect.NavigateBackToRegistration)
            PaymentEvent.ContinueToScheduling  -> continueToScheduling()
        }
    }

    // ── Initiate payment & generate QR ───────────────────────────────────────

    private fun generateQr() {
        val token = TokenStorage.accessToken
        if (token.isNullOrBlank()) {
            setEffect(PaymentEffect.ShowSnackbar("Session expired. Please log in again."))
            return
        }
        viewModelScope.launch {
            setState { copy(isGeneratingQr = true) }
            when (val result = candidateRepository.initiatePayment(token)) {
                is NetworkResult.Success -> {
                    setState {
                        copy(
                            isGeneratingQr = false,
                            qrCodeBase64   = result.data.qrCodeBase64,
                            paymentId      = result.data.paymentId,
                            transactionId  = result.data.transactionId,
                            phase          = PaymentPhase.QR_SHOWN
                        )
                    }
                }
                is NetworkResult.Error -> {
                    // Demo fallback: show QR phase with empty base64 so QrShownPhase
                    // renders a locally-generated demo QR for client recording purposes.
                    setState {
                        copy(
                            isGeneratingQr = false,
                            qrCodeBase64   = "",
                            paymentId      = "DEMO-PAY-001",
                            transactionId  = "TXN-DEMO-${System.currentTimeMillis()}",
                            phase          = PaymentPhase.QR_SHOWN
                        )
                    }
                }
            }
        }
    }

    // ── Confirm Payment ───────────────────────────────────────────────────────

    private fun confirmPayment() {
        viewModelScope.launch {
            setState { copy(isConfirming = true) }
            // TODO: call confirm payment API when available
            val date = currentFormattedDate()
            setState {
                copy(
                    isConfirming  = false,
                    phase         = PaymentPhase.PAID,
                    paymentDate   = date
                )
            }
        }
    }

    // ── Download Receipt ──────────────────────────────────────────────────────

    private fun downloadReceipt() {
        val s = state.value
        val receiptText = buildReceiptText(s.transactionId, s.paymentDate)
        setState { copy(isDownloading = true) }
        setEffect(
            PaymentEffect.TriggerReceiptDownload(
                transactionId = s.transactionId,
                paymentDate   = s.paymentDate,
                receiptText   = receiptText
            )
        )
        viewModelScope.launch {
            setState { copy(isDownloading = false) }
            setEffect(PaymentEffect.ShowSnackbar("Receipt downloaded successfully"))
        }
    }

    // ── Continue to Scheduling ────────────────────────────────────────────────

    private fun continueToScheduling() {
        if (state.value.phase != PaymentPhase.PAID) {
            setEffect(PaymentEffect.ShowSnackbar("Please complete payment first"))
            return
        }
        setEffect(PaymentEffect.NavigateToScheduling)
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun currentFormattedDate(): String {
        val now   = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        val month = now.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
        return "$month ${now.dayOfMonth}, ${now.year}"
    }

    private fun buildReceiptText(txId: String, date: String): String = """
        ╔══════════════════════════════════════╗
        ║     SOFT SKILL TRAINING RECEIPT      ║
        ╠══════════════════════════════════════╣
        ║  Transaction ID : $txId
        ║  Payment Date   : $date
        ║  Amount         : PKR 5,000
        ║  Status         : SUCCESS
        ║  Description    : Registration Fee
        ╚══════════════════════════════════════╝
        Thank you for your payment.
    """.trimIndent()
}