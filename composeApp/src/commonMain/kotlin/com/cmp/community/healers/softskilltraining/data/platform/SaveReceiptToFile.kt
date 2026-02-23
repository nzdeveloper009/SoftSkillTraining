package com.cmp.community.healers.softskilltraining.data.platform

/**
 * Platform-specific receipt file saver.
 *
 * Android: writes a .txt file to Downloads folder via MediaStore.
 * iOS: presents UIActivityViewController (share sheet) with the receipt text.
 *
 * Both platforms are fire-and-forget; result is reported via [onResult].
 *
 * @param fileName   Suggested filename without extension  e.g. "receipt_CP-12345678"
 * @param content    Plain-text receipt content
 * @param onResult   Called on main thread: true = success, false = failed/cancelled
 */
expect fun saveReceiptToFile(
    fileName: String,
    content:  String,
    onResult: (success: Boolean) -> Unit
)