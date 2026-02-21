package com.cmp.community.healers.softskilltraining.utils.constants

// ─────────────────────────────────────────────────────────────────────────────
// MIME constants  (shared — both platforms understand these strings)
// ─────────────────────────────────────────────────────────────────────────────

object MimeType {
    const val IMAGE        = "image/*"
    const val PDF          = "application/pdf"
    const val IMAGE_OR_PDF = "*/*"          // iOS maps to [UTTypeImage, UTTypePDF]
}