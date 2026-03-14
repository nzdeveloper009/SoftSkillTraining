package com.cmp.community.healers.softskilltraining.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import org.jetbrains.skia.Image

@OptIn(ExperimentalEncodingApi::class)
actual fun decodeBase64Bitmap(base64: String): ImageBitmap? = try {
    val bytes = Base64.decode(base64)
    Image.makeFromEncoded(bytes).toComposeImageBitmap()
} catch (e: Exception) {
    null
}