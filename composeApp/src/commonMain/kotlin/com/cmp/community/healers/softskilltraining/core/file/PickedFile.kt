package com.cmp.community.healers.softskilltraining.core.file

/**
 * Platform-agnostic file selected by the user.
 *
 * @param uri   Platform URI string — content:// on Android, file:// on iOS
 * @param name  Original filename,  e.g. "passport.pdf"
 * @param bytes Raw bytes — read eagerly so callers don't need platform APIs
 */
data class PickedFile(
    val uri:   String,
    val name:  String,
    val bytes: ByteArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PickedFile) return false
        return uri == other.uri && name == other.name &&
                (bytes ?: byteArrayOf()).contentEquals(other.bytes ?: byteArrayOf())
    }
    override fun hashCode() = 31 * (31 * uri.hashCode() + name.hashCode()) +
            (bytes?.contentHashCode() ?: 0)
}