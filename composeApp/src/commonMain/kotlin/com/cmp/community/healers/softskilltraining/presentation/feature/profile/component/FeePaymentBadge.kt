package com.cmp.community.healers.softskilltraining.presentation.feature.profile.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.Icon
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
import com.cmp.community.healers.softskilltraining.theme.AmberBg
import com.cmp.community.healers.softskilltraining.theme.AmberBorder
import com.cmp.community.healers.softskilltraining.theme.AmberText
import com.cmp.community.healers.softskilltraining.theme.SuccessBg
import com.cmp.community.healers.softskilltraining.theme.SuccessBorder
import com.cmp.community.healers.softskilltraining.theme.SuccessText
import com.cmp.community.healers.softskilltraining.utils.constants.payment.FeePaymentStatus

@Composable
fun FeePaymentBadge(status: FeePaymentStatus) {
    val isPaid = status == FeePaymentStatus.PAID
    Surface(
        shape  = RoundedCornerShape(20.dp),
        color  = if (isPaid) SuccessBg else AmberBg,
        border = BorderStroke(1.dp, if (isPaid) SuccessBorder else AmberBorder)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                if (isPaid) Icons.Outlined.CheckCircle else Icons.Outlined.RadioButtonUnchecked,
                null, tint = if (isPaid) SuccessText else AmberText,
                modifier = Modifier.size(14.dp)
            )
            Text(
                if (isPaid) "Paid" else "Unpaid",
                style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
                    color = if (isPaid) SuccessText else AmberText)
            )
        }
    }
}