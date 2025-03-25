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
