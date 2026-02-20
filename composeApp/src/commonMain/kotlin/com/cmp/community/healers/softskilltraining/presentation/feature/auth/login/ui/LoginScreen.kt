package com.cmp.community.healers.softskilltraining.presentation.feature.auth.login.ui

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
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import com.cmp.community.healers.softskilltraining.theme.*


// ─── Screen ──────────────────────────────────────────────────────────────────
@Composable
fun SignInScreen(
    onSignIn: (phone: String, password: String) -> Unit = { _, _ -> },
    onSignUp: () -> Unit = {}
) {
    var phone    by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 64.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            // ── Title ────────────────────────────────────────────────────────
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

            // ── Subtitle ─────────────────────────────────────────────────────
            Text(
                text = "Sign in to access your candidate portal",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Normal,
                    color = SubtitleColor
                )
            )

            Spacer(Modifier.height(40.dp))

            // ── Phone Number ─────────────────────────────────────────────────
            Text(
                text = "Phone Number",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = LabelColor
                )
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                placeholder = {
                    Text(
                        "03001234567",
                        color = PlaceholderColor,
                        fontSize = 15.sp
                    )
                },
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
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor   = FieldBackground,
                    unfocusedContainerColor = FieldBackground,
                    focusedBorderColor      = PrimaryGreen,
                    unfocusedBorderColor    = Color.Transparent,
                    cursorColor             = PrimaryGreen,
                    focusedTextColor        = LabelColor,
                    unfocusedTextColor      = LabelColor
                ),
                textStyle = TextStyle(fontSize = 15.sp)
            )

            Spacer(Modifier.height(24.dp))

            // ── Password ─────────────────────────────────────────────────────
            Text(
                text = "Password",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = LabelColor
                )
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                placeholder = {
                    Text(
                        "Enter your password",
                        color = PlaceholderColor,
                        fontSize = 15.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password",
                        tint = IconColor,
                        modifier = Modifier.size(20.dp)
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onSignIn(phone, password)
                    }
                ),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor   = FieldBackground,
                    unfocusedContainerColor = FieldBackground,
                    focusedBorderColor      = PrimaryGreen,
                    unfocusedBorderColor    = Color.Transparent,
                    cursorColor             = PrimaryGreen,
                    focusedTextColor        = LabelColor,
                    unfocusedTextColor      = LabelColor
                ),
                textStyle = TextStyle(fontSize = 15.sp)
            )

            Spacer(Modifier.height(32.dp))

            // ── Sign In Button ───────────────────────────────────────────────
            Button(
                onClick = { onSignIn(phone, password) },
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
                    text = "Sign In",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.3.sp
                    )
                )
            }

            Spacer(Modifier.height(28.dp))

            // ── Sign Up Link ─────────────────────────────────────────────────
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TextButton(onClick = onSignUp) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = SubtitleColor, fontSize = 14.sp)) {
                                append("Don't have an account? ")
                            }
                            withStyle(
                                SpanStyle(
                                    color = SignUpLinkColor,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            ) {
                                append("Sign Up")
                            }
                        }
                    )
                }
            }
        }
    }
}