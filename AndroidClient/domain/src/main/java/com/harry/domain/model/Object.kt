package com.harry.domain.model


data class ApiResult(
    val resultCode: String,
    val message: String,
    val data : List<Result>,
)


data class Result(
    val item: String,
    val confidence: Double,
    val left: Double,
    val top: Double,
    val right: Double,
    val bottom: Double
)
