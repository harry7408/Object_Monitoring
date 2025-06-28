package com.harry.data.repository

import com.harry.data.datasource.KtorApi
import com.harry.data.model.toDomainModel
import com.harry.domain.model.ApiResult
import com.harry.domain.repository.ObjectDetectionResultRepository
import javax.inject.Inject

class ObjectRepositoryImpl @Inject constructor(
    private val api: KtorApi
) : ObjectDetectionResultRepository {

    override suspend fun getResults(): ApiResult {
        return api.getResults().toDomainModel()
    }
}