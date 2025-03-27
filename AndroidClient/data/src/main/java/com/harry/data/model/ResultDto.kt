package com.harry.data.model

import com.harry.domain.model.ApiResult
import com.harry.domain.model.Result
import kotlinx.serialization.Serializable

@Serializable
data class ApiResultDto(
    val resultCode: String,
    val message: String,
    val data: List<ResultDto>,
)

// Domain Model로 매핑하는 Extension Func
fun ApiResultDto.toDomainModel(): ApiResult {
    return ApiResult(
        resultCode = resultCode,
        message = message,
        data = data.map { it.toDomainModel() }
    )
}

@Serializable
data class ResultDto(
    val item: String,
    val confidence: Double,
    val left: Double,
    val top: Double,
    val right: Double,
    val bottom: Double
)

fun ResultDto.toDomainModel(): Result {
    return Result(
        item = item,
        confidence = confidence,
        left = left,
        top = top,
        right = right,
        bottom = bottom
    )
}
