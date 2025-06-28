package com.harry.presentation.util

import androidx.camera.core.ImageProxy
import com.harry.domain.model.ImageFrame

fun ImageProxy.toDomain(): ImageFrame {


    return ImageFrame(
        // 변환하는 부분 필요
        imageData = ByteArray(0),
        width = width,
        height = height,
        rotationDegree = imageInfo.rotationDegrees
    )

}