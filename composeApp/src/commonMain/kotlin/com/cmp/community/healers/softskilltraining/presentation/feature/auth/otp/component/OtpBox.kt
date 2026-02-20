package com.cmp.community.healers.softskilltraining.presentation.feature.auth.otp.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.theme.*

@Composable
fun OtpBox(
    value: String,
    isError: Boolean,
    isExpired: Boolean,
    enabled: Boolean,
    focusRequester: FocusRequester,
    onValueChange: (String) -> Unit,
    onBackspace: () -> Unit,
    modifier: Modifier = Modifier
) {
    var focused by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        targetValue = when {
            isExpired          -> Color(0xFFBDBDBD)
            isError            -> ErrorColor
            focused            -> PrimaryGreen
            value.isNotEmpty() -> PrimaryGreen
            else               -> Color(0xFFE0E0E0)
        },
        animationSpec = tween(150), label = "border"
    )
    val bgColor by animateColorAsState(
        targetValue = when {
            isExpired               -> Color(0xFFF0F0F0)
            isError && value.isNotEmpty() -> Color(0xFFFFF0F0)
            focused                 -> Color.White
            value.isNotEmpty()      -> GreenTint.copy(alpha = 0.3f)
            else                    -> FieldBackground
        },
        animationSpec = tween(150), label = "bg"
    )
    val borderWidth by animateDpAsState(
        targetValue = if (focused || value.isNotEmpty()) 2.dp else 1.5.dp,
        animationSpec = tween(150), label = "bw"
    )

    BasicTextField(
        value = value,
        onValueChange = { if (enabled) onValueChange(it) },
        readOnly = !enabled,
        modifier = modifier
            .aspectRatio(0.85f)
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(borderWidth, borderColor, RoundedCornerShape(14.dp))
            .focusRequester(focusRequester)
            .onFocusChanged { focused = it.isFocused && enabled }
            .onKeyEvent { event ->
                if (enabled && event.type == KeyEventType.KeyDown && event.key == Key.Backspace) {
                    onBackspace(); true
                } else false
            },
        textStyle = TextStyle(
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = if (isExpired) Color(0xFFBDBDBD) else LabelColor,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        singleLine = true,
        enabled = enabled,
        cursorBrush = SolidColor(PrimaryGreen),
        decorationBox = { inner -> Box(contentAlignment = Alignment.Center) { inner() } }
    )
}
