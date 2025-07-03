package com.harry.presentation.main

import android.content.Context
import android.graphics.Bitmap
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

    //private var lastAnalyzeTime = 0L
    private val yuvToRgbConverter = YuvToRgbConverter(context)

    private var lastAnalyzerTime = 0L

    @ExperimentalGetImage
    override fun analyze(image: ImageProxy) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastAnalyzerTime >= 1000L) {
            lastAnalyzerTime = currentTime

            val bitmap = createBitmap(image.width, image.height)
            yuvToRgbConverter.yuvToRgb(image.image!!, bitmap)

            val byteArray = bitmapToByteArray(bitmap)
            val imageFrame = ImageFrame(
                imageData = byteArray,
                width = image.width,
                height = image.height,
                rotationDegree = image.imageInfo.rotationDegrees
            )

            onImageCaptured(imageFrame)
        }
        image.close()
    }
}