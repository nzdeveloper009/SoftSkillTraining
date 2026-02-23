package com.cmp.community.healers.softskilltraining.data.platform

import androidx.compose.ui.interop.LocalUIViewController
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.dataUsingEncoding
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

@OptIn(ExperimentalForeignApi::class)
actual fun saveReceiptToFile(
    fileName: String,
    content:  String,
    onResult: (Boolean) -> Unit
) {
    // On iOS we use the share sheet — user can save to Files, Mail, etc.
    val nsString = content as NSString
    val data     = nsString.dataUsingEncoding(NSUTF8StringEncoding)
        ?: run { onResult(false); return }

    val activityVC = UIActivityViewController(
        activityItems        = listOf(data),
        applicationActivities = null
    )

    // Completion handler
    activityVC.completionWithItemsHandler = { _, completed, _, _ ->
        onResult(completed)
    }

    // Present from the root view controller
    val rootVC = UIApplication.sharedApplication.keyWindow?.rootViewController
        ?: run { onResult(false); return }

    // Dismiss any existing presented VC first to avoid "already presenting" crash
    if (rootVC.presentedViewController != null) {
        rootVC.dismissViewControllerAnimated(false, completion = null)
    }
    rootVC.presentViewController(activityVC, animated = true, completion = null)
}