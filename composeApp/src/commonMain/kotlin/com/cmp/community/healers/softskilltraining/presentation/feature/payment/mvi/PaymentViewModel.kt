package com.cmp.community.healers.softskilltraining.presentation.feature.payment.mvi

import androidx.lifecycle.viewModelScope
import com.cmp.community.healers.softskilltraining.core.base.BaseViewModel
import com.cmp.community.healers.softskilltraining.utils.constants.payment.PaymentPhase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class PaymentViewModel : BaseViewModel<PaymentState, PaymentEvent, PaymentEffect>(
    PaymentState()
) {
    override fun handleEvent(event: PaymentEvent) {
        when (event) {
            is PaymentEvent.SelectMethod     -> setState { copy(selectedMethod = event.method) }
            PaymentEvent.GenerateQr          -> generateQr()
            PaymentEvent.ConfirmPayment      -> confirmPayment()
            PaymentEvent.DownloadReceipt     -> downloadReceipt()
            PaymentEvent.BackToRegistration  -> setEffect(PaymentEffect.NavigateBackToRegistration)
            PaymentEvent.ContinueToScheduling -> continueToScheduling()
        }
    }

    // ── Generate QR ───────────────────────────────────────────────────────────

    private fun generateQr() {
        viewModelScope.launch {
            setState { copy(isGeneratingQr = true) }
            delay(800) // simulate network call to get payment URL
            // In production: call backend API to create payment session
            // and get back a unique payment URL / reference
            val qrData = "https://pay.softskilltraining.pk/reg?ref=CP-${generateRef()}&amount=3000"
            setState {
                copy(
                    isGeneratingQr = false,
                    qrContent      = qrData,
                    phase          = PaymentPhase.QR_SHOWN
                )
            }
        }
    }

    // ── Confirm Payment ───────────────────────────────────────────────────────

    private fun confirmPayment() {
        viewModelScope.launch {
            setState { copy(isConfirming = true) }
            delay(1200) // simulate payment verification API call
            val txId   = "#CP-${generateRef()}"
            val date   = currentFormattedDate()
            setState {
                copy(
                    isConfirming   = false,
                    phase          = PaymentPhase.PAID,
                    transactionId  = txId,
                    paymentDate    = date
                )
            }
        }
    }

    // ── Download Receipt ──────────────────────────────────────────────────────

    private fun downloadReceipt() {
        val s = state.value
        val receiptText = buildReceiptText(s.transactionId, s.paymentDate)
        setState { copy(isDownloading = true) }
        // Emit effect — the Screen/platform handles the actual file save
        setEffect(
            PaymentEffect.TriggerReceiptDownload(
                transactionId = s.transactionId,
                paymentDate   = s.paymentDate,
                receiptText   = receiptText
            )
        )
        viewModelScope.launch {
            delay(300)
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

    private fun generateRef(): String =
        (10000000..99999999).random().toString()

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
        ║  Amount         : PKR 3,000
        ║  Status         : SUCCESS
        ║  Description    : Registration Fee
        ╚══════════════════════════════════════╝
        Thank you for your payment.
    """.trimIndent()
}