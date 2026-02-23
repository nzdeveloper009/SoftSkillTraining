package com.cmp.community.healers.softskilltraining.presentation.feature.payment.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.presentation.feature.payment.mvi.PaymentEvent
import com.cmp.community.healers.softskilltraining.presentation.feature.payment.mvi.PaymentState
import com.cmp.community.healers.softskilltraining.theme.BorderColor
import com.cmp.community.healers.softskilltraining.theme.CardColor
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.PrimaryGrad
import com.cmp.community.healers.softskilltraining.theme.SuccessBg
import com.cmp.community.healers.softskilltraining.theme.SuccessBorder
import com.cmp.community.healers.softskilltraining.theme.SuccessText
import com.cmp.community.healers.softskilltraining.theme.TextFg
import com.cmp.community.healers.softskilltraining.utils.constants.payment.PaymentPhase

// ─────────────────────────────────────────────────────────────────────────────
// REGISTRATION FEE CARD  (all 3 phases inside one card)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun RegistrationFeeCard(
    state: PaymentState,
    onEvent: (PaymentEvent) -> Unit
) {
    Surface(
        modifier        = Modifier.fillMaxWidth(),
        shape           = RoundedCornerShape(14.dp),
        color           = CardColor,
        border          = BorderStroke(1.dp, BorderColor.copy(alpha = 0.4f)),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {

            // ── Green accent bar (matches website h-2 gradient-primary) ────────
            Box(
                modifier = Modifier.fillMaxWidth().height(6.dp)
                    .background(PrimaryGrad)
            )

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // ── Header row: title + PAID badge ────────────────────────────
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Registration Fee",
                            style = TextStyle(
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextFg
                            )
                        )
                        Text("One-time registration fee to activate your candidate profile",
                            style = TextStyle(fontSize = 12.sp, color = MutedFg, lineHeight = 16.sp),
                            modifier = Modifier.padding(top = 4.dp))
                    }
                    AnimatedVisibility(
                        visible = state.phase == PaymentPhase.PAID,
                        enter   = scaleIn(tween(300)) + fadeIn(),
                        exit    = scaleOut() + fadeOut()
                    ) {
                        Surface(
                            shape  = RoundedCornerShape(20.dp),
                            color  = SuccessBg,
                            border = BorderStroke(1.dp, SuccessBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Outlined.CheckCircle, null, tint = SuccessText, modifier = Modifier.size(13.dp))
                                Text("PAID", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SuccessText)
                            }
                        }
                    }
                }

                // ── Amount ────────────────────────────────────────────────────
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("PKR 3,000", style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold, color = TextFg))
                    Text("Included Taxes", style = TextStyle(fontSize = 12.sp, color = MutedFg),
                        modifier = Modifier.padding(bottom = 4.dp))
                }

                // ── Phase-specific content ────────────────────────────────────
                AnimatedContent(
                    targetState = state.phase,
                    transitionSpec = {
                        if (targetState.ordinal > initialState.ordinal)
                            slideInVertically { it } + fadeIn() togetherWith
                                    slideOutVertically { -it } + fadeOut()
                        else
                            slideInVertically { -it } + fadeIn() togetherWith
                                    slideOutVertically { it } + fadeOut()
                    },
                    label = "phase"
                ) { phase ->
                    when (phase) {
                        PaymentPhase.SELECT_METHOD -> SelectMethodPhase(state, onEvent)
                        PaymentPhase.QR_SHOWN      -> QrShownPhase(state, onEvent)
                        PaymentPhase.PAID          -> PaidPhase(state, onEvent)
                    }
                }
            }
        }
    }
}