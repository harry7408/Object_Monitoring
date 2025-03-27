package com.harry.data.repository

import com.harry.data.datasource.KtorApi
import com.harry.data.model.toDomainModel
import com.harry.domain.model.ApiResult
import com.harry.domain.repository.ObjectRepository
import javax.inject.Inject

class ObjectRepositoryImpl @Inject constructor(
    private val api: KtorApi
) : ObjectRepository {

    override suspend fun getResults(): ApiResult {
        return api.getResults().toDomainModel()
    }
}