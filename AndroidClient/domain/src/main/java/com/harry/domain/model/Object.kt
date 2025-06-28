package com.harry.domain.model

/**
 * REST API로 받아오는 JSON 데이터 형식
 * @author harry
 */
data class ApiResult(
    val resultCode: String,
    val message: String,
    val data: List<Result>,
)

/**
 * Object Detect 결과 Domain Model
 */
data class Result(
    val item: String,
    val confidence: Double,
    val left: Double,
    val top: Double,
    val right: Double,
    val bottom: Double
)
