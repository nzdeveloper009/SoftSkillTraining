package com.cmp.community.healers.softskilltraining.core.file

import androidx.compose.runtime.Composable
import com.cmp.community.healers.softskilltraining.core.file.PickedFile

/**
 * Returns a no-arg lambda that opens the platform-native file picker.
 *
 * ```kotlin
 * // Usage inside any @Composable in commonMain:
 * val launch = rememberFilePicker(MimeType.PDF) { file ->
 *     file?.let { vm.onEvent(DocumentSelected(docType, it.uri)) }
 * }
 * Button(onClick = launch) { Text("Upload") }
 * ```
 *
 * Contract:
 * - Must be called unconditionally at the top level of a @Composable.
 * - [onResult] is always called on the **main thread**.
 * - [onResult] receives **null** when the user cancels.
 */
@Composable
expect fun rememberFilePicker(
    mimeType: String,
    onResult: (PickedFile?) -> Unit
): () -> Unit
