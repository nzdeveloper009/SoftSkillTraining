package com.cmp.community.healers.softskilltraining.presentation.feature.auth.otp.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.otp.component.OtpBox
import com.cmp.community.healers.softskilltraining.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val OTP_LENGTH = 6
private const val RESEND_COUNTDOWN = 60   // seconds

@Composable
fun OtpScreen(
    phoneNumber: String = "03001234567",          // shown masked in subtitle
    onOtpVerified: (otp: String) -> Unit = {},
    onBack: () -> Unit = {},
    onResendOtp: () -> Unit = {}
) {
    val otpValues      = remember { mutableStateListOf(*Array(OTP_LENGTH) { "" }) }
    val focusRequesters = remember { List(OTP_LENGTH) { FocusRequester() } }
    val focusManager   = LocalFocusManager.current
    val scope          = rememberCoroutineScope()

    var hasError       by remember { mutableStateOf(false) }
    var errorMessage   by remember { mutableStateOf("") }
    var isVerifying    by remember { mutableStateOf(false) }

    // Resend countdown
    var resendTimer    by remember { mutableStateOf(RESEND_COUNTDOWN) }
    var canResend      by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Auto-focus first box
        focusRequesters[0].requestFocus()
        // Start countdown
        while (resendTimer > 0) {
            delay(1000)
            resendTimer--
        }
        canResend = true
    }

    fun restartCountdown() {
        resendTimer = RESEND_COUNTDOWN
        canResend = false
        scope.launch {
            while (resendTimer > 0) {
                delay(1000)
                resendTimer--
            }
            canResend = true
        }
    }

    fun submitOtp() {
        val otp = otpValues.joinToString("")
        if (otp.length < OTP_LENGTH) {
            hasError = true
            errorMessage = "Please enter all 6 digits"
            return
        }
        hasError = false
        isVerifying = true
        // Simulate async — caller handles real verification
        scope.launch {
            delay(600)
            isVerifying = false
            onOtpVerified(otp)
        }
    }

    val maskedPhone = if (phoneNumber.length >= 7)
        phoneNumber.take(4) + "***" + phoneNumber.takeLast(3)
    else phoneNumber

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 56.dp)
        ) {

            // ── Back Button ──────────────────────────────────────────────────
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(FieldBackground)
                    .offset(x = (-4).dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "Back",
                    tint = LabelColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.height(32.dp))

            // ── Header ───────────────────────────────────────────────────────
            Text(
                text = "Verification Code",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LabelColor,
                    letterSpacing = (-0.5).sp
                )
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = "We've sent a 6-digit code to",
                style = TextStyle(
                    fontSize = 15.sp,
                    color = SubtitleColor
                )
            )

            Text(
                text = maskedPhone,
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = LabelColor
                )
            )

            Spacer(Modifier.height(48.dp))

            // ── OTP Boxes ────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                otpValues.forEachIndexed { index, value ->
                    OtpBox(
                        value = value,
                        isFocused = false,   // managed via FocusRequester
                        isError = hasError,
                        modifier = Modifier.weight(1f),
                        focusRequester = focusRequesters[index],
                        onValueChange = { newVal ->
                            // Accept only digits
                            val digit = newVal.filter { it.isDigit() }.take(1)
                            otpValues[index] = digit
                            hasError = false

                            if (digit.isNotEmpty() && index < OTP_LENGTH - 1) {
                                focusRequesters[index + 1].requestFocus()
                            } else if (digit.isNotEmpty() && index == OTP_LENGTH - 1) {
                                focusManager.clearFocus()
                                submitOtp()
                            }
                        },
                        onKeyBack = {
                            if (otpValues[index].isEmpty() && index > 0) {
                                otpValues[index - 1] = ""
                                focusRequesters[index - 1].requestFocus()
                            } else {
                                otpValues[index] = ""
                            }
                        }
                    )
                }
            }

            // ── Error Message ─────────────────────────────────────────────────
            AnimatedVisibility(
                visible = hasError,
                enter = fadeIn() + slideInVertically(),
                exit  = fadeOut()
            ) {
                Column {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = errorMessage,
                        color = ErrorColor,
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(Modifier.height(40.dp))

            // ── Verify Button ─────────────────────────────────────────────────
            Button(
                onClick = { submitOtp() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                enabled = !isVerifying,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor         = PrimaryGreen,
                    contentColor           = Color.White,
                    disabledContainerColor = PrimaryGreen.copy(alpha = 0.6f),
                    disabledContentColor   = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 2.dp
                )
            ) {
                if (isVerifying) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Text(
                        text = "Verify Code",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.3.sp
                        )
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // ── Resend Row ────────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Didn't receive the code? ",
                    color = SubtitleColor,
                    fontSize = 14.sp
                )

                if (canResend) {
                    TextButton(
                        onClick = {
                            onResendOtp()
                            restartCountdown()
                            // Clear OTP fields
                            repeat(OTP_LENGTH) { otpValues[it] = "" }
                            hasError = false
                            focusRequesters[0].requestFocus()
                        },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Resend",
                            color = PrimaryGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    Text(
                        text = "Resend in ",
                        color = SubtitleColor,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "${resendTimer}s",
                        color = PrimaryGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}