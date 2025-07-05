package com.harry.presentation.main

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.harry.presentation.util.YuvToRgbConverter
import androidx.core.graphics.createBitmap
import com.harry.domain.model.ImageFrame
import com.harry.presentation.util.bitmapToByteArray

class FrameAnalyzer(
    private val context: Context,
    private val onImageCaptured: (ImageFrame) -> Unit
) : ImageAnalysis.Analyzer {

    // Android 12 버전 이후 Deprecated 된 Class가 존재하지만 변환하는 가장 편한 방법
    private val yuvToRgbConverter = YuvToRgbConverter(context)

    private var lastAnalyzerTime = 0L

    @ExperimentalGetImage
    override fun analyze(image: ImageProxy) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastAnalyzerTime >= 1000L) {
            lastAnalyzerTime = currentTime

            val bitmap = createBitmap(image.width, image.height)
            yuvToRgbConverter.yuvToRgb(image.image!!, bitmap)

            // 화면 회전에 대한 대응
            val rotatedBitmap = if (image.imageInfo.rotationDegrees != 0) {
                val matrix = Matrix().apply { postRotate(image.imageInfo.rotationDegrees.toFloat()) }
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            } else {
                bitmap
            }

            val byteArray = bitmapToByteArray(rotatedBitmap)

            val imageFrame = ImageFrame(
                imageData = byteArray,
                width = if (image.imageInfo.rotationDegrees == 90 || image.imageInfo.rotationDegrees == 270) image.height else image.width,
                height = if (image.imageInfo.rotationDegrees == 90 || image.imageInfo.rotationDegrees == 270) image.width else image.height,
                rotationDegree = 0
            )

            onImageCaptured(imageFrame)

            // Bitmap 새로 생성할 시 recycle 호출
            if (rotatedBitmap != bitmap && !rotatedBitmap.isRecycled) {
                rotatedBitmap.recycle()
            }
        }
        image.close()
    }
}