package com.cmp.community.healers.softskilltraining.utils

import androidx.compose.ui.graphics.ImageBitmap

/** Decodes a Base64-encoded image string into an [ImageBitmap], or null on failure. */
expect fun decodeBase64Bitmap(base64: String): ImageBitmap?