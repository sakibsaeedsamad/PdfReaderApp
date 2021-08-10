package com.sssakib.pdfreaderapp

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Build
import android.os.ParcelFileDescriptor
import android.widget.ImageView
import java.io.File

class PdfReader(file: File) {

    private var currentPage: PdfRenderer.Page? = null
    private val fileDescriptor =
        ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
    private val pdfRenderer = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        PdfRenderer(fileDescriptor)
    } else {
        TODO("VERSION.SDK_INT < LOLLIPOP")
    }

    val pageCount = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        pdfRenderer.pageCount
    } else {
        TODO("VERSION.SDK_INT < LOLLIPOP")
    }

    fun openPage(page: Int, pdfImage: ImageView) {
        if (page >= pageCount) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            currentPage?.close()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            currentPage = pdfRenderer.openPage(page).apply {
                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                pdfImage.setImageBitmap(bitmap)
            }
        }
    }

    fun close() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            currentPage?.close()
        }
        fileDescriptor.close()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pdfRenderer.close()
        }
    }
}
