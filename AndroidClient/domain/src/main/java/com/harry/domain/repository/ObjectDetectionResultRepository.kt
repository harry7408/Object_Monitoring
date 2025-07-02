package com.harry.domain.repository

import com.harry.domain.model.ApiResult
import com.harry.domain.model.ImageFrame
import kotlinx.coroutines.flow.Flow

/**
 * Object Detection 결과를 받아 오는 Repository
 * @author harry
 */
interface ObjectDetectionResultRepository {
    suspend operator fun invoke(imageFrame: ImageFrame): Flow<ApiResult>
}