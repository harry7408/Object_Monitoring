package com.harry.data.datasource

import com.harry.data.model.ApiResultDto

interface Api {
    suspend fun getResults(): ApiResultDto
}