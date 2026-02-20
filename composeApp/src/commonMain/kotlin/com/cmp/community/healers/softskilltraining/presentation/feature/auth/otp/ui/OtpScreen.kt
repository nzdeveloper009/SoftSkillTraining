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

    // Effects — one-shot navigation / focus commands
    LaunchedEffect(vm) {
        vm.effect.collect { effect ->
            when (effect) {
                is OtpEffect.MoveFocus     -> focusRequesters.getOrNull(effect.index)?.requestFocus()
                is OtpEffect.NavigateToHome -> onNavigateToHome()
                is OtpEffect.NavigateBack  -> onNavigateBack()
                is OtpEffect.ShowSnackbar  -> { /* wire to SnackbarHost */ }
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
            // Back button
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

            // Title
            Text(
                "Verification Code",
                style = TextStyle(
                    fontSize = 32.sp, fontWeight = FontWeight.ExtraBold,
                    color = LabelColor, letterSpacing = (-0.5).sp
                )
            )
            Spacer(Modifier.height(10.dp))

            // Subtitle with masked phone
            val masked = phone.let {
                if (it.length >= 7) it.take(4) + "***" + it.takeLast(3) else it
            }
            Text("We've sent a 6-digit code to", style = TextStyle(fontSize = 15.sp, color = SubtitleColor))
            Text(masked, style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, color = LabelColor))

            Spacer(Modifier.height(48.dp))

            // OTP boxes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                state.digits.forEachIndexed { index, digit ->
                    OtpBox(
                        value = digit,
                        isError = state.isError,
                        focusRequester = focusRequesters[index],
                        modifier = Modifier.weight(1f),
                        onValueChange = { vm.onEvent(OtpEvent.DigitEntered(index, it)) },
                        onBackspace   = { vm.onEvent(OtpEvent.BackspacePressed(index)) }
                    )
                }
            }

            // Inline error
            AnimatedVisibility(
                visible = state.isError,
                enter = fadeIn() + slideInVertically { -it / 2 },
                exit  = fadeOut()
            ) {
                Column {
                    Spacer(Modifier.height(12.dp))
                    Text(state.errorMessage, color = ErrorColor, fontSize = 13.sp)
                }
            }

            Spacer(Modifier.height(40.dp))

            // Verify button
            Button(
                onClick  = { vm.onEvent(OtpEvent.Submit) },
                enabled  = !state.isLoading,
                modifier = Modifier.fillMaxWidth().height(58.dp),
                shape    = RoundedCornerShape(14.dp),
                colors   = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen, contentColor = Color.White,
                    disabledContainerColor = PrimaryGreen.copy(alpha = 0.6f), disabledContentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(22.dp), color = Color.White, strokeWidth = 2.5.dp)
                } else {
                    Text("Verify Code", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold))
                }
            }

            Spacer(Modifier.height(32.dp))

            // Resend row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Didn't receive the code? ", color = SubtitleColor, fontSize = 14.sp)
                if (state.canResend) {
                    TextButton(
                        onClick = { vm.onEvent(OtpEvent.ResendOtp) },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("Resend", color = PrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                } else {
                    Text("Resend in ", color = SubtitleColor, fontSize = 14.sp)
                    Text("${state.resendTimer}s", color = PrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}
