package com.cmp.community.healers.softskilltraining.presentation.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.theme.CardBg
import com.cmp.community.healers.softskilltraining.theme.CardBorder
import com.cmp.community.healers.softskilltraining.theme.Primary
import com.cmp.community.healers.softskilltraining.theme.SubtitleHomeColor
import com.cmp.community.healers.softskilltraining.theme.TitleColor

@Composable
fun ScheduledCard(modifier: Modifier, onGoToProfile: () -> Unit) {
    Surface(
        modifier        = modifier.fillMaxWidth(),
        shape           = RoundedCornerShape(16.dp),
        color           = CardBg,
        shadowElevation = 0.dp,
        border          = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Column(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Training Already Scheduled",
                style = TextStyle(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TitleColor,
                    textAlign = TextAlign.Center
                )
            )
            Text(
                "Your training has been scheduled. View your profile for training details.",
                style = TextStyle(
                    fontSize   = 14.sp,
                    color      = SubtitleHomeColor,
                    textAlign  = TextAlign.Center,
                    lineHeight = 21.sp
                )
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick  = onGoToProfile,
                shape    = RoundedCornerShape(10.dp),
                colors   = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor   = Color.White
                ),
                modifier = Modifier.height(46.dp)
            ) {
                Text("Go to Profile", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}