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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.otp.component.OtpBox
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.otp.mvi.OtpEffect
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.otp.mvi.OtpEvent
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.otp.mvi.OtpViewModel
import com.cmp.community.healers.softskilltraining.theme.*
import com.cmp.community.healers.softskilltraining.utils.constants.OTP_LENGTH
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OtpScreen(
    phone: String,
    vm: OtpViewModel = viewModel { OtpViewModel(phone) },
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val focusRequesters = remember { List(OTP_LENGTH) { FocusRequester() } }

    // ── Effects ───────────────────────────────────────────────────────────────
    LaunchedEffect(vm) {
        vm.effect.collect { effect ->
            when (effect) {
                is OtpEffect.MoveFocus      -> focusRequesters.getOrNull(effect.index)?.requestFocus()
                is OtpEffect.NavigateToHome -> onNavigateToHome()
                is OtpEffect.NavigateBack   -> onNavigateBack()
                is OtpEffect.ShowSnackbar   -> { /* wire to SnackbarHost */ }
            }
        }
    }

    // Auto-focus first box on entry
    LaunchedEffect(Unit) { focusRequesters[0].requestFocus() }

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

            // ── Back ──────────────────────────────────────────────────────────
            IconButton(
                onClick = { vm.onEvent(OtpEvent.NavigateBack) },
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(FieldBackground)
            ) {
                Icon(
                    Icons.AutoMirrored.Outlined.ArrowBack, null,
                    tint = LabelColor, modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.height(32.dp))

            // ── Title ─────────────────────────────────────────────────────────
            Text(
                "Verification Code",
                style = TextStyle(
                    fontSize = 32.sp, fontWeight = FontWeight.ExtraBold,
                    color = LabelColor, letterSpacing = (-0.5).sp
                )
            )
            Spacer(Modifier.height(10.dp))

            // Masked phone subtitle
            val masked = phone.let {
                if (it.length >= 7) it.take(4) + "***" + it.takeLast(3) else it
            }
            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(color = SubtitleColor, fontSize = 15.sp)) {
                        append("We've sent a 6-digit code to ")
                    }
                    withStyle(SpanStyle(color = LabelColor, fontWeight = FontWeight.Bold, fontSize = 15.sp)) {
                        append(masked)
                    }
                }
            )

            // ── Hint chip ─────────────────────────────────────────────────────
            Spacer(Modifier.height(12.dp))
            Surface(
                color = GreenTint,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "💡 Hint: enter  1 1 1 1 1 1  to verify",
                    fontSize = 12.sp,
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Spacer(Modifier.height(40.dp))

            // ── OTP Boxes ─────────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (state.isExpired) 0.45f else 1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                state.digits.forEachIndexed { index, digit ->
                    OtpBox(
                        value = digit,
                        isError = state.isError,
                        isExpired = state.isExpired,
                        focusRequester = focusRequesters[index],
                        modifier = Modifier.weight(1f),
                        onValueChange = { vm.onEvent(OtpEvent.DigitEntered(index, it)) },
                        onBackspace   = { vm.onEvent(OtpEvent.BackspacePressed(index)) },
                        enabled = !state.isExpired && !state.isLoading
                    )
                }
            }

            // ── Inline error / expiry messages ────────────────────────────────
            AnimatedVisibility(
                visible = state.isError || state.isExpired,
                enter = fadeIn() + slideInVertically { -it / 2 },
                exit  = fadeOut()
            ) {
                Spacer(Modifier.height(12.dp))
                Surface(
                    color = if (state.isExpired) OrangeExpired.copy(alpha = 0.08f)
                    else ErrorColor.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (state.isExpired)
                            "⏱  OTP expired. Tap Resend to get a new code."
                        else
                            state.errorMessage,
                        color = if (state.isExpired) OrangeExpired else ErrorColor,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // ── Verify button (hidden once expired) ───────────────────────────
            AnimatedVisibility(visible = !state.isExpired) {
                Button(
                    onClick  = { vm.onEvent(OtpEvent.Submit) },
                    enabled  = !state.isLoading,
                    modifier = Modifier.fillMaxWidth().height(58.dp),
                    shape    = RoundedCornerShape(14.dp),
                    colors   = ButtonDefaults.buttonColors(
                        containerColor         = PrimaryGreen,
                        contentColor           = Color.White,
                        disabledContainerColor = PrimaryGreen.copy(alpha = 0.6f),
                        disabledContentColor   = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.5.dp
                        )
                    } else {
                        Text("Verify Code", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold))
                    }
                }
            }

            // ── Resend button (prominent when expired) ────────────────────────
            AnimatedVisibility(
                visible = state.isExpired,
                enter = fadeIn() + expandVertically(),
                exit  = fadeOut() + shrinkVertically()
            ) {
                Button(
                    onClick   = { vm.onEvent(OtpEvent.ResendOtp) },
                    modifier  = Modifier.fillMaxWidth().height(58.dp),
                    shape     = RoundedCornerShape(14.dp),
                    colors    = ButtonDefaults.buttonColors(
                        containerColor = OrangeExpired,
                        contentColor   = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text("Resend OTP", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold))
                }
            }

            Spacer(Modifier.height(28.dp))

            // ── Timer / inline resend row ─────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                when {
                    state.isExpired -> {
                        // Already showing the prominent button above; show subtle link too
                        Text("Didn't receive it? ", color = SubtitleColor, fontSize = 14.sp)
                        TextButton(
                            onClick = { vm.onEvent(OtpEvent.ResendOtp) },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("Resend", color = PrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                    state.canResend -> {
                        Text("Didn't receive the code? ", color = SubtitleColor, fontSize = 14.sp)
                        TextButton(
                            onClick = { vm.onEvent(OtpEvent.ResendOtp) },
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text("Resend", color = PrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                    }
                    else -> {
                        Text("Resend code in ", color = SubtitleColor, fontSize = 14.sp)
                        // Animated countdown
                        AnimatedContent(
                            targetState = state.resendTimer,
                            transitionSpec = {
                                slideInVertically { it } togetherWith slideOutVertically { -it }
                            },
                            label = "timer"
                        ) { seconds ->
                            Text(
                                "${seconds}s",
                                color = PrimaryGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
