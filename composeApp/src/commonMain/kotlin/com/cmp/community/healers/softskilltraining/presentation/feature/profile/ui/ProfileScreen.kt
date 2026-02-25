package com.cmp.community.healers.softskilltraining.presentation.feature.profile.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeState
import com.cmp.community.healers.softskilltraining.presentation.feature.profile.component.ApplicationStatusCard
import com.cmp.community.healers.softskilltraining.presentation.feature.profile.component.PersonalInfoCard
import com.cmp.community.healers.softskilltraining.presentation.feature.profile.component.ProfileHeaderCard
import com.cmp.community.healers.softskilltraining.presentation.feature.profile.component.TrainingScheduledCard
import com.cmp.community.healers.softskilltraining.presentation.feature.profile.component.UploadedDocumentsCard

@Composable
fun ProfileScreen(state: CandidateHomeState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .padding(top = 16.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ProfileHeaderCard(state)
        if (state.scheduledTrainingDate.isNotBlank()) TrainingScheduledCard(state)
        PersonalInfoCard(state)
        ApplicationStatusCard(state)
        UploadedDocumentsCard(state)
    }
}