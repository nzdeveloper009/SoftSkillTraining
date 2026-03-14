package com.cmp.community.healers.softskilltraining.presentation.feature.registration.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.presentation.components.card.ExpandCard
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeEvent
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeState
import com.cmp.community.healers.softskilltraining.theme.Border
import com.cmp.community.healers.softskilltraining.theme.LocalAppStrings
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.Primary
import com.cmp.community.healers.softskilltraining.theme.PrimaryGrad
import com.cmp.community.healers.softskilltraining.theme.Secondary
import com.cmp.community.healers.softskilltraining.theme.TextFg

@Composable
fun EducationDeclarationCard(
    state:        CandidateHomeState,
    onEvent:      (CandidateHomeEvent) -> Unit,
    onPickDegree: () -> Unit
) {
    val s = LocalAppStrings.current
    ExpandCard(Icons.Outlined.MenuBook, s.educationDeclaration, s.educationSub,
        state.educationExpanded, { onEvent(CandidateHomeEvent.ToggleEducationSection) }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Surface(shape = RoundedCornerShape(12.dp), color = Secondary.copy(0.3f), border = BorderStroke(1.dp, Border.copy(0.4f))) {
                Row(modifier = Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                        Text(s.sixteenYears, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextFg)
                        Text(s.sixteenYearsSub, fontSize = 12.sp, color = MutedFg, lineHeight = 16.sp, modifier = Modifier.padding(top = 2.dp))
                    }
                    Switch(
                        checked = state.hasSixteenYearsEducation,
                        onCheckedChange = { onEvent(CandidateHomeEvent.ToggleSixteenYearsEducation) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White, checkedTrackColor = Primary,
                            uncheckedThumbColor = Color.White, uncheckedTrackColor = Border
                        )
                    )
                }
            }
            AnimatedVisibility(visible = state.hasSixteenYearsEducation, enter = expandVertically() + fadeIn(), exit = shrinkVertically() + fadeOut()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Outlined.School, null, tint = Primary, modifier = Modifier.size(18.dp))
                        Text(
                            if (state.degreeUri != null) "${s.uploadDegree.substringBefore("/")} ✓" else s.uploadDegree.substringBefore(" /"),
                            fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Primary
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(16.dp), color = Primary.copy(0.02f),
                        border = BorderStroke(2.dp, if (state.degreeUri != null) PrimaryGrad else Brush.linearGradient(listOf(Border, Border))),
                        modifier = Modifier.fillMaxWidth().clickable { onPickDegree() }
                    ) {
                        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(modifier = Modifier.size(52.dp).clip(CircleShape).background(Primary.copy(0.1f)), contentAlignment = Alignment.Center) {
                                Icon(
                                    if (state.degreeUri != null) Icons.Outlined.CheckCircle else Icons.Outlined.Upload,
                                    null, tint = Primary, modifier = Modifier.size(24.dp)
                                )
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    if (state.degreeUri != null) "Degree uploaded ✓" else s.uploadDegree,
                                    fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextFg
                                )
                                Text(s.uploadDegreeHint, fontSize = 12.sp, color = MutedFg)
                            }
                            Button(
                                onClick = onPickDegree, shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = Color.White),
                                modifier = Modifier.height(40.dp)
                            ) {
                                Text(
                                    if (state.degreeUri != null) s.changeFile else s.upload,
                                    fontWeight = FontWeight.SemiBold, fontSize = 13.sp
                                )
                            }
                        }
                    }
                    Surface(color = Primary.copy(0.05f), shape = RoundedCornerShape(8.dp), border = BorderStroke(1.dp, Primary.copy(0.1f))) {
                        Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.Top) {
                            Icon(Icons.Outlined.Info, null, tint = Primary, modifier = Modifier.size(14.dp))
                            Text(
                                s.educationSub,
                                style = TextStyle(fontSize = 11.sp, color = MutedFg, lineHeight = 16.sp)
                            )
                        }
                    }
                }
            }
        }
    }
}