package com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronLeft
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.theme.CardColor
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.Primary
import com.cmp.community.healers.softskilltraining.theme.TextFg

@Composable
fun SchedulingBottomBar(
    dateSelected: Boolean,
    isScheduling: Boolean,
    modifier:     Modifier,
    onBack:       () -> Unit,
    onContinue:   () -> Unit
) {
    Surface(
        modifier        = modifier.fillMaxWidth(),
        color           = CardColor,
        shadowElevation = 12.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
                .padding(top = 10.dp, bottom = 14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Step 3 of 3  •  Scheduling",
                style    = TextStyle(fontSize = 12.sp, color = MutedFg),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment     = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick        = onBack,
                    shape          = RoundedCornerShape(10.dp),
                    border         = BorderStroke(2.dp, Primary),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    colors         = ButtonDefaults.outlinedButtonColors(contentColor = TextFg),
                    modifier       = Modifier.weight(0.38f).height(52.dp)
                ) {
                    Icon(Icons.Outlined.ChevronLeft, null, tint = Primary, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Back", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextFg, maxLines = 1)
                }
                Button(
                    onClick        = onContinue,
                    enabled        = dateSelected && !isScheduling,
                    shape          = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    colors         = ButtonDefaults.buttonColors(
                        containerColor         = Primary,
                        contentColor           = Color.White,
                        disabledContainerColor = Color(0xFFD1D5DB),
                        disabledContentColor   = Color(0xFF9CA3AF)
                    ),
                    modifier = Modifier.weight(0.62f).height(52.dp)
                ) {
                    if (isScheduling) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                        Spacer(Modifier.width(8.dp))
                        Text("Scheduling...", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
                    } else {
                        Text("Complete Registration", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, maxLines = 1, softWrap = false)
                        Spacer(Modifier.width(6.dp))
                        Icon(Icons.Outlined.ChevronRight, null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}