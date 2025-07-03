package com.harry.data.model

import com.harry.domain.model.ApiResult
import com.harry.domain.model.Data
import com.harry.domain.model.Result
import kotlinx.serialization.Serializable

@Serializable
data class ApiResultDto(
    val resultCode: String,
    val message: String,
    val data: DataDto,
)

@Serializable
data class DataDto(
    val result: List<ResultDto>
)

@Serializable
data class ResultDto(
    val item: String,
    val confidence: Double,
    val left: Double,
    val top: Double,
    val right: Double,
    val bottom: Double
)


// Domain Model로 매핑하는 Extension Func
fun ApiResultDto.toDomainModel(): ApiResult {
    return ApiResult(
        resultCode = resultCode,
        message = message,
        data = data.toDomainModel()
    )
}

fun DataDto.toDomainModel(): Data {
    return Data(
        result = result.map { it.toDomainModel() }
    )
}

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
