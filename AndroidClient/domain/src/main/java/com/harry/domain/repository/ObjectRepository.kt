package com.harry.domain.repository

import com.harry.domain.model.ApiResult

interface ObjectRepository {

    suspend fun getResults() : ApiResult
}