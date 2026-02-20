package com.cmp.community.healers.softskilltraining.presentation.feature.auth.signup.ui

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
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.signup.component.AuthTextField
import com.cmp.community.healers.softskilltraining.theme.*

// ─── Sign Up Screen ───────────────────────────────────────────────────────────
@Composable
fun SignUpScreen(
    onSignUp: (fullName: String, phone: String, password: String) -> Unit = { _, _, _ -> },
    onSignIn: () -> Unit = {}
) {
    var fullName         by remember { mutableStateOf("") }
    var phone            by remember { mutableStateOf("") }
    var password         by remember { mutableStateOf("") }
    var confirmPassword  by remember { mutableStateOf("") }
    var passwordVisible  by remember { mutableStateOf(false) }
    var confirmVisible   by remember { mutableStateOf(false) }

    // Validation states — only shown after first submit attempt
    var submitted        by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current
    val scrollState  = rememberScrollState()

    val nameError     = submitted && fullName.isBlank()
    val phoneError    = submitted && phone.isBlank()
    val passwordError = submitted && password.length < 8
    val confirmError  = submitted && confirmPassword != password

    fun validate(): Boolean {
        submitted = true
        return fullName.isNotBlank()
                && phone.isNotBlank()
                && password.length >= 8
                && confirmPassword == password
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .padding(top = 64.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            // ── Header ───────────────────────────────────────────────────────
            Text(
                text = "Create Account",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = LabelColor,
                    letterSpacing = (-0.5).sp
                )
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Sign up to get started with your candidate portal",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = SubtitleColor
                )
            )

            Spacer(Modifier.height(40.dp))

            // ── Full Name ────────────────────────────────────────────────────
            AuthTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = "Full Name",
                placeholder = "John Doe",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Name",
                        tint = IconColor,
                        modifier = Modifier.size(20.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                isError = nameError,
                errorMessage = "Full name is required"
            )

            Spacer(Modifier.height(24.dp))

            // ── Phone Number ─────────────────────────────────────────────────
            AuthTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Phone Number",
                placeholder = "03001234567",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Phone,
                        contentDescription = "Phone",
                        tint = IconColor,
                        modifier = Modifier.size(20.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                isError = phoneError,
                errorMessage = "Phone number is required"
            )

            Spacer(Modifier.height(24.dp))

            // ── Password ─────────────────────────────────────────────────────
            AuthTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                placeholder = "Min. 8 characters",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Password",
                        tint = IconColor,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Outlined.VisibilityOff
                            else Icons.Outlined.Visibility,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = IconColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                isError = passwordError,
                errorMessage = "Password must be at least 8 characters"
            )

            Spacer(Modifier.height(24.dp))

            // ── Confirm Password ─────────────────────────────────────────────
            AuthTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm Password",
                placeholder = "Re-enter your password",
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Confirm Password",
                        tint = IconColor,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { confirmVisible = !confirmVisible }) {
                        Icon(
                            imageVector = if (confirmVisible) Icons.Outlined.VisibilityOff
                            else Icons.Outlined.Visibility,
                            contentDescription = if (confirmVisible) "Hide password" else "Show password",
                            tint = IconColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                visualTransformation = if (confirmVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        if (validate()) onSignUp(fullName, phone, password)
                    }
                ),
                isError = confirmError,
                errorMessage = "Passwords do not match"
            )

            Spacer(Modifier.height(32.dp))

            // ── Sign Up Button ───────────────────────────────────────────────
            Button(
                onClick = {
                    focusManager.clearFocus()
                    if (validate()) onSignUp(fullName, phone, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreen,
                    contentColor   = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 2.dp
                )
            ) {
                Text(
                    text = "Sign Up",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.3.sp
                    )
                )
            }

            Spacer(Modifier.height(28.dp))

            // ── Divider (optional visual separator) ──────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = DividerColor,
                    thickness = 1.dp
                )
                Text(
                    text = "  or  ",
                    color = SubtitleColor,
                    fontSize = 13.sp
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = DividerColor,
                    thickness = 1.dp
                )
            }

            Spacer(Modifier.height(20.dp))

            // ── Sign In Link ─────────────────────────────────────────────────
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TextButton(onClick = onSignIn) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = SubtitleColor, fontSize = 14.sp)) {
                                append("Already have an account? ")
                            }
                            withStyle(
                                SpanStyle(
                                    color = SignInLinkColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            ) {
                                append("Sign In")
                            }
                        }
                    )
                }
            }
        }
    }
}