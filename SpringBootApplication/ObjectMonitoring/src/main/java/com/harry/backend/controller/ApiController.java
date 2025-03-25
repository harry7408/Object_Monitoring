package com.harry.backend.controller;

import com.harry.backend.Api;
import com.harry.backend.dto.FastApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController {

    @PostMapping(
            path = "/object/monitor",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Api<FastApiResponse> results(
            @RequestPart("image") MultipartFile image
    ) {
        try {
            FastApiResponse response = getResultFromApi(image);

            if (response == null) {
                return Api.<FastApiResponse>builder()
                        .resultCode(String.valueOf(HttpStatus.OK))
                        .message("식별된 물체가 없습니다")
                        .build();
            } else {
                return Api.<FastApiResponse>builder()
                        .resultCode(String.valueOf(HttpStatus.OK))
                        .message("결과")
                        .data(response)
                        .build();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return Api.<FastApiResponse>builder()
                    .resultCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
                    .build();
        }
    }

    // 이미지를 분석하여 결과를 출력하는 API와 통신
    private FastApiResponse getResultFromApi(MultipartFile image) throws Exception {
        try {
            Resource fileResource = new ByteArrayResource(image.getBytes()) {
              @Override
              public String getFilename() {return image.getOriginalFilename();}
            };

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", fileResource);

            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

            // 통신 요청 부분
            ResponseEntity<FastApiResponse> response =
                    restTemplate.postForEntity("http://localhost:8081/object_monitor", request, FastApiResponse.class);
            return response.getBody();
        } catch (Exception e) {
            throw new Exception();
        }
    }
}



