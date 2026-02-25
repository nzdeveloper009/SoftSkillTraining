package com.cmp.community.healers.softskilltraining.presentation.feature.registration.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.presentation.components.card.ExpandCard
import com.cmp.community.healers.softskilltraining.presentation.components.field.FieldBox
import com.cmp.community.healers.softskilltraining.presentation.components.field.FieldLabel
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeEvent
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeState
import com.cmp.community.healers.softskilltraining.theme.Border
import com.cmp.community.healers.softskilltraining.theme.CardColor
import com.cmp.community.healers.softskilltraining.theme.Destructive
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.Primary
import com.cmp.community.healers.softskilltraining.theme.Secondary
import com.cmp.community.healers.softskilltraining.theme.TextFg
import com.cmp.community.healers.softskilltraining.utils.constants.PAKISTAN_CITIES

@Composable
fun PersonalInformationCard(
    state:   CandidateHomeState,
    onEvent: (CandidateHomeEvent) -> Unit
) {
    ExpandCard(Icons.Outlined.Person, "Personal Information", "Enter your official details as per CNIC",
        state.personalExpanded, { onEvent(CandidateHomeEvent.TogglePersonalSection) }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FieldBox(Modifier.weight(1f), "Father's Name", state.fatherName, "Aslam Khan", Icons.Outlined.Person,
                    { onEvent(CandidateHomeEvent.FatherNameChanged(it)) }, state.errors["fatherName"])
                FieldBox(Modifier.weight(1f), "CNIC Number", state.cnicNumber, "42201-XXXXXXX-X", Icons.Outlined.CreditCard,
                    { onEvent(CandidateHomeEvent.CnicChanged(it)) }, state.errors["cnic"], KeyboardType.Number)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FieldBox(Modifier.weight(1f), "Date of Birth", state.dateOfBirth, "DD/MM/YYYY", Icons.Outlined.CalendarMonth,
                    { onEvent(CandidateHomeEvent.DateOfBirthChanged(it)) }, state.errors["dob"])
                // Contact — locked read-only
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    FieldLabel("Contact Number")
                    OutlinedTextField(
                        value = state.contactNumber, onValueChange = {}, enabled = false, singleLine = true,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        leadingIcon = { Icon(Icons.Outlined.Phone, null, tint = MutedFg, modifier = Modifier.size(16.dp)) },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledContainerColor = Secondary, disabledBorderColor = Border,
                            disabledTextColor = MutedFg, disabledLeadingIconColor = MutedFg
                        ),
                        textStyle = TextStyle(fontSize = 13.sp)
                    )
                }
            }
            // City dropdown
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                FieldLabel("City / Area")
                Box {
                    OutlinedTextField(
                        value = state.city.ifBlank { "Select your city" }, onValueChange = {}, enabled = false, singleLine = true,
                        modifier = Modifier.fillMaxWidth().height(50.dp).clickable { onEvent(CandidateHomeEvent.ToggleCityDropdown) },
                        leadingIcon  = { Icon(Icons.Outlined.LocationOn, null, tint = MutedFg, modifier = Modifier.size(16.dp)) },
                        trailingIcon = { Icon(Icons.Outlined.KeyboardArrowDown, null, tint = MutedFg) },
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledContainerColor    = if (state.city.isBlank()) Secondary else CardColor,
                            disabledBorderColor       = if (state.errors["city"] != null) Destructive else Border,
                            disabledTextColor         = if (state.city.isBlank()) MutedFg else TextFg,
                            disabledLeadingIconColor  = MutedFg,
                            disabledTrailingIconColor = MutedFg
                        ),
                        textStyle = TextStyle(fontSize = 13.sp)
                    )
                    DropdownMenu(expanded = state.cityDropdownOpen, onDismissRequest = { onEvent(CandidateHomeEvent.ToggleCityDropdown) }) {
                        PAKISTAN_CITIES.forEach { city ->
                            DropdownMenuItem(
                                text   = { Text(city, fontSize = 14.sp) },
                                onClick = { onEvent(CandidateHomeEvent.CityChanged(city)) },
                                leadingIcon = { if (city == state.city) Icon(Icons.Outlined.Check, null, tint = Primary, modifier = Modifier.size(14.dp)) }
                            )
                        }
                    }
                }
                state.errors["city"]?.let { Text(it, color = Destructive, fontSize = 11.sp) }
            }
            FieldBox(Modifier, "Address", state.address, "House #, Street #, Sector/Area", Icons.Outlined.Home,
                { onEvent(CandidateHomeEvent.AddressChanged(it)) }, state.errors["address"])
        }
    }
}