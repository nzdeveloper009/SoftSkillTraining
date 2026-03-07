package com.cmp.community.healers.softskilltraining.presentation.feature.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.theme.BackgroundWhite
import com.cmp.community.healers.softskilltraining.theme.PrimaryGreen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    vm: SplashViewModel,
    onNavigateToSignIn: () -> Unit,
    onNavigateToCandidateHome: (phone: String) -> Unit
) {
    LaunchedEffect(vm) {
        delay(1500)
        vm.effect.collect { effect ->
            when (effect) {
                is SplashEffect.NavigateToCandidateHome -> onNavigateToCandidateHome(effect.phone)
                SplashEffect.NavigateToSignIn           -> onNavigateToSignIn()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Community Healers",
                style = TextStyle(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryGreen
                )
            )
            Spacer(Modifier.height(32.dp))
            CircularProgressIndicator(
                color = PrimaryGreen,
                modifier = Modifier.size(36.dp),
                strokeWidth = 3.dp
            )
        }
    }
}