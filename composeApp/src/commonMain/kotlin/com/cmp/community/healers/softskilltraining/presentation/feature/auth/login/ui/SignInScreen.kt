package com.cmp.community.healers.softskilltraining.presentation.feature.auth.login.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.component.AuthLabel
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.component.AuthTextField
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.login.mvi.SignInEffect
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.login.mvi.SignInEvent
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.login.mvi.SignInViewModel
import com.cmp.community.healers.softskilltraining.theme.*


// ─── Screen ──────────────────────────────────────────────────────────────────
@Composable
fun SignInScreen(
    vm: SignInViewModel = viewModel { SignInViewModel() },
    onNavigateToHome: (phone: String) -> Unit,
    onNavigateToSignUp: () -> Unit
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(vm) {
        vm.effect.collect { effect ->
            when (effect) {
                is SignInEffect.NavigateToHome    -> onNavigateToHome(effect.phone)
                is SignInEffect.NavigateToSignUp -> onNavigateToSignUp()
                is SignInEffect.ShowSnackbar     -> { /* wire to SnackbarHost if needed */ }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 64.dp)
        ) {
            Text(
                text = "Welcome Back",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LabelColor,
                    letterSpacing = (-0.5).sp
                )
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Sign in to access your candidate portal",
                style = TextStyle(fontSize = 15.sp, color = SubtitleColor)
            )
            Spacer(Modifier.height(40.dp))

            // General error banner
            AnimatedVisibility(visible = state.generalError != null, enter = fadeIn(), exit = fadeOut()) {
                state.generalError?.let { msg ->
                    Surface(
                        color = ErrorColor.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(msg, color = ErrorColor, fontSize = 13.sp, modifier = Modifier.padding(12.dp))
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }

            AuthLabel("Phone Number")
            Spacer(Modifier.height(8.dp))
            AuthTextField(
                value = state.phone,
                onValueChange = { vm.onEvent(SignInEvent.PhoneChanged(it)) },
                placeholder = "03001234567",
                leadingIcon = { Icon(Icons.Outlined.Phone, null, tint = IconTint, modifier = Modifier.size(20.dp)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                isError = state.phoneError != null,
                errorMessage = state.phoneError
            )
            Spacer(Modifier.height(24.dp))

            AuthLabel("Password")
            Spacer(Modifier.height(8.dp))
            AuthTextField(
                value = state.password,
                onValueChange = { vm.onEvent(SignInEvent.PasswordChanged(it)) },
                placeholder = "Enter your password",
                leadingIcon = { Icon(Icons.Outlined.Lock, null, tint = IconTint, modifier = Modifier.size(20.dp)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(); vm.onEvent(SignInEvent.Submit) }),
                isError = state.passwordError != null,
                errorMessage = state.passwordError
            )
            Spacer(Modifier.height(32.dp))

            Button(
                onClick = { focusManager.clearFocus(); vm.onEvent(SignInEvent.Submit) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth().height(58.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen, contentColor = Color.White,
                    disabledContainerColor = PrimaryGreen.copy(alpha = 0.6f), disabledContentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(22.dp), color = Color.White, strokeWidth = 2.5.dp)
                } else {
                    Text("Sign In", style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold))
                }
            }
            Spacer(Modifier.height(28.dp))

            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                TextButton(onClick = { vm.onEvent(SignInEvent.NavigateToSignUp) }) {
                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(color = SubtitleColor, fontSize = 14.sp)) { append("Don't have an account? ") }
                            withStyle(SpanStyle(color = PrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)) { append("Sign Up") }
                        }
                    )
                }
            }
        }
    }
}
