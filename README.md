## [A.I_Application]
- 전달 받은 이미지를 분석 후 전송하는 Role

### Fast API 실행 방법

1. Anaconda 환경 설치</br>
📎 [Download Anaconda Distribution | Anaconda](https://www.anaconda.com/download)
2. Anaconda Prompt 실행 후 Virtual Environment 만들기</br>
`conda create -n [가상 환경 이름] python=3.10` 
3. Virtual Environment 활성화</br>
`conda activate [가상 환경 이름]`
4. 실행에 필요한 필수 패키지 설치</br>
`pip install fastapi uvicorn[standard] pillow pandas numpy loguru ultralytics python-multipart`
5. Fast Api 실행 (main.py가 있는 Directory 이동)</br>
`uvicorn main:app --port 8081 --reoload`

### About API

> Swagger
> 

```
# Fast Api 제공
http://localhost:8081/docs

# Redircet url
http://localhost:8081
```

> API
> 

```
# 이미지 분석 요청 End Point
http://localhost:8081/object_monitor
```

- Request
    - Multipart_Image
- Response
    - Json String Example
    
    ```json
    {
      "result": [
        {
          "item": "dog", // 종류
          "confidence": 0.9259905815, // 분석 정확도
          "left": 100.615234375, // Bounding Box의 좌측 경계선
          "top": 0.9213780761, // Bounding Box의 상단 경계선
          "right": 203.5078887939, // Bounding Box의 우측 경계선
          "bottom": 162.1179656982 // Bounding Box의 하단 경계선
        },
        {
          "item": "dog",
          "confidence": 0.8665550947,
          "left": 5.903427124,
          "top": 19.0094356537,
          "right": 87.5690841675,
          "bottom": 125.6044082642
        }
      ]
    }
    ```
---
    
## SpringBootApplication
- Android Client 측과 이미지 분석 Server 의 중간 노드
### About API

> Swagger
> 

```
http://localhost:8080/api-swagger
```

- yaml 파일에서 설정

> API
> 

```
POST 요청
http://localhost:8080/api/object/monitor
```

- Request
    - Multipart Image
- Response
    - result Code, 메세지, A.I Fast API 측에서 받아온 데이터
    
    ```java
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class Api<T> {
    	private String resultCode;
    	private String message;
    	private T data;
    }
    ```
     
    - Json String Example
    
    ```json
    {
      "resultCode": "200 OK",
      "message": "결과",
      "data": {
        "result": [
          {
            "item": "dog",
            "confidence": 0.9259905815,
            "left": 100.615234375,
            "top": 0.9213780761,
            "right": 203.5078887939,
            "bottom": 162.1179656982
          },
          {
            "item": "dog",
            "confidence": 0.8665550947,
            "left": 5.903427124,
            "top": 19.0094356537,
            "right": 87.5690841675,
            "bottom": 125.6044082642
          }
        ]
      }
    }
    ```


    ⭐ A.I 측에서 분석한 결과를 받아서 그대로 데이터만 By-Passing 하면 Android Client↔ Spring Boot Server ↔ A.I Server 3개의 노드 간 통신하는 것이 의미가 없어 Spring Boot Server 측에서 Confidence 값이 0.75 이상인 결과만 넘겨주도록 필터링 추가


    ```java
    // ✅ confidence 0.75 이상만 필터링
            List<FastApiResponse.ObjectDto> filteredResults = response.getResult().stream()
                    .filter(result -> result.getConfidence() >= 0.75)
                    .collect(Collectors.toList());
    
            FastApiResponse filteredResponse = new FastApiResponse();
            filteredResponse.setResult(filteredResults);
    
            return Api.<FastApiResponse>builder()
                    .resultCode(String.valueOf(HttpStatus.OK))
                    .message("식별된 결과")
                    .data(filteredResponse)
                    .build();
    ```
    
    +필터링 Logic 추가와 더불어 Controller에 있던 코드 → Service로 전환하여 Controller 코드엔 API 제공 Method만 존재하도록 수정

    <details>
    <summary>필터링 결과</summary>
    <div markdown="1">
    
    - 상단의 A.I 분석 측에서 confidence값이 0.75를 넘기지 못하는 인식된 물체는 Client 측에 전송 X
  
      - (상단 : A.I, 하단 : Spring Boot Server)
    
    <img width="1104" height="454" alt="Image" src="https://github.com/user-attachments/assets/55980c42-9b15-47a2-ab4d-c6bad7c4bde7" />
    </div>
    </details>
---
    
## Android Client

Spring Boot Application 과 REST API를 통해 이미지를 전송하여 분석된 물체 정보를 불러오는 역할

### 구현 내용 및 주요 라이브러리

- Clean Architecture(app, data, domain, presentation Module) 적용
- MVI(Model - ViewModel - Intent) : Orbit 라이브러리 사용
- JetPack Compose UI, CameraX 라이브러리 사용
- 의존성 주입 : Hilt
- Ktor Client로 API 요청
- Timber, TedPermission 등 3rd Party 라이브러리 활용

---

- Request
    - Multipart Image
- Response
    - result Code, 메세지, 데이터
    
    ```kotlin
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
    ```

    <details>
    <summary>주요 내용</summary>
    <div markdown="1">

   - Api 요청을 가장 많이 활용되는 Retrofit 라이브러리가 아닌 JetBrains 사에서 만든 경량 웹 Framework인 Ktor 사용
    
    > KtorApi.kt
    > 
    
    ```kotlin
    interface Api {
        suspend fun getResults(imageFrame: ImageFrame): ApiResultDto
    }
    
    class KtorApi @Inject constructor() : Api {
    
        private val client = HttpClient {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }
    
        // 데이터를 가져오는 부분 -> Post Mapping(Multipart Body 이미지 형식 필요)
        override suspend fun getResults(imageFrame: ImageFrame): ApiResultDto {
            val response: HttpResponse = client.submitFormWithBinaryData(
                url = API_ENDPOINT,
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
    ```
        
    > Retrofit은 Service 인터페이스 정의 후 생성하는 코드, OkHttpClient, Json Converter 등의 설정이 필요했으나 Ktor는 위에 있는 코드로 API 요청이 끝이 나서 매우 간결해진다
    
    > End Point 같은 경우 Emulator를 쓴다면 `10.0.2.2/~` , 실제 Device에서 동작한다면 `서버 실행 Node의 주소/~` 로 입력하면 된다.


    - YUV → RGB (Bitmap)
        
        > YuvToRgbConverter.kt
        > 
        
        ```kotlin
        class YuvToRgbConverter(context: Context) {
            private val rs = RenderScript.create(context)
            private val scriptYuvToRgb = ScriptIntrinsicYuvToRGB.create(rs, Element.U8_4(rs))
        
            private var pixelCount: Int = -1
            private lateinit var yuvBuffer: ByteBuffer
            private lateinit var inputAllocation: Allocation
            private lateinit var outputAllocation: Allocation
        
            @Synchronized
            fun yuvToRgb(image: Image, output: Bitmap) {
        
                // Ensure that the intermediate output byte buffer is allocated
                if (!::yuvBuffer.isInitialized) {
                    pixelCount = image.cropRect.width() * image.cropRect.height()
                    yuvBuffer = ByteBuffer.allocateDirect(
                        pixelCount * ImageFormat.getBitsPerPixel(ImageFormat.YUV_420_888) / 8
                    )
                }
        
                // Get the YUV data in byte array form
                imageToByteBuffer(image, yuvBuffer)
        
                // Ensure that the RenderScript inputs and outputs are allocated
                if (!::inputAllocation.isInitialized) {
                    inputAllocation = Allocation.createSized(rs, Element.U8(rs), yuvBuffer.array().size)
                }
                if (!::outputAllocation.isInitialized) {
                    outputAllocation = Allocation.createFromBitmap(rs, output)
                }
        
                // Convert YUV to RGB
                inputAllocation.copyFrom(yuvBuffer.array())
                scriptYuvToRgb.setInput(inputAllocation)
                scriptYuvToRgb.forEach(outputAllocation)
                outputAllocation.copyTo(output)
            }
        
            private fun imageToByteBuffer(image: Image, outputBuffer: ByteBuffer) {
                assert(image.format == ImageFormat.YUV_420_888)
        
                val imageCrop = image.cropRect
                val imagePlanes = image.planes
                val rowData = ByteArray(imagePlanes.first().rowStride)
        
                imagePlanes.forEachIndexed { planeIndex, plane ->
                    val outputStride: Int
        
                    var outputOffset: Int
        
                    when (planeIndex) {
                        0 -> {
                            outputStride = 1
                            outputOffset = 0
                        }
        
                        1 -> {
                            outputStride = 2
                            outputOffset = pixelCount + 1
                        }
        
                        2 -> {
                            outputStride = 2
                            outputOffset = pixelCount
                        }
        
                        else -> {
                            // Image contains more than 3 planes, something strange is going on
                            return@forEachIndexed
                        }
                    }
        
                    val buffer = plane.buffer
                    val rowStride = plane.rowStride
                    val pixelStride = plane.pixelStride
        
                    // We have to divide the width and height by two if it's not the Y plane
                    val planeCrop = if (planeIndex == 0) {
                        imageCrop
                    } else {
                        Rect(
                            imageCrop.left / 2,
                            imageCrop.top / 2,
                            imageCrop.right / 2,
                            imageCrop.bottom / 2
                        )
                    }
        
                    val planeWidth = planeCrop.width()
                    val planeHeight = planeCrop.height()
        
                    buffer.position(rowStride * planeCrop.top + pixelStride * planeCrop.left)
                    for (row in 0 until planeHeight) {
                        val length: Int
                        if (pixelStride == 1 && outputStride == 1) {
                            // When there is a single stride value for pixel and output, we can just copy
                            // the entire row in a single step
                            length = planeWidth
                            buffer.get(outputBuffer.array(), outputOffset, length)
                            outputOffset += length
                        } else {
                            // When either pixel or output have a stride > 1 we must copy pixel by pixel
                            length = (planeWidth - 1) * pixelStride + 1
                            buffer.get(rowData, 0, length)
                            for (col in 0 until planeWidth) {
                                outputBuffer.array()[outputOffset] = rowData[col * pixelStride]
                                outputOffset += outputStride
                            }
                        }
        
                        if (row < planeHeight - 1) {
                            buffer.position(buffer.position() + rowStride - length)
                        }
                    }
                }
            }
        }
        ```
        
        > Android Camera Preview 등에서는 메모리 공간 절약을 위해 RGB 형식이 아닌 YUV_420_ 888 형식으로 데이터를 제공하지만 Preview에 데이터를 띄우려면 RGB Format으로 전환을 해야한다
        
        > 위의 Class는 기존에 CameraX 1.3.x 버전에서는 지원했으나 Android 12 이상 버전에서는 Deprecated 된 Class 및 메서드가 많아서 끊긴 것 같다
    </div>
    </details>

---
## 시연 영상

https://github.com/user-attachments/assets/fa0f7459-f035-4b97-9adf-6bd2da550c08
    
---

## Trouble Shooting

🖇️ [https://www.notion.so/Trouble-Shooting-1bfa531acec880d79dfaf0cd8feeb22e?source=copy_link](https://www.notion.so/Trouble-Shooting-1bfa531acec880d79dfaf0cd8feeb22e?pvs=21)

---

## 회고

- Android에서 하드웨어에서 받아온 Image를 다루는 부분이 생각보다 복잡했다
- Clean Architecture을 적용하고 Orbit 라이브러리를 사용하여 MVI 패턴을 적용할 수 있었다
    - Screen간 전환이 없어 Navigation Compose를 적용하지 못한 부분에 대한 아쉬움
    - Orbit 라이브러리를 쓴다면 MVI 패턴도 코드 구조가 Boiler-Plate 할 것 같음을 느낌
- Trouble Shooting에서 Profiler 기능을 이용해서 메모리 누수를 발견하고 이를 해결할 수 있었다
- 간단하지만 Spring Boot, Yolo 모델을 사용하여 Object Detection을 수행하고 Fast API를 사용해 데이터를 전송할 수 있었다
---
```
라이센스 (License)

이 프로젝트는 YOLOv11 모델을 사용하며, 다음 라이센스 조건을 따릅니다:

- **YOLOv11**: AGPL-3.0 License (Ultralytics)
- **프로젝트 코드**: AGPL-3.0 License

중요 고지사항

본 프로젝트는 [Ultralytics YOLOv11](https://github.com/ultralytics/ultralytics)을 사용합니다.
YOLOv11은 AGPL-3.0 라이센스 하에 배포되므로, 이 프로젝트의 모든 코드와 수정사항은 
동일한 라이센스 조건을 따라야 합니다.

상업적 사용을 위해서는 [Ultralytics Enterprise License](https://ultralytics.com/license)를 
구매해야 할 수 있습니다.

License

This project uses YOLOv11 under the AGPL-3.0 License.
- YOLOv11 by Ultralytics: AGPL-3.0
- Project code: AGPL-3.0

For commercial use, please consider purchasing an [Ultralytics Enterprise License](https://ultralytics.com/license).

```
