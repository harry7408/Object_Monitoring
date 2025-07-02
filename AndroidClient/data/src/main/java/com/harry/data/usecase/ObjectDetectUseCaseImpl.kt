package com.harry.data.usecase

import com.harry.domain.model.ApiResult
import com.harry.domain.model.ImageFrame
import com.harry.domain.repository.ObjectDetectionResultRepository
import com.harry.domain.usecase.ObjectDetectUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObjectDetectUseCaseImpl @Inject constructor(
    private val repository: ObjectDetectionResultRepository
) : ObjectDetectUseCase {
    override suspend fun invoke(imageFrame: ImageFrame): Flow<ApiResult> {
        // Repository 에서 나오는 값 by-passing
        return repository(imageFrame)
    }
}