package com.harry.domain.model

import kotlinx.serialization.Serializable

/**
 * REST API로 받아오는 JSON 데이터 형식
 * @author harry
 */
@Serializable
data class ApiResult(
    val resultCode: String,
    val message: String,
    val data: Data,
)

@Serializable
data class Data(
    val result: List<Result>
)

/**
 * Object Detect 결과 Domain Model
 */
@Serializable
data class Result(
    val item: String,
    val confidence: Double,
    val left: Double,
    val top: Double,
    val right: Double,
    val bottom: Double
)
