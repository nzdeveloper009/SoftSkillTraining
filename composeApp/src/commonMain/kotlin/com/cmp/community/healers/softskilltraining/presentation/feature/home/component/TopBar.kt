package com.cmp.community.healers.softskilltraining.presentation.feature.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeState
import com.cmp.community.healers.softskilltraining.presentation.feature.registration.document_upload.helper.CandidateTab
import com.cmp.community.healers.softskilltraining.theme.AppLanguage
import com.cmp.community.healers.softskilltraining.theme.BorderColor
import com.cmp.community.healers.softskilltraining.theme.CardColor
import com.cmp.community.healers.softskilltraining.theme.Destructive
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.PrimaryGrad
import com.cmp.community.healers.softskilltraining.theme.Secondary
import com.cmp.community.healers.softskilltraining.theme.TextFg

@Composable
fun TopBar(
    state:        CandidateHomeState,
    onTab:        (CandidateTab) -> Unit,
    onLangToggle: () -> Unit,
    onLogout:     () -> Unit
) {
    Surface(
        modifier        = Modifier.fillMaxWidth().shadow(3.dp),
        color           = CardColor,
        tonalElevation  = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 10.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            // ── Row 1: Logo + title  |  Logout ────────────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Logo + title + subtitle
                Row(
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(PrimaryGrad),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.Shield,
                            contentDescription = null,
                            tint     = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                        Text(
                            "Soft Skill Training",
                            style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextFg)
                        )
                        Text(
                            "Candidate Portal",
                            style = TextStyle(fontSize = 11.sp, color = MutedFg)
                        )
                    }
                }

                // Logout — top-right, always visible
                OutlinedButton(
                    onClick        = onLogout,
                    shape          = RoundedCornerShape(8.dp),
                    border         = BorderStroke(1.5.dp, Destructive.copy(alpha = 0.5f)),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                    colors         = ButtonDefaults.outlinedButtonColors(contentColor = Destructive),
                    modifier       = Modifier.height(34.dp)
                ) {
                    Icon(Icons.Outlined.Logout, null, modifier = Modifier.size(14.dp), tint = Destructive)
                    Spacer(Modifier.width(5.dp))
                    Text("Logout", fontSize = 12.sp, color = Destructive, fontWeight = FontWeight.Medium)
                }
            }

            // ── Row 2: Segmented tab  |  Language toggle ──────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Profile | Application segmented control
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Secondary.copy(alpha = 0.5f))
                        .border(1.dp, BorderColor.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    SegTab(
                        label    = "Profile",
                        icon     = Icons.Outlined.Person,
                        selected = state.activeTab == CandidateTab.PROFILE,
                        onClick  = { onTab(CandidateTab.PROFILE) }
                    )
                    SegTab(
                        label    = "Application",
                        icon     = Icons.Outlined.Description,
                        selected = state.activeTab == CandidateTab.REGISTRATION,
                        onClick  = { onTab(CandidateTab.REGISTRATION) }
                    )
                }

                // Language toggle — right side of row 2
                OutlinedButton(
                    onClick        = onLangToggle,
                    shape          = RoundedCornerShape(8.dp),
                    border         = BorderStroke(1.5.dp, BorderColor),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
                    colors         = ButtonDefaults.outlinedButtonColors(contentColor = TextFg),
                    modifier       = Modifier.height(34.dp)
                ) {
                    Icon(Icons.Outlined.Language, null, tint = TextFg, modifier = Modifier.size(15.dp))
                    Spacer(Modifier.width(5.dp))
                    Text(
                        text       = if (state.language == AppLanguage.ENGLISH) "اردو" else "EN",
                        fontSize   = 13.sp,
                        color      = TextFg,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}