package com.harry.data.datasource

import com.harry.data.model.ApiResultDto
import com.harry.domain.model.ImageFrame

interface Api {
    suspend fun getResults(imageFrame: ImageFrame): ApiResultDto
}