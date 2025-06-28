package com.harry.domain.usecase

import com.harry.domain.model.ApiResult
import com.harry.domain.model.ImageFrame
import kotlinx.coroutines.flow.Flow

/**
 * 물체 인식을 요청 하는 UseCase
 * Repository 결과를 By Passing
 * @author harry
 */
interface ObjectDetectUseCase {
    // TODO : 이미지 넘길 형식이 필요
    suspend operator fun invoke(imageFrame: ImageFrame): Flow<ApiResult>

}