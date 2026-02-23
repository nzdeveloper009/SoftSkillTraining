package com.cmp.community.healers.softskilltraining.presentation.feature.payment.component

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


// ─────────────────────────────────────────────────────────────────────────────
// BOTTOM BAR  (matches website: Back | Step 2 of 3 | Continue)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun PaymentBottomBar(
    isPaid: Boolean, modifier: Modifier, onBack: () -> Unit, onContinue: () -> Unit
) {
    Surface(
        modifier        = modifier.fillMaxWidth(),
        color           = CardColor,
        shadowElevation = 12.dp,
        tonalElevation  = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()            // never clip — grow to fit content
                .navigationBarsPadding()
                .padding(horizontal = 16.dp)
                .padding(top = 10.dp, bottom = 14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // ── Row 1: Step label centred ─────────────────────────────────────
            Text(
                text     = "Step 2 of 3  •  Payment",
                style    = TextStyle(fontSize = 12.sp, color = MutedFg),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // ── Row 2: Back  |  Continue to Scheduling ────────────────────────
            //
            // weight() shares the full width between the two buttons so neither
            // can overflow or be hidden — Back gets ~38%, Continue gets ~62%.
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment     = Alignment.CenterVertically
            ) {

                // ── Back to Registration ──────────────────────────────────────
                OutlinedButton(
                    onClick        = onBack,
                    shape          = RoundedCornerShape(10.dp),
                    border         = BorderStroke(2.dp, Primary),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    colors         = ButtonDefaults.outlinedButtonColors(
                        contentColor = TextFg
                    ),
                    modifier = Modifier
                        .weight(0.38f)
                        .height(52.dp)
                ) {
                    Icon(
                        imageVector        = Icons.Outlined.ChevronLeft,
                        contentDescription = null,
                        tint               = Primary,
                        modifier           = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text       = "Back",
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = TextFg,
                        maxLines   = 1
                    )
                }

                // ── Continue to Scheduling ────────────────────────────────────
                Button(
                    onClick        = onContinue,
                    enabled        = isPaid,
                    shape          = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    colors         = ButtonDefaults.buttonColors(
                        containerColor         = Primary,
                        contentColor           = Color.White,
                        disabledContainerColor = Color(0xFFD1D5DB),  // neutral grey — clearly disabled
                        disabledContentColor   = Color(0xFF9CA3AF)
                    ),
                    modifier = Modifier
                        .weight(0.62f)
                        .height(52.dp)
                ) {
                    Text(
                        text       = "Continue to Scheduling",
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines   = 1,
                        softWrap   = false                       // never wraps; stays on one line
                    )
                    Spacer(Modifier.width(6.dp))
                    Icon(
                        imageVector        = Icons.Outlined.ChevronRight,
                        contentDescription = null,
                        modifier           = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}