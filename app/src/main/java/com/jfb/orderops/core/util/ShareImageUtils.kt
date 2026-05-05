package com.jfb.orderops.core.util

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

object ShareImageUtils {

    fun shareBitmap(
        context: Context,
        bitmap: Bitmap
    ) {
        val receiptsDir = File(context.cacheDir, "receipts").apply {
            mkdirs()
        }

        val file = File(receiptsDir, "orderops-receipt.png")

        FileOutputStream(file).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
        }

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            clipData = ClipData.newUri(
                context.contentResolver,
                "Comprovante OrderOps",
                uri
            )
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        val chooser = Intent.createChooser(
            shareIntent,
            "Compartilhar comprovante"
        )

        context.startActivity(chooser)
    }
}