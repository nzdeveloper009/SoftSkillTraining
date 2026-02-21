package com.cmp.community.healers.softskilltraining.core.file

import androidx.compose.runtime.Composable
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberFilePicker(
    mimeType: String,
    onResult: (PickedFile?) -> Unit
): () -> Unit {

    val context = LocalContext.current

    // ActivityResultLauncher must be created at composition time (Compose rule)
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri == null) {
            onResult(null)
            return@rememberLauncherForActivityResult
        }

        val name  = context.resolveFileName(uri) ?: uri.lastPathSegment ?: "file"
        val bytes = runCatching {
            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
        }.getOrNull()

        onResult(PickedFile(uri = uri.toString(), name = name, bytes = bytes))
    }

    // Wrap in a stable lambda so recompositions don't create new instances
    return remember(launcher, mimeType) { { launcher.launch(mimeType) } }
}

// ── Helper ────────────────────────────────────────────────────────────────────

private fun Context.resolveFileName(uri: Uri): String? = runCatching {
    contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val col = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (cursor.moveToFirst() && col != -1) cursor.getString(col) else null
    }
}.getOrNull()