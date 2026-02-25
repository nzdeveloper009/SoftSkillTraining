package com.cmp.community.healers.softskilltraining.presentation.components.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cmp.community.healers.softskilltraining.theme.Border
import com.cmp.community.healers.softskilltraining.theme.CardColor

/** Plain white card with 14dp radius and subtle border — matches site card */
@Composable
fun Card14(content: @Composable ColumnScope.() -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), color = CardColor,
        border = BorderStroke(1.dp, Border.copy(alpha = 0.6f)), shadowElevation = 1.dp) {
        Column(modifier = Modifier.padding(16.dp), content = content)
    }
}