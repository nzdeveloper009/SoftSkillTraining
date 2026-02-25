package com.cmp.community.healers.softskilltraining.presentation.feature.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeState
import com.cmp.community.healers.softskilltraining.theme.Border
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.TextFg


@Composable
fun PersonalInfoCard(state: CandidateHomeState) {
    ProfileCard {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            SectionTitle(Icons.Outlined.Person, "Personal Information")

            val fields = listOf(
                "Father's Name"     to state.fatherName.ifBlank { "N/A" },
                "CNIC Number"       to state.cnicNumber.ifBlank { "N/A" },
                "Date of Birth"     to state.dateOfBirth.ifBlank { "N/A" },
                "City"              to state.city.ifBlank { "N/A" },
                "Address"           to state.address.ifBlank { "N/A" },
                "Registration Date" to state.profileRegistrationDate.ifBlank { "N/A" }
            )

            fields.chunked(2).forEach { pair ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    pair.forEach { (label, value) -> PersonalField(label, value, Modifier.weight(1f)) }
                    if (pair.size == 1) Spacer(Modifier.weight(1f))
                }
            }

            HorizontalDivider(color = Border.copy(alpha = 0.4f))

            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text("Fee Payment", style = TextStyle(fontSize = 12.sp, color = MutedFg))
                FeePaymentBadge(state.profileFeePaymentStatus)
            }
        }
    }
}

@Composable
private fun PersonalField(label: String, value: String, modifier: Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(label, style = TextStyle(fontSize = 11.sp, color = MutedFg))
        Text(value, style = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextFg), maxLines = 2)
    }
}