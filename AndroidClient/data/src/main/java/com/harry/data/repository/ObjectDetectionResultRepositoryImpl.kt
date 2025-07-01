package com.harry.data.repository

import com.harry.data.datasource.KtorApi
import com.harry.data.model.toDomainModel
import com.harry.domain.model.ApiResult
import com.harry.domain.repository.ObjectDetectionResultRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObjectDetectionResultRepositoryImpl @Inject constructor(
    private val api: KtorApi
) : ObjectDetectionResultRepository {

    override suspend fun invoke(): Flow<ApiResult> {
        TODO("Not yet implemented")
    }


}