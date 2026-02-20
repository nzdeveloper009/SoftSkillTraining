package com.cmp.community.healers.softskilltraining.presentation.feature.auth.signup.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.component.AuthLabel
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.component.AuthTextField
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.signup.mvi.SignUpEffect
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.signup.mvi.SignUpEvent
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.signup.mvi.SignUpViewModel
import com.cmp.community.healers.softskilltraining.theme.*

// ─── Sign Up Screen ───────────────────────────────────────────────────────────
@Composable
fun SignUpScreen(
    vm: SignUpViewModel = viewModel { SignUpViewModel() },
    onNavigateToOtp: (phone: String) -> Unit,
    onNavigateToSignIn: () -> Unit
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(vm) {
        vm.effect.collect { effect ->
            when (effect) {
                is SignUpEffect.NavigateToOtp -> onNavigateToOtp(effect.phone)
                is SignUpEffect.NavigateToSignIn -> onNavigateToSignIn()
                is SignUpEffect.ShowSnackbar -> { /* wire to SnackbarHost */
                }
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 64.dp, bottom = 32.dp)
        ) {
            // Header
            Text(
                "Create Account",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LabelColor,
                    letterSpacing = (-0.5).sp
                )
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Sign up to get started with your candidate portal",
                style = TextStyle(fontSize = 15.sp, color = SubtitleColor)
            )
            Spacer(Modifier.height(40.dp))

            // General error
            AnimatedVisibility(
                visible = state.generalError != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                state.generalError?.let { msg ->
                    Surface(
                        color = ErrorColor.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            msg,
                            color = ErrorColor,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }

            // Full Name
            AuthLabel("Full Name")
            Spacer(Modifier.height(8.dp))
            AuthTextField(
                value = state.fullName,
                onValueChange = { vm.onEvent(SignUpEvent.FullNameChanged(it)) },
                placeholder = "John Doe",
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Person,
                        null,
                        tint = IconTint,
                        modifier = Modifier.size(20.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                isError = state.fullNameError != null,
                errorMessage = state.fullNameError
            )
            Spacer(Modifier.height(24.dp))

            // Phone
            AuthLabel("Phone Number")
            Spacer(Modifier.height(8.dp))
            AuthTextField(
                value = state.phone,
                onValueChange = { vm.onEvent(SignUpEvent.PhoneChanged(it)) },
                placeholder = "03001234567",
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Phone,
                        null,
                        tint = IconTint,
                        modifier = Modifier.size(20.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                isError = state.phoneError != null,
                errorMessage = state.phoneError
            )
            Spacer(Modifier.height(24.dp))

            // Password
            AuthLabel("Password")
            Spacer(Modifier.height(8.dp))
            AuthTextField(
                value = state.password,
                onValueChange = { vm.onEvent(SignUpEvent.PasswordChanged(it)) },
                placeholder = "Min. 8 characters",
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Lock,
                        null,
                        tint = IconTint,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { vm.onEvent(SignUpEvent.TogglePasswordVisibility) }) {
                        Icon(
                            if (state.passwordVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            null, tint = IconTint, modifier = Modifier.size(20.dp)
                        )
                    }
                },
                visualTransformation = if (state.passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                isError = state.passwordError != null,
                errorMessage = state.passwordError
            )
            Spacer(Modifier.height(24.dp))

            // Confirm Password
            AuthLabel("Confirm Password")
            Spacer(Modifier.height(8.dp))
            AuthTextField(
                value = state.confirmPassword,
                onValueChange = { vm.onEvent(SignUpEvent.ConfirmPasswordChanged(it)) },
                placeholder = "Re-enter your password",
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Lock,
                        null,
                        tint = IconTint,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { vm.onEvent(SignUpEvent.ToggleConfirmVisibility) }) {
                        Icon(
                            if (state.confirmVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                            null, tint = IconTint, modifier = Modifier.size(20.dp)
                        )
                    }
                },
                visualTransformation = if (state.confirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus(); vm.onEvent(
                    SignUpEvent.Submit
                )
                }),
                isError = state.confirmError != null,
                errorMessage = state.confirmError
            )
            Spacer(Modifier.height(32.dp))

            // Submit
            Button(
                onClick = { focusManager.clearFocus(); vm.onEvent(SignUpEvent.Submit) },
                enabled = !state.isLoading,
                modifier = Modifier.fillMaxWidth().height(58.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                    contentColor = Color.White,
                    disabledContainerColor = PrimaryGreen.copy(alpha = 0.6f),
                    disabledContentColor = Color.White
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
                    Text(
                        "Sign Up",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    )
                }
            }
            Spacer(Modifier.height(28.dp))

            // Divider
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f), color = DividerColor)
                Text("  or  ", color = SubtitleColor, fontSize = 13.sp)
                HorizontalDivider(modifier = Modifier.weight(1f), color = DividerColor)
            }
            Spacer(Modifier.height(20.dp))

            // Sign In link
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                TextButton(onClick = { vm.onEvent(SignUpEvent.NavigateToSignIn) }) {
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                SpanStyle(
                                    color = SubtitleColor,
                                    fontSize = 14.sp
                                )
                            ) { append("Already have an account? ") }
                            withStyle(
                                SpanStyle(
                                    color = PrimaryGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            ) { append("Sign In") }
                        }
                    )
                }
            }
        }
    }
}