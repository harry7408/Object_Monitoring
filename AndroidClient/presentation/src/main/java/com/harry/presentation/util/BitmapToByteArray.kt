package com.harry.presentation.util

import android.graphics.Bitmap
import java.io.ByteArrayOutputStream

fun bitmapToByteArray(bitmap: Bitmap, quality: Int = 100): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
    return stream.toByteArray()
}