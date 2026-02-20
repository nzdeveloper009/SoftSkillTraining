package com.cmp.community.healers.softskilltraining.presentation.feature.auth.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.theme.*


// ─── Reusable Field Composable ────────────────────────────────────────────────

@Composable
internal fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().height(58.dp),
            placeholder = { Text(placeholder, color = Color(0xFFBDBDBD), fontSize = 15.sp) },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            isError = isError,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor   = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5),
                errorContainerColor     = Color(0xFFFFF0F0),
                focusedBorderColor      = Color(0xFF2D6A4F),
                unfocusedBorderColor    = Color.Transparent,
                errorBorderColor        = Color(0xFFE53935),
                cursorColor             = Color(0xFF2D6A4F),
                focusedTextColor        = Color(0xFF1A1A1A),
                unfocusedTextColor      = Color(0xFF1A1A1A)
            ),
            textStyle = TextStyle(fontSize = 15.sp)
        )
        if (isError && errorMessage != null) {
            Spacer(Modifier.height(4.dp))
            Text(errorMessage, color = Color(0xFFE53935), fontSize = 12.sp)
        }
    }
}