package com.harry.data.datasource

import com.harry.data.model.ApiResultDto
import com.harry.domain.model.ApiResult
import com.harry.domain.model.ImageFrame
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Inject

class KtorApi @Inject constructor() : Api {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    // 데이터를 가져오는 부분 -> Post Mapping(Multipart Body 이미지 형식 필요)
    override suspend fun getResults(imageFrame: ImageFrame): ApiResultDto {
        val response: HttpResponse = client.submitFormWithBinaryData(
            url = "http://192.168.45.212:8080/api/object/monitor",
            formData = formData {
                append(
                    "image",
                    imageFrame.imageData,
                    Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"image.jpg\"")
                    })
            }
        )
        Timber.v(response.body<ApiResult>().toString())
        return response.body()
    }
}