package com.harry.domain.repository

import com.harry.domain.model.ApiResult
import kotlinx.coroutines.flow.Flow

/**
 * Object Detection 결과를 받아 오는 Repository
 * @author harry
 */
interface ObjectDetectionResultRepository {
    suspend operator fun invoke(): Flow<ApiResult>
}