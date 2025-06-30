package com.harry.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.graphics.Matrix
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.harry.domain.model.ImageFrame
import timber.log.Timber
import java.io.ByteArrayOutputStream

/**
 * UseCase 호출을 위해 Domain Model로 Mapping 하는 Extension Function
 */
@OptIn(ExperimentalGetImage::class)
fun ImageProxy.toDomain(context: Context, reusableBitmap: Bitmap): ImageFrame? {
    if (this.format != ImageFormat.YUV_420_888) {
        this.close()
        return null
    }

    try {
        // YUV -> RGB 전환 Helper Class
        val yuvToRgbConverter = YuvToRgbConverter(context = context)

        yuvToRgbConverter.yuvToRgb(this.image!!, reusableBitmap)

        val rotationDegree = this.imageInfo.rotationDegrees

        // Bitmap 생성
        val rotatedBitmap = if (rotationDegree != 0) {
            val matrix = Matrix().apply { postRotate(rotationDegree.toFloat()) }
            Bitmap.createBitmap(
                reusableBitmap,
                0,
                0,
                reusableBitmap.width,
                reusableBitmap.height,
                matrix,
                true
            )
        } else {
            reusableBitmap
        }

        // Bitmap 생성 후 ByteArray로 변환
        val finalBytes = ByteArrayOutputStream().use { outputStream ->
            rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.toByteArray()
        }

        // 회전된 Bitmap이 새로 생성된 경우만 recycle
        if (rotatedBitmap != reusableBitmap && !rotatedBitmap.isRecycled) {
            rotatedBitmap.recycle()
        }

        return ImageFrame(
            imageData = finalBytes,
            width = if (rotationDegree == 90 || rotationDegree == 270) height else width,
            height = if (rotationDegree == 90 || rotationDegree == 270) width else height,
            rotationDegree = 0,
        )

    } catch (e: Exception) {
        Timber.tag("ExtensionFunc").e(e, "Error In Domain Conversion")
        return null
    } finally {
        close()
    }
}