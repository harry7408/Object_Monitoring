package com.harry.domain.model

/**
 * Camera의 Image 정보를 담는 Data Class
 * Android 의존성이 없으므로 ByteArray 형태로 받아와야 한다
 * @author harry
 */
data class ImageFrame(
    val imageData: ByteArray,
    val width: Int,
    val height: Int,
    val rotationDegree: Int,
)
