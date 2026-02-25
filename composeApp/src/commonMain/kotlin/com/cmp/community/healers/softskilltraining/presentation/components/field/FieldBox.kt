package com.cmp.community.healers.softskilltraining.presentation.components.field

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.theme.Border
import com.cmp.community.healers.softskilltraining.theme.CardColor
import com.cmp.community.healers.softskilltraining.theme.Destructive
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.Primary
import com.cmp.community.healers.softskilltraining.theme.TextFg

/** Standard outlined text field with label + optional error */
@Composable
fun FieldBox(
    modifier: Modifier, label: String, value: String, placeholder: String, icon: ImageVector,
    onValue: (String) -> Unit, error: String? = null, keyboard: KeyboardType = KeyboardType.Text
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(5.dp)) {
        FieldLabel(label)
        OutlinedTextField(
            value = value, onValueChange = onValue, singleLine = true,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            placeholder = { Text(placeholder, fontSize = 12.sp, color = MutedFg.copy(0.7f)) },
            leadingIcon = { Icon(icon, null, tint = MutedFg, modifier = Modifier.size(16.dp)) },
            isError = error != null,
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = keyboard),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primary, unfocusedBorderColor = Border,
                errorBorderColor = Destructive, focusedContainerColor = CardColor,
                unfocusedContainerColor = CardColor, errorContainerColor = Destructive.copy(0.04f),
                cursorColor = Primary, focusedTextColor = TextFg,
                unfocusedTextColor = TextFg
            ),
            textStyle = TextStyle(fontSize = 13.sp)
        )
        if (error != null) Text(error, color = Destructive, fontSize = 11.sp)
    }
}