package com.cmp.community.healers.softskilltraining.core.file

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.interop.LocalUIViewController
import com.cmp.community.healers.softskilltraining.utils.constants.MimeType
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.UIKit.UIDocumentPickerDelegateProtocol
import platform.UIKit.UIDocumentPickerViewController
import platform.UIKit.UIModalPresentationFormSheet
import platform.UIKit.UIViewController
import platform.UniformTypeIdentifiers.UTType
import platform.UniformTypeIdentifiers.UTTypeData
import platform.UniformTypeIdentifiers.UTTypeImage
import platform.UniformTypeIdentifiers.UTTypePDF
import platform.darwin.NSObject
import platform.objc.OBJC_ASSOCIATION_RETAIN_NONATOMIC
import platform.objc.objc_setAssociatedObject

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun rememberFilePicker(
    mimeType: String,
    onResult: (PickedFile?) -> Unit
): () -> Unit {

    val hostVC = LocalUIViewController.current

    val utTypes: List<UTType> = remember(mimeType) {
        when (mimeType) {
            MimeType.IMAGE        -> listOf(UTTypeImage)
            MimeType.PDF          -> listOf(UTTypePDF)
            MimeType.IMAGE_OR_PDF -> listOf(UTTypeImage, UTTypePDF)
            else                  -> listOf(UTTypeData)
        }
    }

    return remember(hostVC, mimeType) {
        {
            presentDocumentPicker(
                hostVC   = hostVC,
                utTypes  = utTypes,
                onResult = onResult
            )
        }
    }
}

// ── Presentation ──────────────────────────────────────────────────────────────

@OptIn(ExperimentalForeignApi::class)
private fun presentDocumentPicker(
    hostVC:   UIViewController,
    utTypes:  List<UTType>,
    onResult: (PickedFile?) -> Unit
) {
    val delegate = DocumentPickerDelegate(onResult)

    val picker = UIDocumentPickerViewController(
        forOpeningContentTypes = utTypes,
        asCopy                 = true
    ).apply {
        this.delegate          = delegate
        allowsMultipleSelection = false
        modalPresentationStyle = UIModalPresentationFormSheet
    }

    // ✅ Fix: objc_setAssociatedObject requires a CValuesRef<*>? key, not a String.
    //    Use a StableRef<Unit> as a stable heap-allocated pointer for the key.
    //    The StableRef is disposed when the picker is deallocated (delegate's
    //    documentPickerWasCancelled / didPickDocuments cleans it up via the
    //    delegate itself — retaining delegate via OBJC_ASSOCIATION_RETAIN_NONATOMIC
    //    is sufficient to keep it alive).
    val keyRef = StableRef.create(Unit)
    objc_setAssociatedObject(
        `object`              = picker,
        key                   = keyRef.asCPointer(),   // CValuesRef<*>? ✅
        value                 = delegate,
        policy                = OBJC_ASSOCIATION_RETAIN_NONATOMIC
    )
    // keyRef itself is small — leaked intentionally (same lifetime as picker).
    // If you prefer to clean it up, store keyRef in the delegate and dispose in
    // documentPickerWasCancelled / didPickDocumentsAtURLs.

    hostVC.presentViewController(picker, animated = true, completion = null)
}

// ── Delegate ──────────────────────────────────────────────────────────────────

@OptIn(ExperimentalForeignApi::class)
private class DocumentPickerDelegate(
    private val onResult: (PickedFile?) -> Unit
) : NSObject(), UIDocumentPickerDelegateProtocol {

    override fun documentPicker(
        controller: UIDocumentPickerViewController,
        didPickDocumentsAtURLs: List<*>
    ) {
        val url = didPickDocumentsAtURLs.firstOrNull() as? NSURL ?: run {
            onResult(null); return
        }

        url.startAccessingSecurityScopedResource()

        val name = url.lastPathComponent ?: "file"
        val bytes: ByteArray? = runCatching {
            NSData.dataWithContentsOfURL(url)?.let { nsData ->
                ByteArray(nsData.length.toInt()).also { buf ->
                    buf.usePinned { pinned ->
                        platform.posix.memcpy(pinned.addressOf(0), nsData.bytes, nsData.length)
                    }
                }
            }
        }.getOrNull()

        url.stopAccessingSecurityScopedResource()

        onResult(PickedFile(uri = url.absoluteString ?: url.path ?: "", name = name, bytes = bytes))
    }

    override fun documentPickerWasCancelled(controller: UIDocumentPickerViewController) {
        onResult(null)
    }
}