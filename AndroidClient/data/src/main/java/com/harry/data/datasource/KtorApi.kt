package com.harry.data.datasource

import com.harry.data.model.ApiResultDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorApi : Api {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    // 데이터를 가져오는 부분
    override suspend fun getResults(): ApiResultDto {
        val response: HttpResponse = client.get("http://localhost:8081/api/object/monitor")
        return response.body()
    }
}