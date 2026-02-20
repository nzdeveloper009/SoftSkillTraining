package com.cmp.community.healers.softskilltraining

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.login.ui.SignInScreen
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.otp.ui.OtpScreen
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.signup.ui.SignUpScreen
import org.jetbrains.compose.resources.painterResource

import softskilltraining.composeapp.generated.resources.Res
import softskilltraining.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
        /*SignInScreen(
            onSignIn = { phone, password ->
                println("Phone: $phone, Password: $password")
            },
            onSignUp = {
                println("Sign Up clicked")
            }
        )*/

        SignUpScreen(
            onSignUp = { fullName, phone, password ->
                println("Full Name: $fullName, Phone: $phone, Password: $password")
            },
            onSignIn = {
                println("Sign In clicked")
            }
        )

        /*OtpScreen(
            onOtpVerified = { otp ->
                println("OTP: $otp")
            },
            onBack = {
                println("Back clicked")
            },
            onResendOtp = {
                println("Resend OTP clicked")
            }
        )*/
    }
}

@Composable
private fun DefaultApp() {
    var showContent by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .safeContentPadding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = { showContent = !showContent }) {
            Text("Click me!")
        }
        AnimatedVisibility(showContent) {
            val greeting = remember { Greeting().greet() }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(painterResource(Res.drawable.compose_multiplatform), null)
                Text("Compose: $greeting")
            }
        }
    }
}