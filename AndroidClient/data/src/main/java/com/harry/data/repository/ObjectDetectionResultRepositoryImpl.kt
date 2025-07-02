package com.harry.data.repository

import com.harry.data.datasource.KtorApi
import com.harry.data.model.toDomainModel
import com.harry.domain.model.ApiResult
import com.harry.domain.model.ImageFrame
import com.harry.domain.repository.ObjectDetectionResultRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ObjectDetectionResultRepositoryImpl @Inject constructor(
    private val api: KtorApi
) : ObjectDetectionResultRepository {

    override suspend fun invoke(imageFrame: ImageFrame): Flow<ApiResult> = flow {
        val response = api.getResults(imageFrame)
        // 값 emit
        emit(response.toDomainModel())
    }
}