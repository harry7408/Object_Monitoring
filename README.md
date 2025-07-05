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
    
   
