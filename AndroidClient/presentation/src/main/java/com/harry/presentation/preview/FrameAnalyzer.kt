package com.harry.presentation.preview

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy

class FrameAnalyzer(
    private val onImageCaptured : (ImageProxy) -> Unit
) : ImageAnalysis.Analyzer{
    private var lastAnalyzerTime = 0L

    override fun analyze(image: ImageProxy) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastAnalyzerTime >=1000L) {
            lastAnalyzerTime = currentTime
            onImageCaptured(image)
        }
        image.close()
    }
}