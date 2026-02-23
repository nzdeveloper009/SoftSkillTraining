package com.cmp.community.healers.softskilltraining.data.platform

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.cmp.community.healers.softskilltraining.AndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

actual fun saveReceiptToFile(
    fileName: String,
    content:  String,
    onResult: (Boolean) -> Unit
) {
    val context: Context = AndroidApp.Companion.get()  // ← replace with however you expose Context in KMP
    AndroidApp.getCoroutineScope().launch(Dispatchers.IO) {
        val success = runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                saveViaMediaStore(context, fileName, content)
            } else {
                saveToLegacyDownloads(fileName, content)
            }
        }.isSuccess
        withContext(Dispatchers.Main) { onResult(success) }
    }
}

// ── Android 10+ (API 29+) ─────────────────────────────────────────────────────
@RequiresApi(Build.VERSION_CODES.Q)
private fun saveViaMediaStore(context: Context, fileName: String, content: String) {
    val values = ContentValues().apply {
        put(MediaStore.Downloads.DISPLAY_NAME, "$fileName.txt")
        put(MediaStore.Downloads.MIME_TYPE,    "text/plain")
        put(MediaStore.Downloads.IS_PENDING,   1)
    }
    val resolver = context.contentResolver
    val uri      = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
        ?: error("MediaStore insert returned null")

    resolver.openOutputStream(uri)?.use { it.write(content.toByteArray()) }

    values.clear()
    values.put(MediaStore.Downloads.IS_PENDING, 0)
    resolver.update(uri, values, null, null)
}

// ── Android 9 and below ───────────────────────────────────────────────────────
@Suppress("DEPRECATION")
private fun saveToLegacyDownloads(fileName: String, content: String) {
    val dir  = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val file = File(dir, "$fileName.txt")
    file.writeText(content)
}