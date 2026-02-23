package com.cmp.community.healers.softskilltraining.presentation.feature.home.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cmp.community.healers.softskilltraining.core.file.rememberFilePicker
import com.cmp.community.healers.softskilltraining.presentation.feature.home.component.TopBar
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeEffect
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeEvent
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeViewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.registration.component.ApplicationProgressCard
import com.cmp.community.healers.softskilltraining.presentation.feature.registration.component.CompleteRegistrationHeader
import com.cmp.community.healers.softskilltraining.presentation.feature.registration.component.DocumentUploadCard
import com.cmp.community.healers.softskilltraining.presentation.feature.registration.component.EducationDeclarationCard
import com.cmp.community.healers.softskilltraining.presentation.feature.registration.component.PersonalInformationCard
import com.cmp.community.healers.softskilltraining.presentation.feature.registration.document_upload.helper.DocumentType
import com.cmp.community.healers.softskilltraining.theme.BgScreen
import com.cmp.community.healers.softskilltraining.theme.CardColor
import com.cmp.community.healers.softskilltraining.theme.MutedFg
import com.cmp.community.healers.softskilltraining.theme.Primary
import com.cmp.community.healers.softskilltraining.utils.constants.MimeType

@Composable
fun CandidateHomeScreen(
    vm:                  CandidateHomeViewModel = viewModel { CandidateHomeViewModel() },
    onLogout:            () -> Unit             = {},
    onNavigateToPayment: () -> Unit             = {}   // ← called after validation passes
) {
    val state    by vm.state.collectAsStateWithLifecycle()
    val snackbar  = remember { SnackbarHostState() }

    // Pre-build one picker per DocumentType (KMP-safe, no Android-only APIs)
    val docLaunchers: Map<DocumentType, () -> Unit> = DocumentType.entries.associateWith { type ->
        val mime = when {
            type.acceptPdf && type.acceptImage -> MimeType.IMAGE_OR_PDF
            type.acceptPdf                     -> MimeType.PDF
            else                               -> MimeType.IMAGE
        }
        rememberFilePicker(mime) { file ->
            file?.let { vm.onEvent(CandidateHomeEvent.DocumentSelected(type, it.uri)) }
        }
    }

    val degreeLauncher: () -> Unit = rememberFilePicker(MimeType.IMAGE_OR_PDF) { file ->
        file?.let { vm.onEvent(CandidateHomeEvent.DegreeSelected(it.uri)) }
    }

    LaunchedEffect(vm) {
        vm.effect.collect { effect ->
            when (effect) {
                CandidateHomeEffect.NavigateToPayment -> onNavigateToPayment()  // ← wired
                CandidateHomeEffect.NavigateToLogin   -> onLogout()
                is CandidateHomeEffect.PickDocument   -> docLaunchers[effect.type]?.invoke()
                CandidateHomeEffect.PickDegree        -> degreeLauncher()
                is CandidateHomeEffect.ShowSnackbar   -> snackbar.showSnackbar(effect.message)
            }
        }
    }

    Scaffold(
        snackbarHost   = { SnackbarHost(snackbar) },
        containerColor = BgScreen
    ) { pad ->
        Box(modifier = Modifier.fillMaxSize().padding(pad)) {

            Column(modifier = Modifier.fillMaxSize()) {
                TopBar(
                    state        = state,
                    onTab        = { vm.onEvent(CandidateHomeEvent.TabChanged(it)) },
                    onLangToggle = { vm.onEvent(CandidateHomeEvent.ToggleLanguage) },
                    onLogout     = { vm.onEvent(CandidateHomeEvent.Logout) }
                )
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 14.dp)
                        .padding(top = 16.dp, bottom = 96.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    ApplicationProgressCard(state.currentStep, state.totalSteps)
                    CompleteRegistrationHeader()
                    PersonalInformationCard(state) { vm.onEvent(it) }
                    DocumentUploadCard(
                        state    = state,
                        onToggle = { vm.onEvent(CandidateHomeEvent.ToggleDocumentsSection) },
                        onPick   = { type -> vm.onEvent(CandidateHomeEvent.RequestPickDocument(type)) }
                    )
                    EducationDeclarationCard(
                        state        = state,
                        onEvent      = { vm.onEvent(it) },
                        onPickDegree = { vm.onEvent(CandidateHomeEvent.RequestPickDegree) }
                    )
                }
            }

            Surface(
                modifier        = Modifier.align(Alignment.BottomCenter).fillMaxWidth(),
                color           = CardColor,
                shadowElevation = 12.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text("Step 1 of 4 • Registration",
                        style = TextStyle(fontSize = 13.sp, color = MutedFg))
                    Button(
                        onClick  = { vm.onEvent(CandidateHomeEvent.ContinueToPayment) },
                        enabled  = !state.isSubmitting,
                        shape    = RoundedCornerShape(10.dp),
                        colors   = ButtonDefaults.buttonColors(
                            containerColor         = Primary,
                            contentColor           = Color.White,
                            disabledContainerColor = Primary.copy(alpha = 0.55f),
                            disabledContentColor   = Color.White
                        ),
                        modifier = Modifier.height(46.dp)
                    ) {
                        if (state.isSubmitting) {
                            CircularProgressIndicator(
                                modifier    = Modifier.size(18.dp),
                                color       = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Continue to Payment",
                                fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                            Spacer(Modifier.width(6.dp))
                            Icon(Icons.Outlined.ChevronRight, null,
                                modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}