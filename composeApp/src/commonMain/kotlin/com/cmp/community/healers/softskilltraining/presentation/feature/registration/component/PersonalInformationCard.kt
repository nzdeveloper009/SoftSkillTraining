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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInformationCard(
    state:   CandidateHomeState,
    onEvent: (CandidateHomeEvent) -> Unit
) {
    // ── Local UI state ────────────────────────────────────────────────────────
    var showDatePicker    by remember { mutableStateOf(false) }
    var cityDropdownOpen  by remember { mutableStateOf(false) }

    // Convert stored "DD/MM/YYYY" → epoch millis for DatePicker initial selection
    val initialDateMillis = remember(state.dateOfBirth) {
        if (state.dateOfBirth.isNotBlank()) {
            try {
                val parts = state.dateOfBirth.split("/")
                val date  = LocalDate(parts[2].toInt(), parts[1].toInt(), parts[0].toInt())
                date.toEpochDays().toLong() * 86_400_000L
            } catch (_: Exception) { null }
        } else null
    }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialDateMillis)

    // ── Date Picker Dialog ────────────────────────────────────────────────────
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val epochDays = (millis / 86_400_000L).toInt()
                        val date      = LocalDate.fromEpochDays(epochDays)
                        val formatted = "%02d/%02d/%04d".format(date.dayOfMonth, date.monthNumber, date.year)
                        onEvent(CandidateHomeEvent.DateOfBirthChanged(formatted))
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    ExpandCard(
        Icons.Outlined.Person, "Personal Information", "Enter your official details as per CNIC",
        state.personalExpanded, { onEvent(CandidateHomeEvent.TogglePersonalSection) }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

            // ── Father's Name + CNIC ──────────────────────────────────────────
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FieldBox(
                    Modifier.weight(1f), "Father's Name", state.fatherName, "Aslam Khan",
                    Icons.Outlined.Person,
                    { onEvent(CandidateHomeEvent.FatherNameChanged(it)) },
                    state.errors["fatherName"]
                )
                FieldBox(
                    Modifier.weight(1f), "CNIC Number", state.cnicNumber, "42201-XXXXXXX-X",
                    Icons.Outlined.CreditCard,
                    { onEvent(CandidateHomeEvent.CnicChanged(it)) },
                    state.errors["cnic"], KeyboardType.Number
                )
            }

            // ── Date of Birth + Contact ───────────────────────────────────────
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

                // Date of Birth — tap opens DatePickerDialog
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    FieldLabel("Date of Birth")
                    Box {
                        OutlinedTextField(
                            value         = state.dateOfBirth.ifBlank { "DD/MM/YYYY" },
                            onValueChange = {},
                            enabled       = false,
                            singleLine    = true,
                            modifier      = Modifier.fillMaxWidth().height(50.dp),
                            leadingIcon   = {
                                Icon(Icons.Outlined.CalendarMonth, null, tint = MutedFg, modifier = Modifier.size(16.dp))
                            },
                            shape  = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledContainerColor   = if (state.dateOfBirth.isBlank()) Secondary else CardColor,
                                disabledBorderColor      = if (state.errors["dob"] != null) Destructive else Border,
                                disabledTextColor        = if (state.dateOfBirth.isBlank()) MutedFg else TextFg,
                                disabledLeadingIconColor = MutedFg
                            ),
                            textStyle = TextStyle(fontSize = 13.sp)
                        )
                        // Overlay to capture taps that the disabled TextField swallows
                        Box(modifier = Modifier.matchParentSize().clickable { showDatePicker = true })
                    }
                    state.errors["dob"]?.let { Text(it, color = Destructive, fontSize = 11.sp) }
                }

                // Contact — locked read-only
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    FieldLabel("Contact Number")
                    OutlinedTextField(
                        value = state.contactNumber, onValueChange = {}, enabled = false, singleLine = true,
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        leadingIcon = {
                            Icon(Icons.Outlined.Phone, null, tint = MutedFg, modifier = Modifier.size(16.dp))
                        },
                        shape  = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledContainerColor = Secondary, disabledBorderColor = Border,
                            disabledTextColor = MutedFg, disabledLeadingIconColor = MutedFg
                        ),
                        textStyle = TextStyle(fontSize = 13.sp)
                    )
                }
            }

            // ── City dropdown — ExposedDropdownMenuBox handles all click events ─
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                FieldLabel("City / Area")
                ExposedDropdownMenuBox(
                    expanded         = cityDropdownOpen,
                    onExpandedChange = { cityDropdownOpen = it }
                ) {
                    OutlinedTextField(
                        value         = state.city.ifBlank { "Select your city" },
                        onValueChange = {},
                        readOnly      = true,
                        singleLine    = true,
                        modifier      = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        leadingIcon  = {
                            Icon(Icons.Outlined.LocationOn, null, tint = MutedFg, modifier = Modifier.size(16.dp))
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = cityDropdownOpen)
                        },
                        shape  = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor  = if (state.city.isBlank()) Secondary else CardColor,
                            focusedContainerColor    = if (state.city.isBlank()) Secondary else CardColor,
                            unfocusedBorderColor     = if (state.errors["city"] != null) Destructive else Border,
                            focusedBorderColor       = if (state.errors["city"] != null) Destructive else Primary,
                            unfocusedTextColor       = if (state.city.isBlank()) MutedFg else TextFg,
                            focusedTextColor         = if (state.city.isBlank()) MutedFg else TextFg,
                            unfocusedLeadingIconColor  = MutedFg,
                            focusedLeadingIconColor    = MutedFg,
                            unfocusedTrailingIconColor = MutedFg,
                            focusedTrailingIconColor   = MutedFg
                        ),
                        textStyle = TextStyle(fontSize = 13.sp)
                    )
                    ExposedDropdownMenu(
                        expanded         = cityDropdownOpen,
                        onDismissRequest = { cityDropdownOpen = false }
                    ) {
                        state.cities.forEach { cityItem ->
                            DropdownMenuItem(
                                text        = { Text(cityItem.name, fontSize = 14.sp) },
                                onClick     = {
                                    onEvent(CandidateHomeEvent.CityChanged(cityItem.name))
                                    cityDropdownOpen = false
                                },
                                leadingIcon = {
                                    if (cityItem.name == state.city)
                                        Icon(Icons.Outlined.Check, null, tint = Primary, modifier = Modifier.size(14.dp))
                                }
                            )
                        }
                    }
                }
                state.errors["city"]?.let { Text(it, color = Destructive, fontSize = 11.sp) }
            }

            // ── Address ───────────────────────────────────────────────────────
            FieldBox(
                Modifier, "Address", state.address, "House #, Street #, Sector/Area",
                Icons.Outlined.Home,
                { onEvent(CandidateHomeEvent.AddressChanged(it)) },
                state.errors["address"]
            )
        }
    }
}